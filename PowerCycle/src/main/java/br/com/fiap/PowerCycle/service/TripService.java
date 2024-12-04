package br.com.fiap.PowerCycle.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import br.com.fiap.PowerCycle.exception.TripException;
import br.com.fiap.PowerCycle.model.*;
import br.com.fiap.PowerCycle.response.TripNotFoundException;

public class TripService {

    private final TripDAOImpl tripDAO;

    public TripService() {
        this.tripDAO = new TripDAOImpl();
    }

    public List<Trip> getAllTrips() throws TripException {
        try {
            return tripDAO.fetchTripsWithRecoveries();
        } catch (SQLException e) {
            throw new TripException("Failed to fetch trips: " + e.getMessage(), e);
        }
    }

    public Trip getTripById(String tripId) throws TripException {
        try {
            Trip trip = tripDAO.fetchTripById(tripId);
            if (trip == null) {
                throw new TripNotFoundException("Trip with ID " + tripId + " not found.");
            }
            return trip;
        } catch (SQLException e) {
            throw new TripException("Failed to fetch trip: " + e.getMessage(), e);
        }
    }

    public void registerTrip(Trip trip) throws TripException {
        validateTrip(trip);
        processAdditionalRequestInfo(trip);
        tripDAO.registerTrip(trip);
    }

    public void updateTrip(Trip trip) throws TripException {
        try {
            if (tripDAO.fetchTripById(trip.getId()) == null) {
                throw new TripNotFoundException("Trip with ID " + trip.getId() + " not found.");
            }
            processAdditionalRequestInfo(trip);
            tripDAO.updateTrip(trip);
        } catch (SQLException e) {
            throw new TripException("Failed to update trip: " + e.getMessage(), e);
        }
    }

    public void deleteTrip(String tripId) throws TripException {
        try {
            if (tripDAO.fetchTripById(tripId) == null) {
                throw new TripNotFoundException("Trip with ID " + tripId + " not found.");
            }
            tripDAO.deleteTripById(tripId);
        } catch (TripNotFoundException e) {
            throw e; // Relançar para o controlador
        } catch (SQLException e) {
            throw new TripException("Error deleting trip: " + e.getMessage(), e);
        }
    }


    private void processAdditionalRequestInfo(Trip trip) throws TripException {
        if (trip.getVehicle_id() == null || trip.getVehicle_id().isEmpty()) {
            throw new TripException("Vehicle ID is null or empty. Cannot process the request.");
        }
        

        Vehicle vehicle = VehicleDataFetcher.fetchVehicleData(trip.getVehicle_id());
        if (vehicle == null) {
            throw new TripException("Vehicle data could not be fetched for ID: " + trip.getVehicle_id());
        }

        trip.setTotalRecoveredEnergy(calculateTotalRecoveredEnergy(trip.getRecovery()));
        trip.setAutonomyExtra(calculateExtraAutonomyGenerated(trip.getTotalRecoveredEnergy(), vehicle.efficiency.realWorld));
        trip.setEnergySavedPercent(calculateEnergyConsumptionSavingsPercent(trip.getTotalConsumption(), trip.getTotalRecoveredEnergy()));
        trip.setRechargeNeeded(calculateRequiredLoadToCompensateConsumption(trip.getTotalConsumption(), trip.getTotalRecoveredEnergy()));
        trip.setTimeSavedCharging(calculateChargeTimeSaved(trip.getTotalRecoveredEnergy(), vehicle.charging.dcPower));
    }

    private double calculateTotalRecoveredEnergy(List<EnergyRecovery> recoveryEnergy) {
        return recoveryEnergy.stream()
                .mapToDouble(EnergyRecovery::getRecoveredEnergy)
                .sum();
    }

    private BigDecimal calculateExtraAutonomyGenerated(double recoveredEnergy, double efficiency) {
        return BigDecimal.valueOf(recoveredEnergy / (efficiency / 1000))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateEnergyConsumptionSavingsPercent(double totalConsumption, double recoveredEnergy) {
        return BigDecimal.valueOf((recoveredEnergy / totalConsumption) * 100)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateRequiredLoadToCompensateConsumption(double totalConsumption, double recoveredEnergy) {
        return BigDecimal.valueOf(totalConsumption - recoveredEnergy)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateChargeTimeSaved(double recoveredEnergy, int chargingPower) {
        return BigDecimal.valueOf(recoveredEnergy / chargingPower)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void validateTrip(Trip trip) throws TripException {
        if (trip.getTotalConsumption() <= 0) {
            throw new TripException("Total consumption must be greater than zero.");
        }
        if (trip.getRecovery() == null || trip.getRecovery().isEmpty()) {
            throw new TripException("Recovery data cannot be null or empty.");
        }
    }
}

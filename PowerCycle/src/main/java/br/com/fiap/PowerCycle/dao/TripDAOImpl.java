package br.com.fiap.PowerCycle.dao;

import java.sql.*;
import java.util.*;

import br.com.fiap.PowerCycle.exception.TripException;
import br.com.fiap.PowerCycle.model.EnergyRecovery;
import br.com.fiap.PowerCycle.model.Trip;
import br.com.fiap.PowerCycle.util.ConectionDB;

public class TripDAOImpl implements TripDAO {

    // Constantes para strings SQL usando concatenação
    private static final String FETCH_TRIPS_QUERY = 
            "SELECT ID AS TRIP_ID, VEHICLE_ID, DISTANCE, START_TIME, END_TIME, " +
            "TOTAL_CONSUMPTION, TOTAL_RECOVERED_ENERGY, AUTONOMY_EXTRA, " +
            "ENERGY_SAVED_PERCENT, RECHARGE_NEEDED, TIME_SAVED_CHARGING " +
            "FROM TRIP ";

    private static final String FETCH_RECOVERIES_QUERY = 
            "SELECT ID AS ENERGY_RECOVERY_ID, TRIP_ID, RECOVERY_TYPE, RECOVERED_ENERGY, RECOVERY_TIME " +
            "FROM ENERGY_RECOVERY ";

    private static final String FETCH_TRIP_BY_ID_QUERY = 
            FETCH_TRIPS_QUERY + "WHERE ID = ?";

    private static final String FETCH_RECOVERIES_BY_TRIP_ID_QUERY = 
            FETCH_RECOVERIES_QUERY + "WHERE TRIP_ID = ?";

    private static final String INSERT_TRIP_QUERY = 
            "INSERT INTO TRIP (VEHICLE_ID, DISTANCE, START_TIME, END_TIME, TOTAL_CONSUMPTION, " +
            "TOTAL_RECOVERED_ENERGY, AUTONOMY_EXTRA, ENERGY_SAVED_PERCENT, " +
            "RECHARGE_NEEDED, TIME_SAVED_CHARGING) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_RECOVERY_QUERY = 
            "INSERT INTO ENERGY_RECOVERY (TRIP_ID, RECOVERY_TYPE, RECOVERED_ENERGY, RECOVERY_TIME) " +
            "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_TRIP_QUERY = 
            "UPDATE TRIP SET VEHICLE_ID = ?, DISTANCE = ?, START_TIME = ?, END_TIME = ?, " +
            "TOTAL_CONSUMPTION = ?, TOTAL_RECOVERED_ENERGY = ?, AUTONOMY_EXTRA = ?, " +
            "ENERGY_SAVED_PERCENT = ?, RECHARGE_NEEDED = ?, TIME_SAVED_CHARGING = ? " +
            "WHERE ID = ?";

    private static final String DELETE_RECOVERIES_QUERY = 
            "DELETE FROM ENERGY_RECOVERY WHERE TRIP_ID = ?";

    private static final String DELETE_TRIP_QUERY = 
            "DELETE FROM TRIP WHERE ID = ?";

    @Override
    public List<Trip> fetchTripsWithRecoveries() throws SQLException {
        Map<String, Trip> tripMap = new HashMap<>();
        Connection conn = ConectionDB.getInstance().getConn();

        // Busca as viagens
        try (PreparedStatement tripStmt = conn.prepareStatement(FETCH_TRIPS_QUERY);
             ResultSet tripRs = tripStmt.executeQuery()) {

            while (tripRs.next()) {
                Trip trip = mapTrip(tripRs);
                tripMap.put(trip.getId(), trip);
            }
        }

        // Busca as recuperações
        try (PreparedStatement recoveryStmt = conn.prepareStatement(FETCH_RECOVERIES_QUERY);
             ResultSet recoveryRs = recoveryStmt.executeQuery()) {

            while (recoveryRs.next()) {
                String tripId = recoveryRs.getString("TRIP_ID");
                Trip trip = tripMap.get(tripId);
                if (trip != null) {
                    trip.getRecovery().add(mapRecovery(recoveryRs));
                }
            }
        }

        return new ArrayList<>(tripMap.values());
    }

    @Override
    public Trip fetchTripById(String tripId) throws SQLException {
        Trip trip = null;
        Connection conn = ConectionDB.getInstance().getConn();

        // Busca a viagem
        try (PreparedStatement tripStmt = conn.prepareStatement(FETCH_TRIP_BY_ID_QUERY)) {
            tripStmt.setString(1, tripId);
            try (ResultSet tripRs = tripStmt.executeQuery()) {
                if (tripRs.next()) {
                    trip = mapTrip(tripRs);
                }
            }
        }

        // Busca as recuperações
        if (trip != null) {
            try (PreparedStatement recoveryStmt = conn.prepareStatement(FETCH_RECOVERIES_BY_TRIP_ID_QUERY)) {
                recoveryStmt.setString(1, tripId);
                try (ResultSet recoveryRs = recoveryStmt.executeQuery()) {
                    while (recoveryRs.next()) {
                        trip.getRecovery().add(mapRecovery(recoveryRs));
                    }
                }
            }
        }

        return trip;
    }

    @Override
    public String registerTrip(Trip trip) throws TripException {
        Connection conn = ConectionDB.getInstance().getConn();

        try (PreparedStatement tripStmt = conn.prepareStatement(INSERT_TRIP_QUERY, new String[]{"ID"})) {
            setTripParameters(tripStmt, trip);
            tripStmt.executeUpdate();
            
            ResultSet tripKeys = tripStmt.getGeneratedKeys();
	        String tripId = null;
	        if (tripKeys.next()) {
	            tripId = tripKeys.getString(1); // Captura o ID gerado pelo banco
	            insertRecoveries(conn, tripId, trip.getRecovery());
	            return tripId;
	        } else {
	            throw new TripException("Não foi possível recuperar o ID da viagem registrada.");
	        }
        } catch (SQLException e) {
            throw new TripException("Erro ao registrar a viagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateTrip(Trip trip) throws SQLException {
        Connection conn = ConectionDB.getInstance().getConn();

        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_TRIP_QUERY)) {
            setTripParameters(stmt, trip);
            stmt.setString(11, trip.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteTripById(String tripId) throws SQLException {
        Connection conn = ConectionDB.getInstance().getConn();

        try (PreparedStatement recoveryStmt = conn.prepareStatement(DELETE_RECOVERIES_QUERY);
             PreparedStatement tripStmt = conn.prepareStatement(DELETE_TRIP_QUERY)) {

            // Deleta recuperações
            recoveryStmt.setString(1, tripId);
            int recoveriesDeleted = recoveryStmt.executeUpdate();

            // Deleta viagem
            tripStmt.setString(1, tripId);
            int tripsDeleted = tripStmt.executeUpdate();

            if (tripsDeleted == 0) {
                throw new SQLException("No trip found with ID: " + tripId);
            }
        }
    }


    // Métodos auxiliares para mapear objetos
    private Trip mapTrip(ResultSet rs) throws SQLException {
        Trip trip = new Trip();
        trip.setId(rs.getString("TRIP_ID"));
        trip.setVehicle_id(rs.getString("VEHICLE_ID"));
        trip.setDistance(rs.getDouble("DISTANCE"));
        trip.setStartTime(rs.getTimestamp("START_TIME").toLocalDateTime());
        trip.setEndTime(rs.getTimestamp("END_TIME").toLocalDateTime());
        trip.setTotalConsumption(rs.getDouble("TOTAL_CONSUMPTION"));
        trip.setTotalRecoveredEnergy(rs.getDouble("TOTAL_RECOVERED_ENERGY"));
        trip.setAutonomyExtra(rs.getBigDecimal("AUTONOMY_EXTRA"));
        trip.setEnergySavedPercent(rs.getBigDecimal("ENERGY_SAVED_PERCENT"));
        trip.setRechargeNeeded(rs.getBigDecimal("RECHARGE_NEEDED"));
        trip.setTimeSavedCharging(rs.getBigDecimal("TIME_SAVED_CHARGING"));
        trip.setRecovery(new ArrayList<>());
        return trip;
    }

    private EnergyRecovery mapRecovery(ResultSet rs) throws SQLException {
        EnergyRecovery recovery = new EnergyRecovery();
        recovery.setId(rs.getString("ENERGY_RECOVERY_ID"));
        recovery.setRecoveryType(rs.getString("RECOVERY_TYPE"));
        recovery.setRecoveredEnergy(rs.getDouble("RECOVERED_ENERGY"));
        recovery.setRecoveryTime(rs.getTimestamp("RECOVERY_TIME").toLocalDateTime());
        return recovery;
    }

    private void setTripParameters(PreparedStatement stmt, Trip trip) throws SQLException {
    	stmt.setBytes(1, hexStringToByteArray(trip.getVehicle_id()));
        stmt.setDouble(2, trip.getDistance());
        stmt.setTimestamp(3, Timestamp.valueOf(trip.getStartTime()));
        stmt.setTimestamp(4, Timestamp.valueOf(trip.getEndTime()));
        stmt.setDouble(5, trip.getTotalConsumption());
        stmt.setDouble(6, trip.getTotalRecoveredEnergy());
        stmt.setBigDecimal(7, trip.getAutonomyExtra());
        stmt.setBigDecimal(8, trip.getEnergySavedPercent());
        stmt.setBigDecimal(9, trip.getRechargeNeeded());
        stmt.setBigDecimal(10, trip.getTimeSavedCharging());
    }

    private void insertRecoveries(Connection conn, String tripId, List<EnergyRecovery> recoveries) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_RECOVERY_QUERY)) {
            for (EnergyRecovery recovery : recoveries) {
                stmt.setBytes(1, hexStringToByteArray(tripId));
                stmt.setString(2, recovery.getRecoveryType());
                stmt.setDouble(3, recovery.getRecoveredEnergy());
                stmt.setTimestamp(4, Timestamp.valueOf(recovery.getRecoveryTime()));
                stmt.executeUpdate();
            }
        }
    }
    
    
    private static byte[] hexStringToByteArray(String hex) {
        // Remove os traços do UUID
        hex = hex.replace("-", "");

        // Valida que o UUID tem exatamente 32 caracteres
        if (hex.length() != 32) {
            throw new IllegalArgumentException("UUID inválido. Deve ter 32 caracteres após a remoção dos traços.");
        }

        // Converte para um array de bytes
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }



}

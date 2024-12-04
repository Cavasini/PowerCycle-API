package br.com.fiap.PowerCycle.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Trip {

    private String id; // UUID as the primary key
    private String vehicle_id; // Association with Vehicle
    private double distance; // Distance traveled (km)
    private LocalDateTime startTime; // Trip start time
    private LocalDateTime endTime; // Trip end time
    private double totalConsumption; // Actual energy consumption (kWh)
    private List<EnergyRecovery> recovery;
    
    private double totalRecoveredEnergy; // Total recovered energy (kWh)
    private BigDecimal autonomyExtra;
    private BigDecimal energySavedPercent;
    private BigDecimal rechargeNeeded;
    private BigDecimal timeSavedCharging;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVehicle_id() {
		return vehicle_id;
	}
	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public double getTotalConsumption() {
		return totalConsumption;
	}
	public void setTotalConsumption(double totalConsumption) {
		this.totalConsumption = totalConsumption;
	}
	public List<EnergyRecovery> getRecovery() {
		return recovery;
	}
	public void setRecovery(List<EnergyRecovery> recovery) {
		this.recovery = recovery;
	}
	public double getTotalRecoveredEnergy() {
		return totalRecoveredEnergy;
	}
	public void setTotalRecoveredEnergy(double totalRecoveredEnergy) {
		this.totalRecoveredEnergy = totalRecoveredEnergy;
	}
	public BigDecimal getAutonomyExtra() {
		return autonomyExtra;
	}
	public void setAutonomyExtra(BigDecimal autonomyExtra) {
		this.autonomyExtra = autonomyExtra;
	}
	public BigDecimal getEnergySavedPercent() {
		return energySavedPercent;
	}
	public void setEnergySavedPercent(BigDecimal energySavedPercent) {
		this.energySavedPercent = energySavedPercent;
	}
	public BigDecimal getRechargeNeeded() {
		return rechargeNeeded;
	}
	public void setRechargeNeeded(BigDecimal rechargeNeeded) {
		this.rechargeNeeded = rechargeNeeded;
	}
	public BigDecimal getTimeSavedCharging() {
		return timeSavedCharging;
	}
	public void setTimeSavedCharging(BigDecimal timeSavedCharging) {
		this.timeSavedCharging = timeSavedCharging;
	}
    

    
    
}

package br.com.fiap.PowerCycle.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class EnergyRecovery {

    private String id; // UUID as the primary key
    private String trip_id; // Association with Trip
    private String recoveryType; // Type of recovery (e.g., braking, heat)
    private double recoveredEnergy; // Energy recovered (kWh)
    private LocalDateTime recoveryTime; // Time of recovery
	
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTrip() {
		return trip_id;
	}
	public void setTrip(String trip_id) {
		this.trip_id = trip_id;
	}
	public String getRecoveryType() {
		return recoveryType;
	}
	public void setRecoveryType(String recoveryType) {
		this.recoveryType = recoveryType;
	}
	public double getRecoveredEnergy() {
		return recoveredEnergy;
	}
	public void setRecoveredEnergy(double recoveredEnergy) {
		this.recoveredEnergy = recoveredEnergy;
	}
	public LocalDateTime getRecoveryTime() {
		return recoveryTime;
	}
	public void setRecoveryTime(LocalDateTime recoveryTime) {
		this.recoveryTime = recoveryTime;
	}
    
    
}

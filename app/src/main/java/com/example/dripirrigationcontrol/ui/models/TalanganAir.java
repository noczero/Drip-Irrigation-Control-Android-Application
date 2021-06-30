package com.example.dripirrigationcontrol.ui.models;

public class TalanganAir {
    String name;
    String soilHumidity;
    long phValue;
    String solenoidStatus;
    String firebaseKey;

    public TalanganAir(String name, String soilHumidity, int phValue, String solenoidStatus, String firebaseKey) {
        this.name = name;
        this.soilHumidity = soilHumidity;
        this.phValue = phValue;
        this.solenoidStatus = solenoidStatus;
        this.firebaseKey = firebaseKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoilHumidity() {
        return soilHumidity;
    }

    public void setSoilHumidity(String soilHumidity) {
        this.soilHumidity = soilHumidity;
    }

    public long getPhValue() {
        return phValue;
    }

    public void setPhValue(long phValue) {
        this.phValue = phValue;
    }

    public String getSolenoidStatus() {
        return solenoidStatus;
    }

    public void setSolenoidStatus(String solenoidStatus) {
        this.solenoidStatus = solenoidStatus;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
}

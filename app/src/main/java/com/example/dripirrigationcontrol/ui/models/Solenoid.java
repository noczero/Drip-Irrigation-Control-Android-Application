package com.example.dripirrigationcontrol.ui.models;

public class Solenoid {
    private String name;
    private String firebaseKey;
    private String status; // ["WATERING","TREATMENT","OFF]
    private String index;

    public Solenoid(String name, String firebaseKey, String index) {
        this.name = name;
        this.firebaseKey = firebaseKey;
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getfirebaseKey() {
        return this.firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return this.status ;
    }

    public void setIndex(String index){
        this.status = index;
    }

    public String getIndex(){
        return this.index ;
    }


}

package com.example.yuusha.ecocharge.Model;

public class Serial {

    public int id;
    public String serial;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public String toString() {
        return this.getSerial();
    }
}

package com.oocl.web.sampleWebApp.domain;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
public class ParkingLot {
    @Id
    private Long Id;

    @Column(name = "parkingLot_id", unique = true, nullable = false)
    private String parkingLotId;

    @Range(min=1, max=100)
    private int capacity;

    public ParkingLot() {
    }

    public Long getId() {
        return Id;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

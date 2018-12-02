package com.oocl.web.sampleWebApp.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String parkingLotId;

    @Max(100)
    @Min(100)
    private int capacity;

    public ParkingLot() {
    }

    public ParkingLot(String parkingLotId, @Max(100) @Min(100) int capacity) {
        this.parkingLotId = parkingLotId;
        this.capacity = capacity;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

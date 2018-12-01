package com.oocl.web.sampleWebApp.domain;

import org.hibernate.validator.constraints.Range;
import javax.persistence.*;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String parkingLotId;

    @Range(min=1, max=100)
    private int capacity;

    public ParkingLot() {
    }

    public ParkingLot(String parkingLotId, @Range(min = 1, max = 100) int capacity) {
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

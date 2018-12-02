package com.oocl.web.sampleWebApp.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "parking_lot_id", unique = true, nullable = false)
    private String parkingLotId;

    private String parkingBoyId;

    @Max(100)
    @Min(1)
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

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getParkingBoyId() {
        return parkingBoyId;
    }

    public void setParkingBoyId(String parkingBoyId) {
        this.parkingBoyId = parkingBoyId;
    }
}

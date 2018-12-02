package com.oocl.web.sampleWebApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingLot;

import java.util.List;
import java.util.Objects;

public class ParkingBoyDetailResponse {
    private String employeeId;
    private List<ParkingLotResponse> parkingLots;

    public String getEmployeeId() {
        return employeeId;
    }

    public List<ParkingLotResponse> getParkingLots() {
        return parkingLots;
    }

    public void setParkingLots(List<ParkingLotResponse> parkingLots) {
        this.parkingLots = parkingLots;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }


    public static ParkingBoyDetailResponse create(String employeeId, List<ParkingLotResponse> parkingLots) {
        Objects.requireNonNull(employeeId);

        final ParkingBoyDetailResponse response = new ParkingBoyDetailResponse();
        response.setEmployeeId(employeeId);
        response.setParkingLots(parkingLots);
        return response;
    }

    @JsonIgnore
    public boolean isValid() {
        return employeeId != null;
    }
}

package com.oocl.web.sampleWebApp.controllers;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/parkinglots")
public class ParkingLotResource {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @GetMapping
    public ResponseEntity<ParkingLotResponse[]> getAll() {
        final ParkingLotResponse[] parkingLots = parkingLotRepository.findAll().stream()
            .map(parkingLot -> ParkingLotResponse.create(parkingLot.getParkingLotId(), parkingLot.getCapacity()))
            .toArray(ParkingLotResponse[]::new);
        return ResponseEntity.ok(parkingLots);
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody ParkingLot parkingLot) {
        if(parkingLotRepository.save(parkingLot)!=null) {
            return ResponseEntity.created(URI.create("/parkinglots/" + parkingLot.getParkingLotId())).build();
        }
        return ResponseEntity.badRequest().build();
    }
}

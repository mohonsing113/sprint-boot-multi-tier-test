package com.oocl.web.sampleWebApp.controllers;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyDetailResponse;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parkingboys")
public class ParkingBoyResource {

    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private EntityManager entityManager;

    @GetMapping
    public ResponseEntity<ParkingBoyResponse[]> getAll() {
        final ParkingBoyResponse[] parkingBoys = parkingBoyRepository.findAll().stream()
                .map(ParkingBoyResponse::create)
                .toArray(ParkingBoyResponse[]::new);
        return ResponseEntity.ok(parkingBoys);
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<String> add(@RequestBody ParkingBoy parkingBoy) {
        if (parkingBoyRepository.save(parkingBoy) != null) {
            return ResponseEntity.created(URI.create("/parkingboys/" + parkingBoy.getEmployeeId())).build();
        }
        return ResponseEntity.badRequest().build();

    }

    @GetMapping(value = "/{employeeId}")
    public ResponseEntity<ParkingBoyDetailResponse> geByEmployeeId(@PathVariable String employeeId) {
        ParkingBoy parkingBoy = parkingBoyRepository.findFirstByEmployeeId(employeeId);

        if (parkingBoy == null) {
            return ResponseEntity.badRequest().build();
        }

        List<ParkingLotResponse> parkingLotResponses =
                parkingLotRepository.findByParkingBoyId(parkingBoy.getEmployeeId()).stream()
                        .map(ParkingLotResponse::create).collect(Collectors.toList());

        ParkingBoyDetailResponse parkingBoyDetailResponse = ParkingBoyDetailResponse.create(parkingBoy.getEmployeeId(), parkingLotResponses);
        return ResponseEntity.ok(parkingBoyDetailResponse);
    }

    @PutMapping(value = "/{employeeId}/parkinglots")
    public ResponseEntity<String> addParkingLot(@PathVariable String employeeId, @RequestBody String[] parkingLotIds) {
        ParkingBoy parkingBoy = parkingBoyRepository.findFirstByEmployeeId(employeeId);

        if (parkingBoy == null) {
            return ResponseEntity.badRequest().body("parking boy not found");
        }

        List<ParkingLot> parkingLots = Arrays.stream(parkingLotIds)
                .map(id -> parkingLotRepository.findFirstByParkingLotId(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (parkingLotIds.length != parkingLots.size()) {
            return ResponseEntity.badRequest().body("some parking lot id(s) not found");
        }

        parkingLots.stream().forEach(parkingLot -> {
            parkingLot.setParkingBoyId(parkingBoy.getEmployeeId());
            parkingLotRepository.save(parkingLot);
        });

        return ResponseEntity.ok("add associates success");
    }

}

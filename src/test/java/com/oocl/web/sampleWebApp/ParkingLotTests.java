package com.oocl.web.sampleWebApp;

import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingLotResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParkingLotTests {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void should_get_parking_lots() throws Exception {
        // Given
        String newParkingLotId = "lot";
        int newParkingLotCapacity =50;
        final ParkingLot Lot = parkingLotRepository.save(new ParkingLot(newParkingLotId, newParkingLotCapacity));

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/parkinglots"))
            .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingLotResponse[] parkingLots = getContentAsObject(result, ParkingLotResponse[].class);

        assertEquals(1, parkingLots.length);
        assertEquals(newParkingLotId, parkingLots[0].getParkingLotId());
        assertEquals(newParkingLotCapacity, parkingLots[0].getCapacity());
    }

    @Test
    public void should_add_new_parking_lot() throws Exception {
        //given
        String newParkingLotId = "lot";
        int newParkingLotCapacity = 50;
        String newParkingLotInJson = "{\"parkingLotId\":\"" + newParkingLotId + "\", \"capacity\":"+newParkingLotCapacity+"}";

        //when
        mvc.perform(post("/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newParkingLotInJson)
        )//then
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/parkinglots/"+newParkingLotId)));
    }

    @Test
    public void should_throw_exception_due_to_exceed_employeeid_length() throws Exception {
        //given
        String newParkingLotId = "lot";
        int newParkingLotCapacityExceedTheMaxLimit = 1000;
        String newParkingLotInJson = "{\"parkingLotId\":\"" + newParkingLotId + "\", \"capacity\":"+newParkingLotCapacityExceedTheMaxLimit+"}";

        //when
        mvc.perform(post("/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newParkingLotInJson)
        )//then
                .andExpect(status().isBadRequest());
    }
}

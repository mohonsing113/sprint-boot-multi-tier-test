package com.oocl.web.sampleWebApp;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
import com.oocl.web.sampleWebApp.domain.ParkingLot;
import com.oocl.web.sampleWebApp.domain.ParkingLotRepository;
import com.oocl.web.sampleWebApp.models.ParkingBoyDetailResponse;
import com.oocl.web.sampleWebApp.models.ParkingBoyResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.oocl.web.sampleWebApp.WebTestUtil.getContentAsObject;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ParkingBoyTests {
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void should_get_parking_boys() throws Exception {
        // Given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));

        // When
        final MvcResult result = mvc.perform(get("/parkingboys"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyResponse[] parkingBoys = getContentAsObject(result, ParkingBoyResponse[].class);

        assertEquals(1, parkingBoys.length);
        assertEquals("boy", parkingBoys[0].getEmployeeId());
    }

    @Test
    public void should_add_new_parking_boy() throws Exception {
        //given
        String newParkingBoyEmployeeId = "1";
        String newParkingBoyInJson = "{\"employeeId\":" + newParkingBoyEmployeeId + "}";

        //when
        mvc.perform(post("/parkingboys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newParkingBoyInJson)
        )//then
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/parkingboys/" + newParkingBoyEmployeeId)));
    }

    @Test
    public void should_throw_exception_due_to_exceed_employeeid_length() throws Exception {
        //given
        String newParkingBoyEmployeeIdExceedMaxLength = "The employee id is a non-empty String representing the unique ID for a parking boy";
        String newParkingBoyInJson = "{\"employeeId\":" + newParkingBoyEmployeeIdExceedMaxLength + "}";

        //when
        mvc.perform(post("/parkingboys")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newParkingBoyInJson)
        )//then
                .andExpect(status().isBadRequest());
    }


    @Test
    public void should_find_parking_boy_by_employee_id_with_one_parking_lot() throws Exception {
        // Given

        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));
        parkingLotRepository.save(new ParkingLot("lot", 40, "boy"));


        // When
        final MvcResult result = mvc.perform(get("/parkingboys/boy"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyDetailResponse parkingBoy = getContentAsObject(result, ParkingBoyDetailResponse.class);

        assertEquals("boy", parkingBoy.getEmployeeId());
        assertEquals("lot", parkingBoy.getParkingLots().get(0).getParkingLotId());
    }

    @Test
    public void should_find_parking_boy_by_employee_id_with_multiple_parking_lot() throws Exception {
        // Given

        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));
        parkingLotRepository.save(new ParkingLot("first_lot", 40, "boy"));
        parkingLotRepository.save(new ParkingLot("second_lot", 60, "boy"));


        // When
        final MvcResult result = mvc.perform(get("/parkingboys/boy"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyDetailResponse parkingBoy = getContentAsObject(result, ParkingBoyDetailResponse.class);

        assertEquals("boy", parkingBoy.getEmployeeId());
        assertEquals("first_lot", parkingBoy.getParkingLots().get(0).getParkingLotId());
        assertEquals("second_lot", parkingBoy.getParkingLots().get(1).getParkingLotId());
    }

    @Test
    public void should_not_find_parking_boy() throws Exception {
        // When // Then
        mvc.perform(get("/parkingboys/boythatsnevercreated"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_parking_boy_associate_to_one_parking_lot() throws Exception {
        //given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));
        final ParkingLot lot = parkingLotRepository.save(new ParkingLot("lot", 40));

        //when
        mvc.perform(put("/parkingboys/" + boy.getEmployeeId() + "/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[\"" + lot.getParkingLotId() + "\"]")
        ).andExpect(status().isOk());
        final MvcResult result = mvc.perform(get("/parkingboys/boy"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyDetailResponse parkingBoy = getContentAsObject(result, ParkingBoyDetailResponse.class);

        assertEquals("boy", parkingBoy.getEmployeeId());
        assertEquals("lot", parkingBoy.getParkingLots().get(0).getParkingLotId());
    }

    @Test
    public void should_update_parking_boy_associate_to_two_parking_lot() throws Exception {
        //given
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));
        final ParkingLot first_lot = parkingLotRepository.save(new ParkingLot("first_lot", 40));
        final ParkingLot second_lot = parkingLotRepository.save(new ParkingLot("second_lot", 60));

        // When
        mvc.perform(put("/parkingboys/" + boy.getEmployeeId() + "/parkinglots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[\"" + first_lot.getParkingLotId() + "\",\"" + second_lot.getParkingLotId() + "\"]")
        ).andExpect(status().isOk());

        final MvcResult result = mvc.perform(get("/parkingboys/boy"))
                .andReturn();

        // Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyDetailResponse parkingBoy = getContentAsObject(result, ParkingBoyDetailResponse.class);

        assertEquals("boy", parkingBoy.getEmployeeId());
        assertEquals("first_lot", parkingBoy.getParkingLots().get(0).getParkingLotId());
        assertEquals("second_lot", parkingBoy.getParkingLots().get(1).getParkingLotId());
    }


}

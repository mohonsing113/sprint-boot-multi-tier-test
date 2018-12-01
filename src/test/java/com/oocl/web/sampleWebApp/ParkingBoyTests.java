package com.oocl.web.sampleWebApp;

import com.oocl.web.sampleWebApp.domain.ParkingBoy;
import com.oocl.web.sampleWebApp.domain.ParkingBoyRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void should_get_parking_boys() throws Exception {
        // Given
        entityManager.clear();
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy"));
        parkingBoyRepository.flush();

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
            .get("/parkingboys"))
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
            .andExpect(header().string("Location", containsString("/parkingboys/"+newParkingBoyEmployeeId)));
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
}

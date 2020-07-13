package com.java.app.requests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestsContollerTest {
	
    private RestTemplate restTemplate;
    private WireMockServer wireMockServer;
    
    @Autowired
    private RequestsController requestsController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(requestsController).build();
    }

    @Test
    public void blacklist() throws Exception{
        requestsController = mock(RequestsController.class);

         this.mockMvc.perform(get("/ipBlacklist")
                 .accept(MediaType.parseMediaType("application/json")))
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void requests() throws Exception{
        requestsController = mock(RequestsController.class);

        this.mockMvc.perform(get("/requests")
                .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void statisticPerCustomer() throws Exception {
        mockMvc.perform(get("/statisticCustomer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void statisticPerDay() throws Exception {
        mockMvc.perform(get("/statisticDay/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void createOrder(){
    	  try {
			mockMvc.perform(post("/requests")
			           .contentType(MediaType.APPLICATION_JSON)
			           .content("{ \"customerID\": 1,"
			           		+ " \"tagID\": 2,"
			           		+ " \"userID\": \"TEST\","
                            + " \"remoteIP\": \"123.234.56.78\" "
                            + " \"timestamp\": 1594573581150}")
			           .accept(MediaType.APPLICATION_JSON))
			           .andReturn();
		} catch (Exception e) {
			System.out.println(e);
		}

    }
}

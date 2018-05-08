package com.duplex.presentation.demo.controller;

import com.duplex.presentation.demo.dto.CSVDataDTO;
import com.duplex.presentation.demo.kafka.Receiver;
import com.duplex.presentation.demo.kafka.Sender;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
public class DataPresentationControllerTest {

  private static String BOOT_TOPIC = "DataForPresenter";
  @ClassRule public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, BOOT_TOPIC);
  @Resource private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private RestTemplate restTemplate;
  private ObjectMapper objectMapper = new ObjectMapper();

  @Autowired private Sender sender;
  @Autowired private Receiver receiver;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
    restTemplate = new RestTemplate();
    IntegrationTestUtil.setTimeout(restTemplate, 5000);
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    String dataToSend =
        "[{\"name\":\"Joe\",\"age\":22,\"height\":\"5.1\"},{\"name\":\"Alice\",\"age\":32,\"height\":\"5.7\"},{\"name\":\"Madden\",\"age\":55,\"height\":\"5.9\"},{\"name\":\"Alfred\",\"age\":21,\"height\":\"6.2\"}]";
    sender.send(BOOT_TOPIC, dataToSend);

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    Assert.assertEquals(0, receiver.getLatch().getCount());
  }

  @After
  public void tearDown() throws Exception {
    restTemplate = null;
    this.mockMvc = null;
  }

  @Test
  public void testGetPersonByName() throws Exception {

    String query = DataPresentationController.personURI + "/" + "Joe";
    MvcResult result = mockMvc.perform(get(query)).andExpect(status().isOk()).andReturn();

    CSVDataDTO dto =
        objectMapper.readValue(result.getResponse().getContentAsByteArray(), CSVDataDTO.class);
    Assert.assertNotNull(dto);
    Assert.assertEquals("Joe", dto.getName());
  }
}

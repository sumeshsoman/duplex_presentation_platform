package com.duplex.presentation.demo.kafka;

import com.duplex.presentation.demo.model.Person;
import com.duplex.presentation.demo.repo.PersonRepository;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
public class KafkaTest {

  private static String BOOT_TOPIC = "DataForPresenter";

  @Autowired private Sender sender;

  @Autowired private Receiver receiver;

  @Autowired private PersonRepository repository;

  @ClassRule public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, BOOT_TOPIC);

  @Test
  public void testReceive() throws Exception {
    String dataToSend =
        "[{\"name\":\"Joe\",\"age\":12,\"height\":\"5.1\"},{\"name\":\"Alice\",\"age\":32,\"height\":\"5.7\"},{\"name\":\"Madden\",\"age\":55,\"height\":\"5.9\"},{\"name\":\"Alfred\",\"age\":21,\"height\":\"6.2\"}]";
    sender.send(BOOT_TOPIC, dataToSend);

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    Assert.assertEquals(0, receiver.getLatch().getCount());

    Iterable<Person> personIterable  = repository.findAll();
    personIterable.forEach(person -> Assert.assertNotNull(person));

    Person person = repository.findByName("Joe").get(0);
    Assert.assertNotNull(person);
    Assert.assertEquals("Joe", person.getName());

  }
}

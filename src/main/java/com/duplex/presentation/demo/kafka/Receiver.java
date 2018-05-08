package com.duplex.presentation.demo.kafka;

import com.duplex.presentation.demo.dto.CSVDataDTO;
import com.duplex.presentation.demo.model.Person;
import com.duplex.presentation.demo.repo.PersonRepository;
import com.duplex.presentation.demo.util.HelperUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

  @Autowired private PersonRepository repository;

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  @KafkaListener(topics = "${kafka.topic.name}")
  public void receive(ConsumerRecord<?, ?> consumerRecord) {
    LOGGER.info("received payload='{}'", consumerRecord.toString());

    List<CSVDataDTO> csvDataDTOList = HelperUtil.generateCSVData(String.valueOf(consumerRecord.value()));
    csvDataDTOList.stream().map(dto -> new Person(dto)).forEach(person -> repository.save(person));
    latch.countDown();
  }
}

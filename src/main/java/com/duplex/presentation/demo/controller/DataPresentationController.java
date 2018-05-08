package com.duplex.presentation.demo.controller;

import com.duplex.presentation.demo.dto.CSVDataDTO;
import com.duplex.presentation.demo.model.Person;
import com.duplex.presentation.demo.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataPresentationController {

  public static final String personURI = "/duplex/v1/people";

  @Autowired private PersonRepository repository;

  @RequestMapping(
    value = personURI + "/{name}",
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET
  )
  public ResponseEntity<CSVDataDTO> getPersonByName(@PathVariable("name") String name) {
    HttpStatus status = HttpStatus.OK;
    CSVDataDTO dataDTO;
    List<Person> personList = repository.findByName(name);

    if (null != personList && personList.size() > 0) {
      dataDTO = new CSVDataDTO(personList.get(0));
    } else {
      dataDTO = new CSVDataDTO();
      status = HttpStatus.NOT_FOUND;
    }

    return new ResponseEntity<>(dataDTO, status);
  }
}

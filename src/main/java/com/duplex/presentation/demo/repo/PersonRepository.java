package com.duplex.presentation.demo.repo;

import com.duplex.presentation.demo.model.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

  List<Person> findByName(@Param("name") String name);
}

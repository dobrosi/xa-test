package com.github.dobrosi.test.xa.repository;

import com.github.dobrosi.test.xa.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}

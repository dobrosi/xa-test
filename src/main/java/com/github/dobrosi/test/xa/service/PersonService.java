package com.github.dobrosi.test.xa.service;

import com.github.dobrosi.test.xa.model.Person;
import com.github.dobrosi.test.xa.repository.PersonRepository;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final JmsTemplate jmsTemplate;

    public PersonService(PersonRepository personRepository, JmsTemplate jmsTemplate) {
        this.personRepository = personRepository;
        this.jmsTemplate = jmsTemplate;
    }

    public Person create(String name) {
        jmsTemplate.convertAndSend("person", name);
        return personRepository.save(
                Person.builder()
                        .name(name)
                        .build());
    }
}

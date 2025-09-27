package com.github.dobrosi.test.xa;

import com.github.dobrosi.test.xa.service.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.activemq.ArtemisContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.lang.System.setProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class ApplicationTest {
	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres")
			.withCommand("postgres", "-c", "max_prepared_transactions=100");

	@Container
	static ArtemisContainer artemis = new ArtemisContainer("apache/activemq-artemis");

	@BeforeAll
	static void init() {
		setProperty("db.url", postgres.getJdbcUrl());
		setProperty("db.username", postgres.getUsername());
		setProperty("db.password", postgres.getPassword());

		setProperty("jms.broker-url", artemis.getBrokerUrl());
		setProperty("jms.username", artemis.getUser());
		setProperty("jms.password", artemis.getPassword());
	}

	@Autowired
	PersonService personService;

	@Test
	void startContext() {}

	@Test
	void create() {
		var name = "Test Elek";
		assertEquals(name, personService.create(name).getName());
	}
}

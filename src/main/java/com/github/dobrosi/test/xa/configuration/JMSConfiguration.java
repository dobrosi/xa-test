package com.github.dobrosi.test.xa.configuration;

import com.atomikos.jms.AtomikosConnectionFactoryBean;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import org.apache.activemq.artemis.jms.client.ActiveMQXAConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class JMSConfiguration {
    @Value("${jms.broker-url}")
    private String brokerUrl;

    @Value("${jms.username}")
    private String username;

    @Value("${jms.password}")
    private String password;

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

    @Bean
    public ConnectionFactory connectionFactory() throws JMSException {
        var activeMQXAConnectionFactory = new
                ActiveMQXAConnectionFactory();
        activeMQXAConnectionFactory.setBrokerURL(brokerUrl);
        activeMQXAConnectionFactory.setUser(username);
        activeMQXAConnectionFactory.setPassword(password);

        var atomikosConnectionFactoryBean = new AtomikosConnectionFactoryBean();
        atomikosConnectionFactoryBean.setUniqueResourceName("xamq");
        atomikosConnectionFactoryBean.setLocalTransactionMode(false);
        atomikosConnectionFactoryBean.setXaConnectionFactory(activeMQXAConnectionFactory);

        return atomikosConnectionFactoryBean;
    }
}

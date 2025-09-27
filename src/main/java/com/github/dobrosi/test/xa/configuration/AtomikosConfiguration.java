package com.github.dobrosi.test.xa.configuration;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@EnableTransactionManagement
public class AtomikosConfiguration {
    @Bean
    public UserTransaction userTransaction() {
        return new UserTransactionImp();
    }

    @Bean
    public TransactionManager atomikosTransactionManager() {
        return new UserTransactionManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            UserTransaction userTransaction,
            TransactionManager atomikosTransactionManager) {
        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }
}

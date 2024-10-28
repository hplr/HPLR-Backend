package org.hplr.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@ComponentScan(basePackages = {
        "org.hplr.*.infrastructure.controller",
        "org.hplr.*.infrastructure.dbadapter",
        "org.hplr.*.core.usecases.service",
        "org.hplr.*.config"
})
@EnableJpaRepositories("org.hplr.*.infrastructure.dbadapter")
@EntityScan("org.hplr.*.infrastructure.dbadapter")
@EnableScheduling
public class RankingApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(RankingApplication.class);
        application.run(args);
    }
}
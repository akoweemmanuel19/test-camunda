package bj.ent.test_camunda.seed;

import bj.ent.test_camunda.service.SeederService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppSeeder {
    
    @Bean
    public CommandLineRunner initDatabase(
        SeederService seederService 
    ) {
        return args -> {
            // L'appel au service déclenche la transaction et la logique de seeding
            seederService.seedInitialUsers();
        };
    }
}
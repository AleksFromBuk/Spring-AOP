package com.springaopopenschool1.firsttask;

import com.springaopopenschool1.firsttask.model.Plant;
import com.springaopopenschool1.firsttask.repository.ExecutionLogRepository;
import com.springaopopenschool1.firsttask.service.PlantService;
import com.springaopopenschool1.firsttask.utils.ThreadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
@EnableJpaRepositories("com.springaopopenschool1.firsttask.repository")
public class SpringAOPApplication {
    private final PlantService plantService;
    private final ExecutionLogRepository executionLogRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringAOPApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        plantService.addPlant(new Plant("Роза", "Цветок"));
        ThreadUtils.waitTime(200);
        System.out.println(plantService.getPlantByType("Цветок"));
        System.out.println(plantService.getPlantByName("Роза"));
        ThreadUtils.waitTime(200);
        plantService.addPlants(List.of(new Plant("Кукуруза", "Не фрукт"),
                new Plant("Дуб", "Полезен")));
        plantService.getPlantByType("Годнота").forEach(System.out::println);
        //executionLogRepository.findAll().forEach(System.out::println);
    }

}

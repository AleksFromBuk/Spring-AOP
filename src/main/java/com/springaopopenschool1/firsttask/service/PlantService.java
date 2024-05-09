package com.springaopopenschool1.firsttask.service;

import com.springaopopenschool1.firsttask.annotation.TrackAsyncTime;
import com.springaopopenschool1.firsttask.annotation.TrackTime;
import com.springaopopenschool1.firsttask.model.Plant;
import com.springaopopenschool1.firsttask.exception.PlantException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlantService {
    private final Map<String, Plant> plants = new HashMap<>();

    @TrackAsyncTime
    public void addPlant(Plant plant) {
        plants.put(plant.getName(), plant);
    }

    @TrackAsyncTime
    public void addPlants(List<Plant> newPlants) {
        if (newPlants.size() <= 1) {
            throw new PlantException("container.size <= 1, please use 'addPlant' method" );
        }
        plants.putAll(newPlants
                .stream()
                .collect(Collectors.toMap(Plant::getName, Function.identity()))
        );
    }

    @TrackTime
    public Plant getPlantByName(String name) {
        return plants.get(name);
    }

    @TrackTime
    public List<Plant> getPlantByType(String type) {
        return plants.values()
                .stream()
                .filter(plant -> plant.getType().equals(type))
                .toList();
    }
}

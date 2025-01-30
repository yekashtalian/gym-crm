package org.example.gymcrm.service;

import org.example.gymcrm.entity.Training;

import java.util.List;

public interface TrainingService {
    void save(Training training);
    List<Training> getAll();
}

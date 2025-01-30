package org.example.gymcrm.service;

import org.example.gymcrm.entity.Trainer;

import java.util.List;

public interface TrainerService {
    void save(Trainer trainer);
    void update(String id, Trainer trainer);
    List<Trainer> getAll();
    List<String> getUsernames();
}

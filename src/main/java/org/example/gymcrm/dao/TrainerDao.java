package org.example.gymcrm.dao;

import org.example.gymcrm.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    void save(Trainer trainee);
    void update(String id, Trainer trainer);
    List<Trainer> findAll();
    Optional<Trainer> findById(String id);
    List<String> getUsernames();
}

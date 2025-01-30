package org.example.gymcrm.service;

import org.example.gymcrm.entity.Trainee;

import java.util.List;

public interface TraineeService {
    void save(Trainee trainee);
    void update(String id, Trainee trainee);
    void delete(String id);
    List<Trainee> getAll();
    List<String> getUsernames();
}

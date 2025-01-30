package org.example.gymcrm.dao;
import org.example.gymcrm.entity.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    void save(Training trainee);
    List<Training> findAll();
    Optional<Training> findById(String id);
}

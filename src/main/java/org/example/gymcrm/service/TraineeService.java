package org.example.gymcrm.service;

import java.util.List;
import org.example.gymcrm.entity.Trainee;

public interface TraineeService {
  void save(Trainee trainee);

  void update(String id, Trainee trainee);

  void delete(String id);

  List<Trainee> getAll();

  List<String> getUsernames();
}

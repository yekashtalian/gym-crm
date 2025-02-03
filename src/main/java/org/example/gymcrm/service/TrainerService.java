package org.example.gymcrm.service;

import java.util.List;
import org.example.gymcrm.entity.Trainer;

public interface TrainerService {
  void save(Trainer trainer);

  void update(String id, Trainer trainer);

  List<Trainer> getAll();

  List<String> getUsernames();
}

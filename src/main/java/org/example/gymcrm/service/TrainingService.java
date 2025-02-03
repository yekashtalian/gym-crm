package org.example.gymcrm.service;

import java.util.List;
import org.example.gymcrm.entity.Training;

public interface TrainingService {
  void save(Training training);

  List<Training> getAll();
}

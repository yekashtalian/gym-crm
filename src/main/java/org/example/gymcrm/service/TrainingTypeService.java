package org.example.gymcrm.service;

import org.example.gymcrm.dto.TrainingTypeDto;

import java.util.List;

public interface TrainingTypeService {
  List<TrainingTypeDto> findAll();
}

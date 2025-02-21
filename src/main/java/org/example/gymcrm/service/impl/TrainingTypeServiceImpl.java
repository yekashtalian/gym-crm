package org.example.gymcrm.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.mapper.TrainingTypeMapper;
import org.example.gymcrm.service.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
  private final TrainingTypeDao trainingTypeDao;
  private final TrainingTypeMapper trainingTypeMapper;

  @Override
  public List<TrainingTypeDto> findAll() {
    var trainingTypesDto =
        trainingTypeDao.findAll().stream().map(trainingTypeMapper::toDtoWithId).toList();
    return trainingTypesDto;
  }
}

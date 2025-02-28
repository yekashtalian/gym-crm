package org.example.gymcrm.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymcrm.dao.TrainingTypeDao;
import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.mapper.TrainingTypeMapper;
import org.example.gymcrm.service.TrainingTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
  private final TrainingTypeDao trainingTypeDao;
  private final TrainingTypeMapper trainingTypeMapper;

  @Override
  @Transactional(readOnly = true)
  public List<TrainingTypeDto> findAll() {
    log.info("Fetching all training types from the database");

    var trainingTypes = trainingTypeDao.findAll();
    log.info("Found {} training types", trainingTypes.size());

    var trainingTypesDto = trainingTypes.stream().map(trainingTypeMapper::toDtoWithId).toList();
    return trainingTypesDto;
  }
}

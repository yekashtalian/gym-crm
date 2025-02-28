package org.example.gymcrm.service;

import java.util.List;

import org.example.gymcrm.dto.*;
import org.example.gymcrm.entity.Trainee;

public interface TraineeService {
  RegisterTraineeResponseDto save(RegisterTraineeRequestDto trainee);

  TraineeProfileDto update(String username, UpdateTraineeRequestDto trainee);

  void deleteByUsername(String username);

  TraineeProfileDto findByUsername(String username);

  void changeStatus(String username);

  List<TraineeTrainersDto> updateTraineeTrainers(
      String username, UpdateTrainersDto updateTrainersDto);
}

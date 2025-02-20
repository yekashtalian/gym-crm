package org.example.gymcrm.service;

import java.util.List;

import org.example.gymcrm.dto.RegisterTraineeRequestDto;
import org.example.gymcrm.dto.RegisterTraineeResponseDto;
import org.example.gymcrm.dto.TraineeProfileDto;
import org.example.gymcrm.dto.UpdateTraineeRequestDto;
import org.example.gymcrm.entity.Trainee;

public interface TraineeService {
  RegisterTraineeResponseDto save(RegisterTraineeRequestDto trainee);

  TraineeProfileDto update(String username, UpdateTraineeRequestDto trainee);

  void deleteByUsername(String username);

  List<TraineeProfileDto> findAll();

  TraineeProfileDto findByUsername(String username);

  void changeStatus(String username);


  void addTrainerToList(String traineeUsername, String trainerUsername);

  void removeTrainerFromList(String traineeUsername, String trainerUsername);
}

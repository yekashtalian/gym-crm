package org.example.gymcrm.service;

import java.util.List;

import org.example.gymcrm.dto.RegisterTraineeRequestDto;
import org.example.gymcrm.dto.RegisterTraineeResponseDto;
import org.example.gymcrm.dto.TraineeProfileDto;
import org.example.gymcrm.entity.Trainee;

public interface TraineeService {
  RegisterTraineeResponseDto save(RegisterTraineeRequestDto trainee);

  void update(Long id, Trainee trainee);

  void deleteByUsername(String username);

  void changePassword(String oldPassword, String newPassword, String username);

  List<TraineeProfileDto> findAll();

  TraineeProfileDto findByUsername(String username);

  void changeStatus(Long id);

  boolean authenticate(String username, String password);

  void addTrainerToList(String traineeUsername, String trainerUsername);

  void removeTrainerFromList(String traineeUsername, String trainerUsername);
}

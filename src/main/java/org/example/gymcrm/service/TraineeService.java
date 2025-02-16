package org.example.gymcrm.service;

import java.util.List;

import org.example.gymcrm.dto.TraineeProfileDTO;
import org.example.gymcrm.entity.Trainee;

public interface TraineeService {
  void save(Trainee trainee);

  void update(Long id, Trainee trainee);

  void deleteByUsername(String username);

  void changePassword(String oldPassword, String newPassword, String username);

  List<TraineeProfileDTO> findAll();

  TraineeProfileDTO findByUsername(String username);
  void changeStatus(Long id);
  boolean authenticate(String username, String password);
  void addTrainerToList(String traineeUsername, String trainerUsername);
  void removeTrainerFromList(String traineeUsername, String trainerUsername);
}

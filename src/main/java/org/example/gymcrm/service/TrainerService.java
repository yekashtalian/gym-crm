package org.example.gymcrm.service;

import java.util.List;

import org.example.gymcrm.dto.RegisterTrainerRequestDto;
import org.example.gymcrm.dto.RegisterTrainerResponseDto;
import org.example.gymcrm.dto.TrainerProfileDto;
import org.example.gymcrm.entity.Trainer;

public interface TrainerService {
  RegisterTrainerResponseDto save(RegisterTrainerRequestDto trainer);

  void update(Long id, Trainer trainer);

  List<TrainerProfileDto> getAll();

  TrainerProfileDto findByUsername(String username);

  void changePassword(String oldPassword, String newPassword, String username);

  void changeStatus(Long id);

  List<Trainer> getUnassignedTrainers(String username);

  boolean authenticate(String username, String password);
}

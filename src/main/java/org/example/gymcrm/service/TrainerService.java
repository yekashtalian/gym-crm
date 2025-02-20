package org.example.gymcrm.service;

import java.util.List;

import org.example.gymcrm.dto.RegisterTrainerRequestDto;
import org.example.gymcrm.dto.RegisterTrainerResponseDto;
import org.example.gymcrm.dto.TrainerProfileDto;
import org.example.gymcrm.dto.UpdateTrainerRequestDto;
import org.example.gymcrm.entity.Trainer;

public interface TrainerService {
  RegisterTrainerResponseDto save(RegisterTrainerRequestDto trainer);

  TrainerProfileDto update(String username, UpdateTrainerRequestDto trainer);

  List<TrainerProfileDto> getAll();

  TrainerProfileDto findByUsername(String username);

  void changeStatus(String username);

  List<TrainerProfileDto> getUnassignedTrainers(String username);
}

package org.example.gymcrm.service;

import java.util.List;

import org.example.gymcrm.dto.*;
import org.example.gymcrm.entity.Trainer;

public interface TrainerService {
  RegisterTrainerResponseDto save(RegisterTrainerRequestDto trainer);

  TrainerProfileDto update(String username, UpdateTrainerRequestDto trainer);

  TrainerProfileDto findByUsername(String username);

  void changeStatus(String username);

  List<TrainerProfileDto> getUnassignedTrainers(String username);
}

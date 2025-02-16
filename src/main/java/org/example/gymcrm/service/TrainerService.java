package org.example.gymcrm.service;

import java.util.List;

import org.example.gymcrm.dto.TrainerProfileDTO;
import org.example.gymcrm.entity.Trainer;

public interface TrainerService {
  void save(Trainer trainer);

  void update(Long id, Trainer trainer);

  List<TrainerProfileDTO> getAll();

  TrainerProfileDTO findByUsername(String username);

  void changePassword(String oldPassword, String newPassword, String username);

  void changeStatus(Long id);

  List<Trainer> getUnassignedTrainers(String username);

  boolean authenticate(String username, String password);
}

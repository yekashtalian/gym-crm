package org.example.gymcrm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dto.*;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.exception.NotFoundException;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.mapper.TraineeMapper;
import org.example.gymcrm.security.service.JwtService;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

  @Mock private TraineeDao traineeDao;
  @Mock private TrainerDao trainerDao;
  @Mock private TraineeMapper traineeMapper;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtService jwtService;

  @InjectMocks private TraineeServiceImpl traineeService;

  private Trainee trainee;
  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUsername("testuser");

    trainee = new Trainee();
    trainee.setUser(user);
  }

//  @Test
//  void save_ShouldRegisterTrainee() {
//    RegisterTraineeRequestDto requestDto = new RegisterTraineeRequestDto();
//    when(traineeMapper.registerDtoToUser(requestDto)).thenReturn(user);
//    when(traineeMapper.registerDtoToTrainee(requestDto)).thenReturn(trainee);
//    when(traineeMapper.traineeToDto(any(User.class))).thenReturn(new RegisterTraineeResponseDto());
//    when(traineeDao.save(any(Trainee.class))).thenReturn(trainee);
//    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//    when(jwtService.generateToken(any(User.class))).thenReturn("mockToken");
//
//    traineeService.save(requestDto);
//
//    verify(traineeDao).save(any(Trainee.class));
//  }

  @Test
  void update_ShouldUpdateTrainee() {
    UpdateTraineeRequestDto updateDto = new UpdateTraineeRequestDto();
    updateDto.setActive(false);
    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.of(trainee));
    when(traineeDao.update(any(Trainee.class))).thenReturn(trainee);
    when(traineeMapper.toProfileDto(any(Trainee.class))).thenReturn(new TraineeProfileDto());

    TraineeProfileDto profileDto = traineeService.update("testuser", updateDto);

    assertThat(profileDto).isNotNull();
    verify(traineeDao).update(any(Trainee.class));
  }

  @Test
  void deleteByUsername_ShouldDeleteTrainee() {
    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.of(trainee));

    traineeService.deleteByUsername("testuser");

    verify(traineeDao).deleteByUsername("testuser");
  }

  @Test
  void findByUsername_ShouldReturnTraineeProfile() {
    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.of(trainee));
    when(traineeMapper.toProfileDto(trainee)).thenReturn(new TraineeProfileDto());

    TraineeProfileDto profileDto = traineeService.findByUsername("testuser");

    assertThat(profileDto).isNotNull();
  }

  @Test
  void findByUsername_ShouldThrowExceptionIfNotFound() {
    when(traineeDao.findByUsername("invalid")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> traineeService.findByUsername("invalid"))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void updateTraineeTrainers_ShouldUpdateTraineeTrainersList() {
    UpdateTrainersDto updateTrainersDto = new UpdateTrainersDto(List.of("trainer1", "trainer2"));
    Trainer trainer1 = new Trainer();
    trainer1.setUser(new User());
    trainer1.getUser().setUsername("trainer1");

    Trainer trainer2 = new Trainer();
    trainer2.setUser(new User());
    trainer2.getUser().setUsername("trainer2");

    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.of(trainee));
    when(trainerDao.findByUsername("trainer1")).thenReturn(Optional.of(trainer1));
    when(trainerDao.findByUsername("trainer2")).thenReturn(Optional.of(trainer2));
    when(traineeMapper.toTraineeTrainersDto(any(Trainer.class)))
        .thenReturn(new TraineeTrainersDto());

    List<TraineeTrainersDto> result =
        traineeService.updateTraineeTrainers("testuser", updateTrainersDto);

    assertThat(result).hasSize(2);
    verify(traineeDao).update(trainee);
  }

  @Test
  void updateTraineeTrainers_ShouldThrowExceptionWhenTrainerNotFound() {
    UpdateTrainersDto updateTrainersDto = new UpdateTrainersDto(List.of("trainer1"));
    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.of(trainee));
    when(trainerDao.findByUsername("trainer1")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> traineeService.updateTraineeTrainers("testuser", updateTrainersDto))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void changeStatus_ShouldToggleTraineeStatus() {
    user.setActive(true);
    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.of(trainee));

    traineeService.changeStatus("testuser");

    assertThat(user.isActive()).isFalse();
  }

  @Test
  void changeStatus_ShouldThrowExceptionIfTraineeNotFound() {
    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> traineeService.changeStatus("testuser"))
        .isInstanceOf(NotFoundException.class);
  }
}

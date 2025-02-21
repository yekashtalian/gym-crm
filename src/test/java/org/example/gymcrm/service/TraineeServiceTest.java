package org.example.gymcrm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.example.gymcrm.dao.TraineeDao;
import org.example.gymcrm.dao.TrainerDao;
import org.example.gymcrm.dto.RegisterTraineeRequestDto;
import org.example.gymcrm.dto.RegisterTraineeResponseDto;
import org.example.gymcrm.dto.TraineeProfileDto;
import org.example.gymcrm.dto.UpdateTraineeRequestDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.User;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.mapper.TraineeMapper;
import org.example.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

  @Mock private TraineeDao traineeDao;
  @Mock private TrainerDao trainerDao;
  @Mock private TraineeMapper traineeMapper;

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

  @Test
  void save_ShouldRegisterTrainee() {
    RegisterTraineeRequestDto requestDto = new RegisterTraineeRequestDto();
    when(traineeMapper.registerDtoToUser(requestDto)).thenReturn(user);
    when(traineeMapper.registerDtoToTrainee(requestDto)).thenReturn(trainee);
    when(traineeMapper.traineeToDto(any(User.class))).thenReturn(new RegisterTraineeResponseDto());
    when(traineeDao.save(any(Trainee.class))).thenReturn(trainee);

    traineeService.save(requestDto);

    verify(traineeDao).save(any(Trainee.class));
  }

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
        .isInstanceOf(TraineeServiceException.class)
        .hasMessage("This trainee doesn't exist!");
  }

  @Test
  void addTrainerToList_ShouldAddTrainer() {
    Trainer trainer = new Trainer();
    trainer.setUser(new User());
    trainer.getUser().setUsername("trainer1");

    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.of(trainee));
    when(trainerDao.findByUsername("trainer1")).thenReturn(Optional.of(trainer));

    traineeService.addTrainerToList("testuser", "trainer1");

    assertThat(trainee.getTrainers()).contains(trainer);
  }

  @Test
  void removeTrainerFromList_ShouldRemoveTrainer() {
    Trainer trainer = new Trainer();
    trainer.setUser(new User());
    trainer.getUser().setUsername("trainer1");
    trainee.getTrainers().add(trainer);

    when(traineeDao.findByUsername("testuser")).thenReturn(Optional.of(trainee));
    when(trainerDao.findByUsername("trainer1")).thenReturn(Optional.of(trainer));

    traineeService.removeTrainerFromList("testuser", "trainer1");

    assertThat(trainee.getTrainers()).doesNotContain(trainer);
  }
}

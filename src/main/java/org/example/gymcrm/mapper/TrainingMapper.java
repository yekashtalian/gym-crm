package org.example.gymcrm.mapper;

import org.example.gymcrm.dto.TraineeTrainingDto;
import org.example.gymcrm.dto.TrainerTrainingDto;
import org.example.gymcrm.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
  @Mappings({
    @Mapping(source = "name", target = "name"),
    @Mapping(source = "date", target = "date"),
    @Mapping(source = "type", target = "trainingType"),
    @Mapping(source = "duration", target = "duration"),
    @Mapping(source = "trainer.user.firstName", target = "trainerName")
  })
  TraineeTrainingDto toTraineeTrainingsDto(Training training);

  @Mappings({
    @Mapping(source = "name", target = "name"),
    @Mapping(source = "date", target = "date"),
    @Mapping(source = "type", target = "trainingType"),
    @Mapping(source = "duration", target = "duration"),
    @Mapping(source = "trainee.user.firstName", target = "traineeName")
  })
  TrainerTrainingDto toTrainerTrainingsDto(Training training);
}

package org.example.gymcrm.mapper;

import org.example.gymcrm.dto.RegisterTrainerRequestDto;
import org.example.gymcrm.dto.RegisterTrainerResponseDto;
import org.example.gymcrm.dto.TrainerProfileDto;
import org.example.gymcrm.dto.TrainerTraineesDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
  RegisterTrainerResponseDto trainerToDto(User trainer);

  User registerDtoToUser(RegisterTrainerRequestDto registerTrainerRequestDto);

  @Mappings({
    @Mapping(source = "user.username", target = "username"),
    @Mapping(source = "user.firstName", target = "firstName"),
    @Mapping(source = "user.lastName", target = "lastName")
  })
  TrainerTraineesDto toTrainerTraineesDto(Trainee trainee);

  @Mappings({
    @Mapping(source = "user.firstName", target = "firstName"),
    @Mapping(source = "user.lastName", target = "lastName"),
    @Mapping(source = "specialization.id", target = "specialization"),
    @Mapping(source = "user.active", target = "active"),
  })
  TrainerProfileDto toProfileDto(Trainer trainer);

  @Mappings({
    @Mapping(source = "user.username", target = "username"),
    @Mapping(source = "user.firstName", target = "firstName"),
    @Mapping(source = "user.lastName", target = "lastName"),
    @Mapping(source = "specialization.id", target = "specialization"),
    @Mapping(source = "trainees", target = "trainees", ignore = true)
  })
  TrainerProfileDto toProfileDtoForUnassigned(Trainer trainer);
}

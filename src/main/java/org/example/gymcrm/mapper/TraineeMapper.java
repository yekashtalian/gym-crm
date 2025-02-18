package org.example.gymcrm.mapper;

import org.example.gymcrm.dto.RegisterTraineeRequestDto;
import org.example.gymcrm.dto.RegisterTraineeResponseDto;
import org.example.gymcrm.dto.TraineeProfileDto;
import org.example.gymcrm.dto.TraineeTrainersDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
  Trainee registerDtoToTrainee(RegisterTraineeRequestDto registerTraineeRequestDto);

  RegisterTraineeResponseDto traineeToDto(User trainee);

  User registerDtoToUser(RegisterTraineeRequestDto registerTraineeRequestDto);

  @Mappings({
    @Mapping(source = "user.username", target = "username"),
    @Mapping(source = "user.firstName", target = "firstName"),
    @Mapping(source = "user.lastName", target = "lastName"),
    @Mapping(source = "specialization.id", target = "specializationId")
  })
  TraineeTrainersDto toTraineeTrainersDto(Trainer trainer);

  @Mappings({
    @Mapping(source = "user.firstName", target = "firstName"),
    @Mapping(source = "user.lastName", target = "lastName"),
    @Mapping(source = "dateOfBirth", target = "dateOfBirth"),
    @Mapping(source = "address", target = "address"),
    @Mapping(source = "user.active", target = "active")
  })
  TraineeProfileDto toProfileDto(Trainee trainee);
}

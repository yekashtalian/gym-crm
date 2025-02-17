package org.example.gymcrm.mapper;

import org.example.gymcrm.dto.RegisterTraineeRequestDto;
import org.example.gymcrm.dto.RegisterTraineeResponseDto;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterTraineeMapper {
  Trainee registerDtoToTrainee(RegisterTraineeRequestDto registerTraineeRequestDto);

  RegisterTraineeResponseDto traineeToDto(User trainee);

  User registerDtoToUser(RegisterTraineeRequestDto registerTraineeRequestDto);
}

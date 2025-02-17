package org.example.gymcrm.mapper;

import org.example.gymcrm.dto.RegisterTrainerRequestDto;
import org.example.gymcrm.dto.RegisterTrainerResponseDto;
import org.example.gymcrm.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterTrainerMapper {
  RegisterTrainerResponseDto trainerToDto(User trainer);

  User registerDtoToUser(RegisterTrainerRequestDto registerTrainerRequestDto);
}

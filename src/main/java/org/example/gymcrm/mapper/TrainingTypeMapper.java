package org.example.gymcrm.mapper;

import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.entity.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
  @Mappings({
    @Mapping(source = "name", target = "name"),
    @Mapping(source = "id", target = "id", ignore = true)
  })
  TrainingTypeDto toDtoWithoutId(TrainingType trainingType);

  @Mappings({@Mapping(source = "name", target = "name"), @Mapping(source = "id", target = "id")})
  TrainingTypeDto toDtoWithId(TrainingType trainingType);
}

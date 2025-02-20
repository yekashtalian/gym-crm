package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeDto {
  private String name;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long id;
}

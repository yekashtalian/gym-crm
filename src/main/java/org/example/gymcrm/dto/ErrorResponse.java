package org.example.gymcrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDateTime localDateTime;

  private String errorMessage;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<ErrorDetail> errors;
}

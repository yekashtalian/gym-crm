package org.example.gymcrm.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraineeTrainersDto {
    private String username;
    private String firstName;
    private String lastName;
    private Long specializationId;
}

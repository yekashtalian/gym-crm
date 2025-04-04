package org.example.gymcrm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training_types")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Enumerated(EnumType.STRING)
  private Type name;

  public TrainingType(Type name) {
    this.name = name;
  }

  public enum Type {
    STRENGTH_TRAINING,
    CARDIO,
    YOGA,
    PILATES,
    HIIT,
    CROSSFIT,
    BOXING,
    DANCE,
    INDIVIDUAL,
    GROUP
  }

}

package org.example.gymcrm.entity;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "trainings")
@Getter
@Setter
@ToString(exclude = {"trainee", "trainer"})
@NoArgsConstructor
public class Training {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "trainee_id", nullable = false)
  private Trainee trainee;

  @ManyToOne(optional = false)
  @JoinColumn(name = "trainer_id", nullable = false)
  private Trainer trainer;

  @NotNull(message = "Training name cannot be null")
  @Size(min = 5, max = 20, message = "Training name size should be from 5 to 20 symbols")
  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "type_id")
  private TrainingType type;

  @NotNull(message = "Training date cannot be null")
  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date date;

  @NotNull(message = "Training duration cannot be null")
  @Positive(message = "Training duration cannot be negative number")
  @Min(value = 25, message = "Training duration should be longer than 25 minutes")
  @Max(value = 90, message = "Training duration should be shorter than 90 minutes")
  @Column(nullable = false)
  private Integer duration;
}

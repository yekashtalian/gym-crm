package org.example.gymcrm.entity;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
  @Size(min = 25, max = 90, message = "Training duration should be between 25 and 90 minutes")
  @Column(nullable = false)
  private Integer duration;
}

package org.example.gymcrm.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@ToString(exclude = {"user", "trainees"})
@NoArgsConstructor
public class Trainer {
  @Id private Long id;

  @ManyToOne()
  @JoinColumn(name = "specialization")
  private TrainingType specialization;

  @Setter(AccessLevel.PRIVATE)
  @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
  private List<Training> trainings = new ArrayList<>();

  @MapsId
  @OneToOne(optional = false)
  @JoinColumn(name = "trainer_id")
  private User user;

  @ManyToMany(mappedBy = "trainers")
  private List<Trainee> trainees = new ArrayList<>();

  public void addTraining(Training training) {
    training.setTrainer(this);
    trainings.add(training);
  }

  public Trainer(User user) {
    this.user = user;
  }
}

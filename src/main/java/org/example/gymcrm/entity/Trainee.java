package org.example.gymcrm.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "trainees")
@Getter
@Setter
@ToString(exclude = "user")
@NoArgsConstructor
public class Trainee {
  @Id private Long id;

  @Column
  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  @Column private String address;

  @Setter(AccessLevel.PRIVATE)
  @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
  private List<Training> trainings = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "trainees_trainers",
      joinColumns = @JoinColumn(name = "trainee_id"),
      inverseJoinColumns = @JoinColumn(name = "trainer_id"))
  private List<Trainer> trainers = new ArrayList<>();

  @MapsId
  @OneToOne(optional = false, cascade = CascadeType.ALL)
  @JoinColumn(name = "trainee_id")
  private User user;

  public void addTrainer(Trainer trainer) {
    trainers.add(trainer);
  }

  public void removeTrainer(Trainer trainer) {
    trainers.remove(trainer);
  }
}

package org.example.gymcrm.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
  @OnDelete(action = OnDeleteAction.CASCADE)
  @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL)
  private List<Training> trainings = new ArrayList<>();

  @ManyToMany
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinTable(
      name = "trainees_trainers",
      joinColumns = @JoinColumn(name = "trainee_id"),
      inverseJoinColumns = @JoinColumn(name = "trainer_id"))
  private List<Trainer> trainers = new ArrayList<>();

  @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
  @MapsId
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "trainee_id")
  private User user;

  public void addTrainer(Trainer trainer) {
    trainers.add(trainer);
  }

  public void removeTrainer(Trainer trainer) {
    trainers.remove(trainer);
  }
}

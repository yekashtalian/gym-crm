package org.example.gymcrm.entity;

import jakarta.persistence.*;

import java.util.*;

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
  private Set<Trainer> trainers = new HashSet<>();

  @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
  @MapsId
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "trainee_id")
  private User user;

  public Trainee(User user) {
    this.user = user;
  }

  public void addTrainer(Trainer trainer) {
    trainers.add(trainer);
  }

  public void removeTrainer(Trainer trainer) {
    trainers.remove(trainer);
  }

  public void clearTrainers() {
    trainers.clear();
  }
}

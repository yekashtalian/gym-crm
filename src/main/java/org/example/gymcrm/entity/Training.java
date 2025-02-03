package org.example.gymcrm.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import org.example.gymcrm.entity.enums.TrainingType;

public class Training {
  private String id;
  private String traineeId;
  private String trainerId;
  private String trainingName;
  private TrainingType type;
  private LocalDate date;
  private Integer duration;

  public Training() {
    id = UUID.randomUUID().toString();
  }

  public Training(
      String traineeId,
      String trainerId,
      String trainingName,
      TrainingType type,
      LocalDate date,
      Integer duration) {
    this.id = UUID.randomUUID().toString();
    this.traineeId = traineeId;
    this.trainerId = trainerId;
    this.trainingName = trainingName;
    this.type = type;
    this.date = date;
    this.duration = duration;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTraineeId() {
    return traineeId;
  }

  public void setTraineeId(String traineeId) {
    this.traineeId = traineeId;
  }

  public String getTrainerId() {
    return trainerId;
  }

  public void setTrainerId(String trainerId) {
    this.trainerId = trainerId;
  }

  public String getTrainingName() {
    return trainingName;
  }

  public void setTrainingName(String trainingName) {
    this.trainingName = trainingName;
  }

  public TrainingType getType() {
    return type;
  }

  public void setType(TrainingType type) {
    this.type = type;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Training training = (Training) o;

    return Objects.equals(id, training.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Training{"
        + "id="
        + id
        + ", traineeId="
        + traineeId
        + ", trainerId="
        + trainerId
        + ", trainingName='"
        + trainingName
        + '\''
        + ", type="
        + type
        + ", date="
        + date
        + ", duration="
        + duration
        + '}';
  }
}

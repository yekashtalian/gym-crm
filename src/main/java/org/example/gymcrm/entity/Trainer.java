package org.example.gymcrm.entity;

import org.example.gymcrm.entity.enums.TrainingType;

import java.util.UUID;

public class Trainer extends User {
  private TrainingType specialization;

  public Trainer() {
    setUserId(UUID.randomUUID().toString());
  }

  public Trainer(
      String firstName,
      String lastName,
      String username,
      String password,
      Boolean isActive,
      TrainingType specialization) {
    super(firstName, lastName, username, password, isActive);
    this.specialization = specialization;
  }

  public Trainer(String firstName, String lastName) {
    super(firstName, lastName);
  }

  public TrainingType getSpecialization() {
    return specialization;
  }

  public void setSpecialization(TrainingType specialization) {
    this.specialization = specialization;
  }

  @Override
  public String toString() {
    return "Trainer{id="
        + super.getUserId()
        + ", firstName='"
        + super.getFirstName()
        + '\''
        + ", lastName='"
        + super.getLastName()
        + '\''
        + ", username='"
        + super.getUsername()
        + '\''
        + ", password='"
        + super.getPassword()
        + '\''
        + ", isActive='"
        + super.getIsActive()
        + '\''
        + ", specialization='"
        + specialization
        + '\''
        + '}';
  }
}

package org.example.gymcrm.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Trainee extends User {
  private LocalDate dateOfBirth;
  private String address;

  public Trainee(
      String firstName,
      String lastName,
      String username,
      String password,
      Boolean isActive,
      LocalDate dateOfBirth,
      String address) {
    super(firstName, lastName, username, password, isActive);
    this.dateOfBirth = dateOfBirth;
    this.address = address;
  }

  public Trainee(String firstName, String lastName) {
    super(firstName, lastName);
  }

  public Trainee(String userId, String firstName, String lastName) {
    super(userId, firstName, lastName);
  }

  public Trainee(
      String userId,
      String firstName,
      String lastName,
      String username,
      String password,
      Boolean isActive,
      LocalDate dateOfBirth,
      String address) {
    super(userId, firstName, lastName, username, password, isActive);
    this.dateOfBirth = dateOfBirth;
    this.address = address;
  }

  public Trainee() {
    setUserId(UUID.randomUUID().toString());
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Trainee{id="
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
        + ", dateOfBirth='"
        + dateOfBirth
        + '\''
        + ", address='"
        + address
        + '\''
        + '}';
  }
}

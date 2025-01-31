package org.example.gymcrm.entity;
import java.util.Objects;
import java.util.UUID;

public abstract class User {
  private String userId;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private Boolean isActive;

  public User() {}

  public User(String firstName, String lastName) {
    userId = UUID.randomUUID().toString();
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(
      String firstName, String lastName, String username, String password, Boolean isActive) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.isActive = isActive;
    userId = UUID.randomUUID().toString();
  }

  public User(String userId, String firstName, String lastName) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(String userId, String firstName, String lastName, String username, String password, Boolean isActive) {
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.isActive = isActive;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (!Objects.equals(userId, user.userId)) return false;
    if (!Objects.equals(firstName, user.firstName)) return false;
    if (!Objects.equals(lastName, user.lastName)) return false;
    if (!Objects.equals(username, user.username)) return false;
    return Objects.equals(password, user.password);
  }

  @Override
  public int hashCode() {
    int result = userId != null ? userId.hashCode() : 0;
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (username != null ? username.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    return result;
  }
}

package org.example.gymcrm.entity;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @NotNull(message = "User first name cannot be null")
  @Size(min = 2, max = 30, message = "User first name should be from 2 to 30 symbols")
  @Column(nullable = false)
  private String firstName;

  @NotNull(message = "User last name cannot be null")
  @Size(min = 2, max = 35, message = "User last name size should be from 2 to 35 symbols")
  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  private boolean isActive;

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(String firstName, String lastName, String username, String password, boolean isActive) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.isActive = isActive;
  }

  public User(String username) {
    this.username = username;
  }
}

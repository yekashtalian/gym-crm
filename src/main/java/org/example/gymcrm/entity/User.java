package org.example.gymcrm.entity;

import static jakarta.persistence.GenerationType.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(of = "username")
@ToString
@NoArgsConstructor
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @NotNull(message = "User first name cannot be null")
  @Size(min = 2, max = 30, message = "User first name should be from 2 to 30 symbols")
  @Pattern(
      regexp = "^[A-Za-z-' ]+$",
      message =
          "Invalid format. Only English letters, spaces, hyphens (-), and apostrophes (') are allowed.")
  @Column(nullable = false)
  private String firstName;

  @NotNull(message = "User last name cannot be null")
  @Size(min = 2, max = 35, message = "User last name size should be from 2 to 35 symbols")
  @Pattern(
      regexp = "^[A-Za-z-' ]+$",
      message =
          "Invalid format. Only English letters, spaces, hyphens (-), and apostrophes (') are allowed.")
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

  public User(
      String firstName, String lastName, String username, String password, boolean isActive) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.isActive = isActive;
  }

  public User(String username) {
    this.username = username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}

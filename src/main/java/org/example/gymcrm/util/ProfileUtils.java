package org.example.gymcrm.util;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.exception.ProfileUtilsException;

public class ProfileUtils {
  private static final int PASSWORD_LENGTH = 10;
  private static final String PASSWORD_CHARACTERS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
  private static final Random RANDOM = new Random();

  public static String generateUsername(
      String firstName, String lastName, List<String> existingUsernames) {

    String baseUsername = (firstName + "." + lastName).toLowerCase();
    int counter = 0;
    boolean baseExists = false;

    for (String username : existingUsernames) {
      if (username.equals(baseUsername)) {
        baseExists = true;
      } else if (username.startsWith(baseUsername)) {
        try {
          counter = Math.max(counter, Integer.parseInt(username.substring(baseUsername.length())));
        } catch (NumberFormatException ignored) {
          throw new ProfileUtilsException("Error while trying to parse serial number");
        }
      }
    }

    return baseExists || counter > 0 ? baseUsername + (counter + 1) : baseUsername;
  }

  public static List<String> mergeAllUsernames(
      List<String> traineeUsernames, List<String> trainerUsernames) {
    return Stream.concat(traineeUsernames.stream(), trainerUsernames.stream()).toList();
  }

  public static String generateRandomPassword() {
    StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      int index = RANDOM.nextInt(PASSWORD_CHARACTERS.length());
      password.append(PASSWORD_CHARACTERS.charAt(index));
    }

    return password.toString();
  }
}

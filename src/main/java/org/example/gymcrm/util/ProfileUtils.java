package org.example.gymcrm.util;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.example.gymcrm.exception.ProfileUtilsException;

public class ProfileUtils {
  private static final int PASSWORD_LENGTH = 10;
  private static final String PASSWORD_CHARACTERS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@$^*()-_";
  private static final Random RANDOM = new Random();

  public static String generateUsername(
      String firstName, String lastName, List<String> existingUsernames) {

    String baseUsername = createBaseUsername(firstName, lastName);
    int maxCounter = getMaxCounter(baseUsername, existingUsernames);
    boolean baseExists = existingUsernames.contains(baseUsername);

    if (baseExists || maxCounter > 0) {
      return baseUsername + (maxCounter + 1);
    }
    return baseUsername;
  }

  private static String createBaseUsername(String firstName, String lastName) {
    return (firstName + "." + lastName).toLowerCase();
  }

  private static int getMaxCounter(String baseUsername, List<String> existingUsernames) {
    int maxCounter = 0;

    for (String username : existingUsernames) {
      if (username.startsWith(baseUsername)) {
        maxCounter = Math.max(maxCounter, extractCounter(baseUsername, username));
      }
    }

    return maxCounter;
  }

  private static int extractCounter(String baseUsername, String username) {
    try {
      return username.equals(baseUsername)
          ? 0
          : Integer.parseInt(username.substring(baseUsername.length()));
    } catch (NumberFormatException ignored) {
      throw new ProfileUtilsException("Error while trying to parse serial number");
    }
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

  public static void validateFirstAndLastName(String firstName, String lastName) {
    if (isNullOrEmpty(firstName) || isNullOrEmpty(lastName)) {
      throw new ProfileUtilsException("First or Last name cannot be empty or null");
    }
  }

  private static boolean isNullOrEmpty(String str) {
    return str == null || str.isEmpty();
  }
}

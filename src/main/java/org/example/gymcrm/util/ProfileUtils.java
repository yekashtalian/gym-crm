package org.example.gymcrm.util;

import java.util.List;
import java.util.Random;
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

    for (String username : existingUsernames) {
      if (username.startsWith(baseUsername)) {
        try {
          counter = parseSerialNumber(baseUsername, counter, username);
        } catch (NumberFormatException ignored) {
          throw new ProfileUtilsException("Error while trying to parse serial number");
        }
      }
    }

    return baseUsername + (counter + 1);
  }

  private static int parseSerialNumber(String baseUsername, int counter, String username) {
    int num = Integer.parseInt(username.substring(baseUsername.length()));
    counter = Math.max(counter, num);
    return counter;
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

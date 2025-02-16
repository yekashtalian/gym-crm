package org.example.gymcrm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.example.gymcrm.entity.*;

public class InputReader {
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  public static Date readDate(Scanner scanner, String prompt) {
    System.out.println(prompt);
    while (true) {
      String input = scanner.nextLine().trim();
      if (input.isEmpty()) return null;
      try {
        return DATE_FORMAT.parse(input);
      } catch (ParseException e) {
        System.out.println("Invalid date format. Please enter in yyyy-MM-dd format:");
      }
    }
  }

  public static String readString(Scanner scanner, String prompt) {
    System.out.println(prompt);
    return scanner.nextLine().trim();
  }

  public static Long readId(Scanner scanner) {
    System.out.println("Please enter ID: ");
    while (!scanner.hasNextLong()) {
      System.out.println("Invalid input. Please enter a numeric ID:");
      scanner.next();
    }
    return scanner.nextLong();
  }

  public static TrainingType.Type readTrainingType(Scanner scanner, boolean allowNull) {
    System.out.println(
        "Please select the trainer's specialization by entering the corresponding number:");
    System.out.println("1. STRENGTH_TRAINING");
    System.out.println("2. CARDIO");
    System.out.println("3. YOGA");
    System.out.println("4. PILATES");
    System.out.println("5. HIIT");
    System.out.println("6. CROSSFIT");
    System.out.println("7. BOXING");
    System.out.println("8. DANCE");
    System.out.println("9. INDIVIDUAL");
    System.out.println("10. GROUP");
    if (allowNull) {
      System.out.println("0. (Empty / No specialization)");
    }

    while (true) {
      try {
        String input = scanner.nextLine().trim();
        if (allowNull && (input.isEmpty() || input.equals("0"))) {
          return null;
        }

        int choice = Integer.parseInt(input);
        var type = mapIntToTrainingType(choice);
        if (type != null) {
          return type;
        } else {
          System.out.println("Invalid choice. Please enter a number between 1 and 10:");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number:");
      }
    }
  }

  private static TrainingType.Type mapIntToTrainingType(int choice) {
    switch (choice) {
      case 1:
        return TrainingType.Type.STRENGTH_TRAINING;
      case 2:
        return TrainingType.Type.CARDIO;
      case 3:
        return TrainingType.Type.YOGA;
      case 4:
        return TrainingType.Type.PILATES;
      case 5:
        return TrainingType.Type.HIIT;
      case 6:
        return TrainingType.Type.CROSSFIT;
      case 7:
        return TrainingType.Type.BOXING;
      case 8:
        return TrainingType.Type.DANCE;
      case 9:
        return TrainingType.Type.INDIVIDUAL;
      case 10:
        return TrainingType.Type.GROUP;
      default:
        return null;
    }
  }

  public static Trainer readTrainer(Scanner scanner) {
    var user = new User();
    user.setFirstName(readString(scanner, "Please enter trainer's first name:"));
    user.setLastName(readString(scanner, "Please enter trainer's last name:"));

    var trainingType = new TrainingType();
    trainingType.setName((readTrainingType(scanner, false)));

    var trainer = new Trainer();
    trainer.setUser(user);
    trainer.setSpecialization(trainingType);

    return trainer;
  }

  public static Trainee readTrainee(Scanner scanner) {
    var user = new User();
    user.setFirstName(readString(scanner, "Please enter trainee's first name:"));
    user.setLastName(readString(scanner, "Please enter trainee's last name:"));

    var trainee = new Trainee();
    trainee.setUser(user);
    trainee.setDateOfBirth(readDate(scanner, "Please enter trainee's date of birth (yyyy-MM-dd):"));
    trainee.setAddress(readString(scanner, "Please enter trainee's address:"));

    return trainee;
  }

  public static Training readTraining(Scanner scanner) {
    var traineeUsername = readString(scanner, "Enter trainee username: ");
    var trainerUsername = readString(scanner, "Enter trainer username: ");
    var trainingName = readString(scanner, "Enter training name: ");
    var trainingTypeName = readTrainingType(scanner, false);
    var trainingDate = readDate(scanner, "Enter training date (yyyy-MM-dd): ");
    var duration = readInt("Enter training duration (25-90 minutes): ", 25, 90, scanner);

    var traineeUser = new User();
    traineeUser.setUsername(traineeUsername);
    var trainee = new Trainee();
    trainee.setUser(traineeUser);

    var trainerUser = new User();
    trainerUser.setUsername(trainerUsername);
    var trainer = new Trainer();
    trainer.setUser(trainerUser);

    var trainingType = new TrainingType();
    trainingType.setName((trainingTypeName));

    var training = new Training();
    training.setTrainee(trainee);
    training.setTrainer(trainer);
    training.setName(trainingName);
    training.setType(trainingType);
    training.setDate(trainingDate);
    training.setDuration(duration);

    return training;
  }

  public static Trainer readTrainerToUpdate(Scanner scanner) {
    var user = new User();
    user.setFirstName(readString(scanner, "Please enter trainer's first name to update:"));
    user.setLastName(readString(scanner, "Please enter trainer's last name to update:"));
    user.setUsername(readString(scanner, "Please enter trainer's username to update:"));

    var trainingType = new TrainingType();
    trainingType.setName((readTrainingType(scanner, false)));

    var trainer = new Trainer();
    trainer.setUser(user);
    trainer.setSpecialization(trainingType);

    return trainer;
  }

  public static Trainee readTraineeToUpdate(Scanner scanner) {
    var user = new User();
    user.setFirstName(readString(scanner, "Please enter trainee first name to update:"));
    user.setLastName(readString(scanner, "Please enter trainee last name to update:"));
    user.setUsername(readString(scanner, "Please enter trainee username to update:"));
    var dateOfBirth = readDate(scanner, "Please enter trainee date of birth to update: ");
    var address = readString(scanner, "Please enter trainee address to update: ");

    var trainee = new Trainee();
    trainee.setUser(user);
    trainee.setDateOfBirth(dateOfBirth);
    trainee.setAddress(address);
    return trainee;
  }

  public static String[] readChangePassword(Scanner scanner, String role) {
    System.out.println("Please enter " + role + " username to change password:");
    String username = scanner.nextLine().trim();
    System.out.println("Please enter old password:");
    String oldPassword = scanner.nextLine().trim();
    System.out.println("Please enter new password:");
    String newPassword = scanner.nextLine().trim();
    return new String[] {oldPassword, newPassword, username};
  }

  public static String readUsername(Scanner scanner) {
    System.out.println("Please enter username: ");
    return scanner.nextLine().trim();
  }

  public static String readPassword(Scanner scanner) {
    System.out.println("Please enter password: ");
    return scanner.nextLine().trim();
  }

  private static int readInt(String prompt, int min, int max, Scanner scanner) {
    int value;
    do {
      System.out.print(prompt);
      while (!scanner.hasNextInt()) {
        System.out.println("Invalid input. Please enter a number.");
        scanner.next();
      }
      value = scanner.nextInt();
      scanner.nextLine();
    } while (value < min || value > max);
    return value;
  }
}

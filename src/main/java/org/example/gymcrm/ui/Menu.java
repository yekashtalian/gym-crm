package org.example.gymcrm.ui;

import static org.example.gymcrm.util.InputReader.*;

import java.util.Scanner;

import jakarta.validation.ValidationException;
import org.example.gymcrm.exception.*;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Menu {
  private static final Logger logger = LoggerFactory.getLogger(Menu.class);
  private static final String SEPARATOR =
      "-------------------------------------------------------------------";
  private final Scanner scanner = new Scanner(System.in);
  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingService trainingService;

  public Menu(
      TraineeService traineeService,
      TrainerService trainerService,
      TrainingService trainingService) {
    this.traineeService = traineeService;
    this.trainerService = trainerService;
    this.trainingService = trainingService;
  }

  public void displayMenu() {
    var running = true;
    System.out.println(SEPARATOR);
    System.out.println("WELCOME TO THE GYM CRM!");
    while (running) {
      System.out.println(
          """
                PLEASE, SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:
                    [1] SELECT TRAINEES IN THE GYM
                    [2] CREATE TRAINEE IN THE GYM
                    [3] UPDATE TRAINEE IN THE GYM
                    [4] DELETE TRAINEE BY USERNAME IN THE GYM
                    [5] SELECT TRAINEE BY USERNAME IN THE GYM
                    [6] CHANGE TRAINEE PASSWORD
                    [7] CHANGE TRAINEE STATUS
                    [8] SELECT TRAINEE TRAININGS LIST

                    [9] SELECT TRAINERS IN THE GYM
                    [10] CREATE TRAINER IN THE GYM
                    [11] UPDATE TRAINER IN THE GYM
                    [12] SELECT TRAINER BY USERNAME IN THE GYM
                    [13] CHANGE TRAINER PASSWORD
                    [14] CHANGE TRAINER STATUS
                    [15] SELECT TRAINER TRAININGS LIST
                    [16] SELECT UNASSIGNED TRAINERS BY TRAINEE USERNAME

                    [17] SELECT TRAININGS IN THE GYM
                    [18] CREATE TRAINING IN THE GYM

                    [19] ADD TRAINER TO TRAINEE LIST
                    [20] REMOVE TRAINER FROM TRAINEE LIST
                TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!
                        """);
      try {
        switch (scanner.nextLine().toLowerCase()) {
          case "1" -> showAllTrainees();
          case "2" -> createTrainee();
          case "3" -> updateTrainee();
          case "4" -> deleteTrainee();
          case "5" -> showTraineeByUsername();
          case "6" -> changeTraineePassword();
          case "7" -> changeTraineeStatus();
          case "8" -> showTraineeTrainings();
          case "9" -> showAllTrainers();
          case "10" -> createTrainer();
          case "11" -> updateTrainer();
          case "12" -> showTrainerByUsername();
          case "13" -> changeTrainerPassword();
          case "14" -> changeTrainerStatus();
          case "15" -> showTrainerTrainings();
          case "16" -> showUnassignedTrainers();
          case "17" -> showTrainings();
          case "18" -> createTraining();
          case "19" -> addTrainerToTraineeList();
          case "20" -> removeTrainerFromTraineeList();
          case "exit" -> {
            running = false;
            exitFromMenu();
          }
          default -> logger.warn("Invalid option, please write correct option from the menu.");
        }
      } catch (TraineeServiceException
          | TrainerServiceException
          | TrainingServiceException
          | ProfileUtilsException
          | AuthenticationException
          | ValidationException ex) {
        logger.error(ex.getMessage());
      }
      System.out.println(SEPARATOR);
    }
  }

  private void removeTrainerFromTraineeList() {
    if (isAuthenticatedTrainee()) {
      var traineeUsername = readString(scanner, "Please enter trainee username");
      var trainerUsername = readString(scanner, "Please enter trainer username");
      traineeService.removeTrainerFromList(traineeUsername, trainerUsername);
    }
  }

  private void addTrainerToTraineeList() {
    if (isAuthenticatedTrainee()) {
      var traineeUsername = readString(scanner, "Please enter trainee username");
      var trainerUsername = readString(scanner, "Please enter trainer username");
      traineeService.addTrainerToList(traineeUsername, trainerUsername);
    }
  }

  private void createTraining() {
    var training = readTraining(scanner);
    trainingService.save(training);
  }

  private void showTrainings() {
    System.out.println(trainingService.getAll());
  }

  private void deleteTrainee() {
    if (isAuthenticatedTrainee()) {
      var username = readString(scanner, "Please enter trainee username to delete: ");
      traineeService.deleteByUsername(username);
    }
  }

  private void updateTrainee() {
    if (isAuthenticatedTrainee()) {
      var traineeId = readId(scanner);
      scanner.nextLine();
      var updatedTrainee = readTraineeToUpdate(scanner);
      traineeService.update(traineeId, updatedTrainee);
    }
  }

  private void showAllTrainers() {
    if (isAuthenticatedTrainer()) {
      trainerService.getAll().forEach(System.out::println);
    }
  }

  private void showAllTrainees() {
    if (isAuthenticatedTrainee()) {
      traineeService.findAll().forEach(System.out::println);
    }
  }

  private void showUnassignedTrainers() {
    if (isAuthenticatedTrainer()) {
      var username = readString(scanner, "Please enter trainee username: ");
      System.out.println(trainerService.getUnassignedTrainers(username));
    }
  }

  private void showTrainerTrainings() {
    if (isAuthenticatedTrainer()) {
      var username = readString(scanner, "Please enter trainer username: ");
      var fromDate = readDate(scanner, "Please enter from date (yyyy-MM-dd): ");
      var toDate = readDate(scanner, "Please enter to date (yyyy-MM-dd): ");
      var type = readTrainingType(scanner, true);
      var firstName = readString(scanner, "Please enter trainer first name: ");
      System.out.println(
          trainingService.getTrainingsByTrainerUsername(
              username, fromDate, toDate, type, firstName));
    }
  }

  private void showTraineeTrainings() {
    if (isAuthenticatedTrainee()) {
      var username = readString(scanner, "Please enter trainee username: ");
      var fromDate = readDate(scanner, "Please enter from date (yyyy-MM-dd): ");
      var toDate = readDate(scanner, "Please enter to date (yyyy-MM-dd): ");
      var firstName = readString(scanner, "Please enter trainee first name: ");
      System.out.println(
          trainingService.getTrainingsByTraineeUsername(username, fromDate, toDate, firstName));
    }
  }

  private void updateTrainer() {
    if (isAuthenticatedTrainer()) {
      var trainerId = readId(scanner);
      scanner.nextLine();
      var updatedTrainer = readTrainerToUpdate(scanner);
      trainerService.update(trainerId, updatedTrainer);
    }
  }

  private void changeTrainerStatus() {
    if (isAuthenticatedTrainer()) {
      var trainerId = readId(scanner);
      trainerService.changeStatus(trainerId);
    }
  }

  private void changeTraineeStatus() {
    if (isAuthenticatedTrainee()) {
      var traineeId = readId(scanner);
      traineeService.changeStatus(traineeId);
    }
  }

  private void changeTraineePassword() {
    if (isAuthenticatedTrainee()) {
      var arguments = readChangePassword(scanner, "trainee");
      traineeService.changePassword(arguments[0], arguments[1], arguments[2]);
    }
  }

  private void changeTrainerPassword() {
    if (isAuthenticatedTrainer()) {
      var arguments = readChangePassword(scanner, "trainer");
      trainerService.changePassword(arguments[0], arguments[1], arguments[2]);
    }
  }

  private void createTrainee() {
    var trainee = readTrainee(scanner);
    traineeService.save(trainee);
  }

  private void createTrainer() {
    var trainer = readTrainer(scanner);
    trainerService.save(trainer);
  }

  private void showTraineeByUsername() {
    if (isAuthenticatedTrainee()) {
      var username = readString(scanner, "Please enter trainee username: ");
      System.out.println(traineeService.findByUsername(username));
    }
  }

  private void showTrainerByUsername() {
    if (isAuthenticatedTrainer()) {
      var username = readString(scanner, "Please enter trainer username: ");
      System.out.println(trainerService.findByUsername(username));
    }
  }

  private boolean isAuthenticatedTrainee() {
    var username = readString(scanner, "Please enter username to authenticate");
    var password = readString(scanner, "Please enter password to authenticate");
    return traineeService.authenticate(username, password);
  }

  private boolean isAuthenticatedTrainer() {
    var username = readString(scanner, "Please enter username to authenticate");
    var password = readString(scanner, "Please enter password to authenticate");
    return trainerService.authenticate(username, password);
  }

  private void exitFromMenu() {
    scanner.close();
    logger.info("GOODBYE!");
  }
}

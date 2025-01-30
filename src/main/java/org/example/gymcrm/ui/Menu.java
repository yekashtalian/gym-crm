package org.example.gymcrm.ui;

import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.enums.TrainingType;
import org.example.gymcrm.exception.ProfileUtilsException;
import org.example.gymcrm.exception.TraineeServiceException;
import org.example.gymcrm.exception.TrainerServiceException;
import org.example.gymcrm.exception.TrainingServiceException;
import org.example.gymcrm.service.TraineeService;
import org.example.gymcrm.service.TrainerService;
import org.example.gymcrm.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;

@Component
public class Menu {
  private static final Logger logger = LoggerFactory.getLogger(Menu.class);
  private static final String SET_GREEN_TEXT_COLOR = "\u001B[32m";
  private static final String SET_DEFAULT_TEXT_COLOR = "\u001B[0m";
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
                    [4] DELETE TRAINEE IN THE GYM

                    [5] SELECT TRAINERS IN THE GYM
                    [6] CREATE TRAINER IN THE GYM
                    [7] UPDATE TRAINER IN THE GYM

                    [8] SELECT TRAININGS IN THE GYM
                    [9] CREATE TRAINING IN THE GYM
                    [10] TEST USERNAME SCENARIO
                TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!
                        """);
      try {
        switch (scanner.nextLine().toLowerCase()) {
          case "1" -> showAllTrainees();
          case "2" -> createTrainee();
          case "3" -> updateTrainee();
          case "4" -> deleteTrainee();
          case "5" -> showAllTrainers();
          case "6" -> createTrainer();
          case "7" -> updateTrainer();
          case "8" -> showAllTrainings();
          case "9" -> createTraining();
          case "10" -> checkUsernameScenario();
          case "exit" -> {
            running = false;
            exitFromMenu();
          }
          default ->
              System.err.println("Invalid option, please write correct option from the menu.");
        }
      } catch (TraineeServiceException | TrainerServiceException | TrainingServiceException | ProfileUtilsException ex) {
        logger.error(ex.getMessage());
      }
      System.out.println(SEPARATOR);
    }
  }

  private void createTraining() {
    var training =
        new Training(
            "550e8400-e29b-41d4-a716-446655440000",
            "550e8400-e29b-41d4-a716-446655440002",
            "Strength Training Session",
            TrainingType.STRENGTH_TRAINING,
            LocalDate.of(2025, 2, 1),
            60);
    trainingService.save(training);
  }

  private void showAllTrainings() {
    trainingService.getAll().forEach(System.out::println);
  }

  private void updateTrainer() {
    var updatedTrainer =
            new Trainer(
                    "UpdatedName",
                    "updatedSurname",
                    "updatedUsername",
                    "updateTrainingName",
                    true,
                    TrainingType.STRENGTH_TRAINING);

    trainerService.update("550e8400-e29b-41d4-a716-446655440000", updatedTrainer);
  }

  private void createTrainer() {
    var trainer = new Trainer("John", "Smith");
    trainerService.save(trainer);
  }

  private void showAllTrainers() {
    trainerService.getAll().forEach(System.out::println);
  }

  private void deleteTrainee() {
    traineeService.delete("abcd-1234");
  }

  private void updateTrainee() {
    var updatedTrainee =
        new Trainee(
            "UpdatedName",
            "updatedSurname",
            "updatedUsername",
            "updatePassword",
            true,
            LocalDate.of(1995, 5, 15),
            "updatedAddress");

    traineeService.update("550e8400-e29b-41d4-a716-446655440000", updatedTrainee);
  }

  private void createTrainee() {
    var trainee =
        new Trainee(
            "abcd-1234",
            "Jane",
            "Smith",
            "john.smith",
            "mypassword",
            true,
            LocalDate.of(1995, 5, 15),
            "123 Elm Street");

    traineeService.save(trainee);
  }

  private void showAllTrainees() {
    traineeService.getAll().forEach(System.out::println);
  }

  private void checkUsernameScenario() {
    var trainee = new Trainee("1", "John", "Smith");
    var trainee1 = new Trainee("2", "John", "Smith");
    var trainee2 = new Trainee("3", "John", "Smith");
    traineeService.save(trainee);
    traineeService.save(trainee1);
    traineeService.save(trainee2);

    traineeService.delete("2");

    var trainee3 = new Trainee("4", "John", "Smith");
    traineeService.save(trainee3);

    traineeService.delete("3");

    var trainee4 = new Trainee("5", "John", "Smith");
    traineeService.save(trainee4);

    traineeService.delete("5");

    var trainee5 = new Trainee("6", "John", "Smith");
    traineeService.save(trainee5);
  }

  private void exitFromMenu() {
    scanner.close();
    logger.info("GOODBYE!");
  }
}

package org.example.gymcrm.dao;

import org.example.gymcrm.dao.Storage;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.entity.enums.TrainingType;
import org.example.gymcrm.exception.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

  private Storage storage;

  @TempDir Path tempDir;

  private String testFilePath;

  @BeforeEach
  void setUp() {
    storage = new Storage();
    testFilePath = tempDir.resolve("test_storage.json").toString();
    storage.setFilePath(testFilePath);
  }

  @Test
  void testInitStorageIfEmptyFile() {
    File emptyFile = new File(testFilePath);
    try {
      emptyFile.createNewFile();
    } catch (IOException e) {
      fail();
    }
    assertThrows(StorageException.class, storage::initStorage);
  }

  @Test
  void testSaveStorageState() {
    storage
        .getTraineeStorage()
        .put(
            "Trainee",
            List.of(
                new Trainee(
                    "TestName",
                    "TestSurname",
                    "testusername",
                    "testpassword",
                    true,
                    LocalDate.of(2000, 2, 20),
                    "Test Address")));
    storage
        .getTrainerStorage()
        .put(
            "Trainer",
            List.of(
                new Trainer(
                    "TestName",
                    "TestSurname",
                    "testUsername",
                    "password",
                    true,
                    TrainingType.BOXING)));
    storage
        .getTrainingStorage()
        .put(
            "Training",
            List.of(
                new Training(
                    "1234",
                    "0",
                    "testTraining",
                    TrainingType.CROSSFIT,
                    LocalDate.of(2025, 1, 25),
                    60)));

    assertDoesNotThrow(storage::saveStorageState);

    File savedFile = new File(testFilePath);
    assertTrue(savedFile.exists());
    assertTrue(savedFile.length() > 0);
  }
}

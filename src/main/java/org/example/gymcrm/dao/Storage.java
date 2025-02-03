package org.example.gymcrm.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.example.gymcrm.entity.Trainee;
import org.example.gymcrm.entity.Trainer;
import org.example.gymcrm.entity.Training;
import org.example.gymcrm.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Storage {
  private static final Logger logger = LoggerFactory.getLogger(Storage.class);
  private Map<String, List<Trainee>> traineeStorage = new HashMap<>();
  private Map<String, List<Trainer>> trainerStorage = new HashMap<>();
  private Map<String, List<Training>> trainingStorage = new HashMap<>();

  @Value("${storage.file.path}")
  private String filePath;

  private ObjectMapper objectMapper;

  @Autowired
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  void init() {
    try {
      var file = new File(filePath);
      Map<String, Object> data = objectMapper.readValue(file, new TypeReference<>() {});
      traineeStorage.put(
          "Trainee",
          objectMapper.convertValue(data.get("Trainee"), new TypeReference<List<Trainee>>() {}));
      trainerStorage.put(
          "Trainer",
          objectMapper.convertValue(data.get("Trainer"), new TypeReference<List<Trainer>>() {}));
      trainingStorage.put(
          "Training",
          objectMapper.convertValue(data.get("Training"), new TypeReference<List<Training>>() {}));

      logger.info("Successfully read data from: {}", filePath);

    } catch (IOException ex) {
      throw new StorageException("Error initializing storage from file: " + filePath);
    }
  }

  @PreDestroy
  void save() {
    try {
      Map<String, Object> data = new HashMap<>();
      data.put("Trainee", traineeStorage.get("Trainee"));
      data.put("Trainer", trainerStorage.get("Trainer"));
      data.put("Training", trainingStorage.get("Training"));
      objectMapper.writeValue(new File(filePath), data);
      logger.info("Successfully saved storage state into: {}", filePath);
    } catch (IOException e) {
      throw new StorageException("Error saving storage to file: " + filePath);
    }
  }

  public Map<String, List<Trainee>> getTraineeStorage() {
    return traineeStorage;
  }

  public Map<String, List<Trainer>> getTrainerStorage() {
    return trainerStorage;
  }

  public Map<String, List<Training>> getTrainingStorage() {
    return trainingStorage;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
}

package org.example.gymcrm.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gymcrm.dto.TrainingTypeDto;
import org.example.gymcrm.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class TrainingTypeControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingTypeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getTrainingTypes() throws Exception {
        var trainingType1 = TrainingTypeDto.builder().name("CARDIO").build();
        var trainingType2 = TrainingTypeDto.builder().name("STRENGTH").build();
        var trainingTypes = List.of(trainingType1, trainingType2);

        when(trainingTypeService.findAll()).thenReturn(trainingTypes);

        mockMvc.perform(get("/api/v1/training-types")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value(trainingType1.getName()))
               .andExpect(jsonPath("$[1].name").value(trainingType2.getName()));

        verify(trainingTypeService, times(1)).findAll();
    }
}

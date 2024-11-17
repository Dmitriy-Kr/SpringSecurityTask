package edu.java.springsecuritytask.controller;

import edu.java.springsecuritytask.dto.TrainingDto;
import edu.java.springsecuritytask.exception.InvalidDataException;
import edu.java.springsecuritytask.service.ServiceException;
import edu.java.springsecuritytask.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static edu.java.springsecuritytask.utility.Validation.*;

@Controller
@RequestMapping("/training")
public class TrainingController {

    private TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    @ResponseBody
    @Operation(summary = "Create new Training")
    public void create(@RequestBody TrainingDto trainingDto) throws InvalidDataException, ServiceException {
        validateLogin(trainingDto.getTraineeUsername());

            validateName(trainingDto.getTrainingName());
            validateDate(trainingDto.getTrainingDay());
            trainingService.create(trainingDto);

    }
}

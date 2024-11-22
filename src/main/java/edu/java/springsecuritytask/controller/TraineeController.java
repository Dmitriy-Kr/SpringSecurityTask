package edu.java.springsecuritytask.controller;

import edu.java.springsecuritytask.dto.TraineeCreatedDto;
import edu.java.springsecuritytask.dto.TraineeDto;
import edu.java.springsecuritytask.dto.TraineeTrainingDto;
import edu.java.springsecuritytask.dto.TrainerDtoForTrainee;
import edu.java.springsecuritytask.entity.Trainee;
import edu.java.springsecuritytask.exception.InvalidDataException;
import edu.java.springsecuritytask.exception.NoResourcePresentException;
import edu.java.springsecuritytask.service.ServiceException;
import edu.java.springsecuritytask.service.TraineeService;
import edu.java.springsecuritytask.utility.MappingUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.java.springsecuritytask.utility.MappingUtils.mapToTrainee;
import static edu.java.springsecuritytask.utility.MappingUtils.mapToTraineeDto;
import static edu.java.springsecuritytask.utility.Validation.*;

@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping("/trainee")
public class TraineeController {
    private TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "Get trainee profile by Username")
    public TraineeDto findByUsername(@RequestParam("username") String username) throws ServiceException, NoResourcePresentException, InvalidDataException {
        Optional<Trainee> trainee;
        validateLogin(username);
        trainee = traineeService.findByUsername(username);
        if (trainee.isPresent()) {
            return mapToTraineeDto(trainee.get());
        } else {
            throw new NoResourcePresentException("Cannot find trainee with username " + username);
        }
    }

    @GetMapping("/trainers-no-assigned")
    @ResponseBody
    @Operation(summary = "Get not assigned on trainee active trainers")
    public List<TrainerDtoForTrainee> findNoAssignedActiveTrainers(@RequestParam("username") String username) throws ServiceException, InvalidDataException {
        validateLogin(username);
        return traineeService.getNotAssignedOnTraineeTrainersByTraineeUsername(username).stream()
                .map(MappingUtils::mapToTrainerDtoForTrainee)
                .collect(Collectors.toList());
    }

    @GetMapping("/trainings")
    @ResponseBody
    @Operation(summary = "Get Trainee Trainings List")
    public List<TraineeTrainingDto> findTrainings(@RequestParam("username") String username,
                                                  @RequestParam(value = "fromDate", required = false) String fromDateParameter,
                                                  @RequestParam(value = "toDate", required = false) String toDateParameter,
                                                  @RequestParam(value = "trainerName", required = false) String trainerName,
                                                  @RequestParam(value = "trainingType", required = false) String trainingType) throws ServiceException, InvalidDataException {

        validateLogin(username);

        Date fromDate = null;
        Date toDate = null;

        if (fromDateParameter != null) {
            if (validateDate(fromDateParameter)) {
                fromDate = Date.valueOf(LocalDate.parse(fromDateParameter));
            }
        }

        if (toDateParameter != null) {
            if (validateDate(toDateParameter)) {
                toDate = Date.valueOf(LocalDate.parse(toDateParameter));
            }
        }

        if (trainerName != null) {
            validateName(trainerName);
        }

        if (trainingType != null) {
            validateName(trainingType);
        }

        return traineeService.getTrainings(username, fromDate, toDate, trainerName, trainingType).stream()
                .map(MappingUtils::mapToTraineeTrainingDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "New Trainee registration")
    public TraineeCreatedDto create(@RequestBody TraineeDto traineeDto) throws InvalidDataException {
        validateName(traineeDto.getFirstname());
        validateName(traineeDto.getLastname());
        return traineeService.save(mapToTrainee(traineeDto));
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Update Trainee Profile")
    public TraineeDto update(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException, InvalidDataException {
        traineeDto.getUsername();
        validateName(traineeDto.getFirstname());
        validateName(traineeDto.getLastname());
        Trainee trainee = mapToTrainee(traineeDto);
        trainee.setId(id);
        return mapToTraineeDto(traineeService.update(trainee).orElseThrow(() -> new ServiceException("Update failed")));
    }

    @PutMapping("/{id}/trainers")
    @ResponseBody
    @Operation(summary = "Update Trainee's Trainer List")
    public List<TrainerDtoForTrainee> updateTrainerList(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException, InvalidDataException {

        validateLogin(traineeDto.getUsername());
        for (TrainerDtoForTrainee trainer : traineeDto.getTrainers()) {
            validateLogin(trainer.getUsername());
        }
        Trainee trainee = mapToTrainee(traineeDto);
        trainee.setId(id);
        return traineeService.updateTrainersList(trainee).stream().map(MappingUtils::mapToTrainerDtoForTrainee).collect(Collectors.toList());
    }

    @PatchMapping("/{id}/status")
    @ResponseBody
    @Operation(summary = "Activate/De-Activate Trainee")
    public TraineeDto changeStatus(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException, InvalidDataException {

        validateLogin(traineeDto.getUsername());
        Trainee trainee = mapToTrainee(traineeDto);
        trainee.setId(id);
        return mapToTraineeDto(traineeService.changeStatus(trainee).orElseThrow(() -> new ServiceException("Cannot change status")));
    }

    @DeleteMapping
    @ResponseBody
    @Operation(summary = "Delete Trainee Profile")
    public void delete(@RequestParam("username") String username) throws ServiceException, InvalidDataException {

        validateLogin(username);
        traineeService.deleteByUsername(username);
    }
}

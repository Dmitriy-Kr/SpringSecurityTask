package edu.java.springsecuritytask.utility;

import edu.java.springsecuritytask.dto.*;
import edu.java.springsecuritytask.entity.*;

import java.util.stream.Collectors;

public class MappingUtils {
    private MappingUtils() {
    }

    public static TraineeDto mapToTraineeDto(Trainee trainee) {
        TraineeDto dto = new TraineeDto();
        dto.setId(trainee.getId());
        dto.setFirstname(trainee.getUser().getFirstname());
        dto.setLastname(trainee.getUser().getLastname());
        dto.setUsername(trainee.getUser().getUsername());
        dto.setPassword(trainee.getUser().getPassword());
        dto.setActive(trainee.getUser().isActive());
        dto.setDateOfBirth(trainee.getDateOfBirth());
        dto.setAddress(trainee.getAddress());
        dto.setTrainers(trainee.getTrainers().stream().map(MappingUtils::mapToTrainerDtoForTrainee).collect(Collectors.toList()));
        return dto;
    }

    public static Trainee mapToTrainee(TraineeDto traineeDto) {
        Trainee trainee = new Trainee();
        trainee.setAddress(traineeDto.getAddress());
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
        if (traineeDto.getTrainers() != null) {
            trainee.setTrainers(traineeDto.getTrainers().stream().map(MappingUtils::mapToTrainer).collect(Collectors.toList()));
        }

        User user = new User();
        user.setFirstname(traineeDto.getFirstname());
        user.setLastname(traineeDto.getLastname());
        user.setIsActive(traineeDto.getActive());
        trainee.setUser(user);
        return trainee;
    }

    public static TrainerDtoForTrainee mapToTrainerDtoForTrainee(Trainer trainer) {
        TrainerDtoForTrainee dto = new TrainerDtoForTrainee();
        dto.setFirstname(trainer.getUser().getFirstname());
        dto.setLastname(trainer.getUser().getLastname());
        dto.setUsername(trainer.getUser().getUsername());
        dto.setTrainingType(trainer.getSpecialization().getTrainingType());
        return dto;
    }

    public static Trainer mapToTrainer(TrainerDtoForTrainee trainerDto) {
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new TrainingType(trainerDto.getTrainingType()));

        User user = new User();
        user.setFirstname(trainerDto.getFirstname());
        user.setLastname(trainerDto.getLastname());
        user.setUsername(trainerDto.getUsername());
        trainer.setUser(user);
        return trainer;
    }

    public static Trainer mapToTrainer(TrainerDto trainerDto) {
        Trainer trainer = new Trainer();
        trainer.setSpecialization(new TrainingType(trainerDto.getSpecialization()));

        User user = new User();
        user.setFirstname(trainerDto.getFirstname());
        user.setLastname(trainerDto.getLastname());
        user.setUsername(trainerDto.getUsername());
        user.setIsActive(trainerDto.getActive());
        trainer.setUser(user);
        return trainer;
    }

    public static TrainerDto mapToTrainerDto(Trainer trainer) {
        TrainerDto dto = new TrainerDto();
        dto.setId(trainer.getId());
        dto.setFirstname(trainer.getUser().getFirstname());
        dto.setLastname(trainer.getUser().getLastname());
        dto.setUsername(trainer.getUser().getUsername());
        dto.setPassword(trainer.getUser().getPassword());
        dto.setActive(trainer.getUser().isActive());
        dto.setSpecialization(trainer.getSpecialization().getTrainingType());
        return dto;
    }

    public static TraineeTrainingDto mapToTraineeTrainingDto (Training training) {
        TraineeTrainingDto dto = new TraineeTrainingDto();

        dto.setTrainingName(training.getTrainingName());
        dto.setTrainingDay(training.getTrainingDay());
        dto.setTrainingType(training.getTrainingType().getTrainingType());
        dto.setTrainingDuration(training.getTrainingDuration());
        dto.setTrainerName(training.getTrainer().getUser().getUsername());

        return dto;
    }

    public static TrainerTrainingDto mapToTrainerTrainingDto (Training training) {
        TrainerTrainingDto dto = new TrainerTrainingDto();

        dto.setTrainingName(training.getTrainingName());
        dto.setTrainingDay(training.getTrainingDay());
        dto.setTrainingType(training.getTrainingType().getTrainingType());
        dto.setTrainingDuration(training.getTrainingDuration());
        dto.setTraineeName(training.getTrainee().getUser().getUsername());

        return dto;
    }

    public static TrainingTypeDto mapToTrainingTypeDto (TrainingType trainingType) {
        TrainingTypeDto dto = new TrainingTypeDto();

        dto.setId(trainingType.getId());
        dto.setTrainingType(trainingType.getTrainingType());

        return dto;
    }

}

package edu.java.springsecuritytask.service;

import edu.java.springsecuritytask.dto.TrainerCreatedDto;
import edu.java.springsecuritytask.entity.*;
import edu.java.springsecuritytask.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static edu.java.springsecuritytask.utility.PasswordGenerator.generatePassword;

@Service
@Transactional(readOnly = true)
public class TrainerService {

    private TrainerRepository trainerRepository;
    private UserRepository userRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private PasswordEncoder passwordEncoder;

    private static Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public TrainerService(TrainerRepository trainerRepository,
                          UserRepository userRepository,
                          TrainingTypeRepository trainingTypeRepository,
                          PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TrainerCreatedDto save(Trainer trainer) throws ServiceException {
        String password = generatePassword();

        trainer.getUser().setUsername(createValidUserName(trainer));
        trainer.getUser().setPassword(passwordEncoder.encode(password));
        trainer.setSpecialization(trainingTypeRepository
                .findByTrainingType(trainer.getSpecialization().getTrainingType())
                .orElseThrow(() -> new ServiceException("Cannot find specialization")));
        trainer.getUser().setIsActive(true);

        try {

            trainer = trainerRepository.save(trainer);
            return new TrainerCreatedDto(trainer.getUser().getUsername(), password);

        } catch (Exception e) {
            logger.error("Error saving Trainer in the database with username {}", trainer.getUser().getUsername());
            throw new ServiceException("Error saving Trainer in the database", e);
        }
    }

    public Optional<Trainer> usernameAndPasswordMatching(String userName, String password) throws ServiceException {
        Optional<Trainer> trainer = findByUsername(userName);
        if (trainer.isPresent()) {
            if (password.equals(trainer.get().getUser().getPassword())) {
                return trainer;
            }
        }
        return Optional.empty();
    }

    public Optional<Trainer> findByUsername(String username) throws ServiceException {

        try {

            Optional<User> userFromDB = userRepository.findByUsername(username);

            if (userFromDB.isPresent() && Objects.nonNull(userFromDB.get().getTrainer())) {

                return Optional.of(userFromDB.get().getTrainer());

            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            logger.error("Fail to get trainer with username {}  from DB", username);
            throw new ServiceException("Fail to get from DB trainer with username " + username, e);
        }
    }

    @Transactional
    public Optional<Trainer> update(Trainer trainer) throws ServiceException {
        Optional<Trainer> trainerFromDB = trainerRepository.findById(trainer.getId());

        if (trainerFromDB.isPresent()) {

            trainerFromDB.get().getUser().setFirstname(trainer.getUser().getFirstname());
            trainerFromDB.get().getUser().setLastname(trainer.getUser().getLastname());
            trainerFromDB.get().getUser().setIsActive(trainer.getUser().isActive());

            if (trainer.getSpecialization() != null) {
                trainerFromDB.get()
                        .setSpecialization(trainingTypeRepository
                                .findByTrainingType(trainer.getSpecialization().getTrainingType())
                                .orElseThrow(() -> new ServiceException("Cannot find specialization")));
            }

            trainer = trainerRepository.save(trainerFromDB.get());
        } else {
            return Optional.empty();
        }

        return Optional.of(trainer);
    }

    @Transactional
    public Optional<Trainer> changeStatus(Trainer trainer) throws ServiceException {
        Optional<Trainer> trainerFromDB = trainerRepository.findById(trainer.getId());

        if (trainerFromDB.isPresent()) {
            trainerFromDB.get().getUser().setIsActive(trainer.getUser().isActive());
            trainer = trainerRepository.save(trainerFromDB.get());
        } else {
            return Optional.empty();
        }

        return Optional.of(trainer);
    }

    public List<Training> getTrainings(String trainerUsername, Date fromDate, Date toDate, String traineeName) throws ServiceException {

        Optional<Trainer> trainerFromDB = findByUsername(trainerUsername);

        Predicate<Training> fromDateTest = fromDate != null ? t -> t.getTrainingDay().compareTo(fromDate) >= 0 : t -> true;
        Predicate<Training> toDateTest = toDate != null ? t -> t.getTrainingDay().compareTo(toDate) <= 0 : t -> true;
        Predicate<Training> traineeNameTest = traineeName != null ? t -> t.getTrainee().getUser().getFirstname().equals(traineeName) : t -> true;

        return trainerFromDB
                .map(
                        trainer -> trainer.getTrainings().stream()
                                .filter(fromDateTest.and(toDateTest).and(traineeNameTest))
                                .collect(Collectors.toList())
                )
                .orElseGet(ArrayList::new);
    }

    private String createValidUserName(Trainer trainer) {

        String userName = trainer.getUser().getFirstname() + "." + trainer.getUser().getLastname();

        if (userRepository.findByUsername(userName).isEmpty()) {
            return userName;
        }

        for (long i = 0; i < Long.MAX_VALUE; i++) {
            StringBuilder newUserName = new StringBuilder(userName + i);
            if (userRepository.findByUsername(newUserName.toString()).isEmpty()) {
                return newUserName.toString();
            }
        }

        return userName;
    }
}

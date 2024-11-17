package edu.java.springsecuritytask.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;
@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
    private String trainingName;
    @ManyToOne
    @JoinColumn(name = "trainingType_id")
    private TrainingType trainingType;
    private Date trainingDay;
    private Integer trainingDuration;

    public Training() {
    }

    public Training(Long id, Trainee trainee, Trainer trainer, String trainingName, TrainingType trainingType, Date trainingDay, Integer trainingDuration) {
        this.id = id;
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDay = trainingDay;
        this.trainingDuration = trainingDuration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public Date getTrainingDay() {
        return trainingDay;
    }

    public void setTrainingDay(Date trainingDay) {
        this.trainingDay = trainingDay;
    }

    public Integer getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(Integer trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Training training = (Training) o;
        return getId().equals(training.getId()) && getTrainee().equals(training.getTrainee()) && getTrainer().equals(training.getTrainer()) && getTrainingName().equals(training.getTrainingName()) && getTrainingType().equals(training.getTrainingType()) && getTrainingDay().equals(training.getTrainingDay()) && getTrainingDuration().equals(training.getTrainingDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTrainee(), getTrainer(), getTrainingName(), getTrainingType(), getTrainingDay(), getTrainingDuration());
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainee=" + trainee.getUser().getUsername() +
                ", trainer=" + trainer.getUser().getUsername() +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDay=" + trainingDay +
                ", trainingDuration=" + trainingDuration +
                '}';
    }
}

package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "briefings_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BriefingResultEntity extends BaseEntity<Long> {

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "correct_answers")
    private Integer correctAnswers;

    @Column(name = "percentage", insertable = false)
    private Double percentage;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "briefing_id")
    private BriefingEntity briefing;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", totalQuestion=" + totalQuestions + ", correctAnswers=" + correctAnswers
                + ", percentage=" + percentage + ", status=" + status + "]";
    }

}

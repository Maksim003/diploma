package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity extends BaseEntity<Long> {

    private String name;

    @ManyToMany
    @JoinTable(name = "m2m_questions_answers", joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id"))
    private List<AnswerEntity> answers;

    @ManyToMany(mappedBy = "questions")
    private List<BriefingEntity> briefings;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", name=" + name + "]";
    }

}

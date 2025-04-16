package com.example.diploma.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerEntity extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @ManyToMany(mappedBy = "answers")
    private List<QuestionEntity> questions;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", name=" + name + ", isCorrect=" + isCorrect + "]";
    }

}

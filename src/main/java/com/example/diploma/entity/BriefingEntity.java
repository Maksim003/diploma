package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "briefings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BriefingEntity extends BaseEntity<Long> {

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "creator")
    private UserEntity creator;

    @ManyToMany
    @JoinTable(name = "m2m_briefings_questions", joinColumns = @JoinColumn(name = "briefing_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<QuestionEntity> questions;

    public BriefingEntity(Long id) {
        this.setId(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", type=" + type + "]";
    }

}

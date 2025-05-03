package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appeals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppealEntity extends BaseEntity<Long> {

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "subject")
    public String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity creator;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", date=" + date + ", subject=" + subject
                + ", description=" + description + ", answer=" + answer + "]";
    }

}

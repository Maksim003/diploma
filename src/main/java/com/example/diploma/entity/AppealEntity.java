package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appeals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppealEntity extends BaseEntity<Long> {

    @Column(name = "subject")
    public String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", subject=" + subject
                + ", description=" + description + ", status=" + status + "]";
    }

}

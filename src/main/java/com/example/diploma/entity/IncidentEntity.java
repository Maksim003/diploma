package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidentEntity extends BaseEntity<Long> {

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "actions_taken")
    private String actions;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity user;

    @ManyToMany
    @JoinTable(name = "m2m_incidents_users", joinColumns = @JoinColumn(name = "incident_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> users;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", date=" + date + ", type=" + type
                + ", description=" + description + ", actions=" + actions + "]";
    }

}

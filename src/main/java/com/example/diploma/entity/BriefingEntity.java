package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", type=" + type + "]";
    }

}

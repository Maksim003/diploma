package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vacations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacationEntity extends BaseEntity<Long> {

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private UserEntity approvedBy;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + getId() + ", startDate=" + startDate + ", endDate=" + endDate
                + ", type=" + type + ", status=" + status + "]";
    }

}

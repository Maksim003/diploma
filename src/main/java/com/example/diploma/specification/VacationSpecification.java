package com.example.diploma.specification;

import com.example.diploma.entity.VacationEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class VacationSpecification {

    public Specification<VacationEntity> getSpecification(Long departmentId, String monthYear) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (departmentId != null) {
                predicates.add(cb.equal(
                        root.get("user").get("department").get("id"),
                        departmentId
                ));
            }
            if (monthYear != null && !monthYear.isEmpty()) {
                String[] parts = monthYear.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);

                Expression<Integer> startYear = cb.function(
                        "DATE_PART", Integer.class,
                        cb.literal("year"),
                        root.get("startDate")
                );
                Expression<Integer> startMonth = cb.function(
                        "DATE_PART", Integer.class,
                        cb.literal("month"),
                        root.get("startDate")
                );

                Expression<Integer> endYear = cb.function(
                        "DATE_PART", Integer.class,
                        cb.literal("year"),
                        root.get("endDate")
                );
                Expression<Integer> endMonth = cb.function(
                        "DATE_PART", Integer.class,
                        cb.literal("month"),
                        root.get("endDate")
                );

                LocalDate firstDay = LocalDate.of(year, month, 1);
                LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

                Predicate monthPredicate = cb.or(
                        cb.and(
                                cb.equal(startYear, year),
                                cb.equal(startMonth, month)
                        ),
                        cb.and(
                                cb.equal(endYear, year),
                                cb.equal(endMonth, month)
                        ),
                        cb.and(
                                cb.lessThanOrEqualTo(root.get("startDate"), firstDay),
                                cb.greaterThanOrEqualTo(root.get("endDate"), lastDay)
                        )
                );

                predicates.add(monthPredicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
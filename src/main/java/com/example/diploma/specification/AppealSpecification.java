package com.example.diploma.specification;

import com.example.diploma.entity.AppealEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppealSpecification {

    public Specification<AppealEntity> getSpecification(Long departmentId, String monthYear) {
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
                Expression<Integer> yearExpr = cb.function(
                        "DATE_PART", Integer.class,
                        cb.literal("year"),
                        root.get("createdAt")
                );
                Expression<Integer> monthExpr = cb.function(
                        "DATE_PART", Integer.class,
                        cb.literal("month"),
                        root.get("createdAt")
                );
                predicates.add(cb.and(
                        cb.equal(yearExpr, year),
                        cb.equal(monthExpr, month)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
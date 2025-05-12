package com.example.diploma.specification;

import com.example.diploma.entity.AppealEntity;
import com.example.diploma.entity.DepartmentEntity;
import com.example.diploma.entity.IncidentEntity;
import com.example.diploma.entity.UserEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IncidentSpecification {

    public Specification<IncidentEntity> getSpecification(Long departmentId, String monthYear) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
//
//            if (departmentId != null) {
//                predicates.add(cb.equal(root.get("users").get("department").get("id"), departmentId));
//            }

            if (departmentId != null) {
                Subquery<Long> incidentSubquery = query.subquery(Long.class);
                Root<IncidentEntity> incidentRoot = incidentSubquery.from(IncidentEntity.class);
                Join<IncidentEntity, UserEntity> userJoin = incidentRoot.join("users");
                Join<UserEntity, DepartmentEntity> departmentJoin = userJoin.join("department");
                incidentSubquery.select(incidentRoot.get("id"))
                        .where(
                                cb.equal(departmentJoin.get("id"), departmentId),
                                cb.equal(incidentRoot.get("id"), root.get("id"))
                        );

                predicates.add(cb.exists(incidentSubquery));
            }

            if (monthYear != null && !monthYear.isEmpty()) {
                String[] parts = monthYear.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                Expression<Integer> yearExpr = cb.function(
                        "DATE_PART", Integer.class,
                        cb.literal("year"),
                        root.get("date")
                );
                Expression<Integer> monthExpr = cb.function(
                        "DATE_PART", Integer.class,
                        cb.literal("month"),
                        root.get("date")
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

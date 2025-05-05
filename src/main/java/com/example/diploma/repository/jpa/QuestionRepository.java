package com.example.diploma.repository.jpa;

import com.example.diploma.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository  extends JpaRepository<QuestionEntity, Long> {

    boolean existsByName(String name);

}

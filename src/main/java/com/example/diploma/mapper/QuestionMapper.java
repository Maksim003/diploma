package com.example.diploma.mapper;

import com.example.diploma.controller.request.question.CreateQuestionRequest;
import com.example.diploma.controller.request.question.UpdateQuestionRequest;
import com.example.diploma.controller.response.QuestionResponse;
import com.example.diploma.entity.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionMapper {

    private final AnswerMapper answerMapper;

    public QuestionEntity toEntity(CreateQuestionRequest createQuestion) {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setName(createQuestion.name());
        return questionEntity;
    }

    public void updateEntity(QuestionEntity questionEntity, UpdateQuestionRequest updateQuestion) {
        questionEntity.setName(updateQuestion.name());
    }

    public QuestionResponse toResponse(QuestionEntity questionEntity) {
        return new QuestionResponse(
                questionEntity.getId(),
                questionEntity.getName(),
                questionEntity.getAnswers().stream()
                        .map(answerMapper::toResponse).toList()
        );
    }

}

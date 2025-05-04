package com.example.diploma.mapper;

import com.example.diploma.controller.request.answer.CreateAnswerRequest;
import com.example.diploma.controller.request.answer.UpdateAnswerRequest;
import com.example.diploma.controller.response.AnswerResponse;
import com.example.diploma.entity.AnswerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerMapper {

    public AnswerEntity toEntity(CreateAnswerRequest createAnswer) {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setName(createAnswer.name());
        answerEntity.setIsCorrect(createAnswer.isCorrect());
        return answerEntity;
    }

    public void updateEntity(AnswerEntity answerEntity, UpdateAnswerRequest updateAnswer) {
        answerEntity.setName(updateAnswer.name());
        answerEntity.setIsCorrect(updateAnswer.isCorrect());
    }

    public AnswerResponse toResponse(AnswerEntity answerEntity) {
        return new AnswerResponse(
                answerEntity.getId(),
                answerEntity.getName(),
                answerEntity.getIsCorrect()
        );
    }

}

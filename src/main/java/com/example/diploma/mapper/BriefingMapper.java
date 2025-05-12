package com.example.diploma.mapper;

import com.example.diploma.controller.request.answer.CreateAnswerRequest;
import com.example.diploma.controller.request.briefing.CreateBriefingRequest;
import com.example.diploma.controller.request.briefing.UpdateBriefingRequest;
import com.example.diploma.controller.request.question.CreateQuestionRequest;
import com.example.diploma.controller.response.BriefingResponse;
import com.example.diploma.controller.response.ShortBriefingResponse;
import com.example.diploma.entity.AnswerEntity;
import com.example.diploma.entity.BriefingEntity;
import com.example.diploma.entity.QuestionEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BriefingMapper {

    private final FullnameMapper fullnameMapper;
    private final QuestionMapper questionMapper;

//    public BriefingEntity toEntity(CreateBriefingRequest createBriefing) {
//        BriefingEntity briefingEntity = new BriefingEntity();
//        briefingEntity.setType(createBriefing.type());
//        UserEntity userEntity = new UserEntity(createBriefing.creator());
//        briefingEntity.setCreator(userEntity);
//        return briefingEntity;
//    }

    public BriefingEntity toEntity(CreateBriefingRequest createBriefing) {
        BriefingEntity briefingEntity = new BriefingEntity();
        briefingEntity.setType(createBriefing.type());
        briefingEntity.setCreator(new UserEntity(createBriefing.creator()));

        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (CreateQuestionRequest questionReq : createBriefing.questions()) {
            QuestionEntity question = new QuestionEntity();
            question.setName(questionReq.name());

            if (question.getBriefings() == null) {
                question.setBriefings(new ArrayList<>());
            }
            question.getBriefings().add(briefingEntity);

            List<AnswerEntity> answerEntities = new ArrayList<>();
            for (CreateAnswerRequest answerReq : questionReq.answers()) {
                AnswerEntity answer = new AnswerEntity();
                answer.setName(answerReq.name());
                answer.setIsCorrect(answerReq.isCorrect());

                if (answer.getQuestions() == null) {
                    answer.setQuestions(new ArrayList<>());
                }
                answer.getQuestions().add(question);

                answerEntities.add(answer);
            }

            question.setAnswers(answerEntities);
            questionEntities.add(question);
        }

        briefingEntity.setQuestions(questionEntities);
        return briefingEntity;
    }



    public void updateEntity(BriefingEntity briefingEntity, UpdateBriefingRequest updateBriefing) {
        briefingEntity.setType(updateBriefing.type());
    }

    public ShortBriefingResponse toShortResponse(BriefingEntity briefingEntity) {
        return new ShortBriefingResponse(
                briefingEntity.getId(),
                briefingEntity.getType()
        );
    }

    public BriefingResponse toResponse(BriefingEntity briefingEntity) {
        return new BriefingResponse(
                briefingEntity.getId(),
                fullnameMapper.toResponse(briefingEntity.getCreator()),
                briefingEntity.getType(),
                briefingEntity.getQuestions().stream()
                        .map(questionMapper::toResponse).toList()
        );
    }

}

package com.example.diploma.mapper;

import com.example.diploma.controller.request.vacation.CreateVacationRequest;
import com.example.diploma.controller.request.vacation.UpdateVacationRequest;
import com.example.diploma.controller.response.VacationResponse;
import com.example.diploma.entity.UserEntity;
import com.example.diploma.entity.VacationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VacationMapper {

    private final FullnameMapper fullnameMapper;

    public VacationEntity toEntity(CreateVacationRequest createVacation) {
        VacationEntity vacationEntity = new VacationEntity();
        vacationEntity.setStartDate(createVacation.startDate());
        vacationEntity.setEndDate(createVacation.endDate());
        vacationEntity.setType(createVacation.type());
        UserEntity userEntity = new UserEntity(createVacation.user());
        vacationEntity.setUser(userEntity);
        return vacationEntity;
    }

    public void updateEntity(VacationEntity vacationEntity, UpdateVacationRequest updateVacation) {
        vacationEntity.setStartDate(updateVacation.startDate());
        vacationEntity.setEndDate(updateVacation.endDate());
        vacationEntity.setType(updateVacation.type());
    }

    public VacationResponse toResponse(VacationEntity vacationEntity) {
        return new VacationResponse(
                vacationEntity.getId(),
                fullnameMapper.toResponse(vacationEntity.getUser()),
                vacationEntity.getStartDate(),
                vacationEntity.getEndDate(),
                vacationEntity.getType()
        );
    }

}

package com.example.diploma.mapper;

import com.example.diploma.controller.request.incident.CreateIncidentRequest;
import com.example.diploma.controller.request.incident.UpdateIncidentRequest;
import com.example.diploma.controller.response.IncidentResponse;
import com.example.diploma.entity.IncidentEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncidentMapper {

    private final FullnameMapper fullnameMapper;

    public IncidentEntity toEntity(CreateIncidentRequest createIncident) {
        IncidentEntity incidentEntity = new IncidentEntity();
        incidentEntity.setDate(createIncident.date());
        incidentEntity.setDescription(createIncident.description());
        incidentEntity.setType(createIncident.type());
        incidentEntity.setStatus(createIncident.status());
        UserEntity userEntity = new UserEntity(createIncident.user());
        incidentEntity.setUser(userEntity);
        return incidentEntity;
    }

    public void updateEntity(IncidentEntity incidentEntity, UpdateIncidentRequest updateIncident) {
        incidentEntity.setDate(updateIncident.date());
        incidentEntity.setDescription(updateIncident.description());
        incidentEntity.setType(updateIncident.type());
        incidentEntity.setStatus(updateIncident.status());
    }

    public IncidentResponse toResponse(IncidentEntity incidentEntity) {
        return new IncidentResponse(
                incidentEntity.getId(),
                fullnameMapper.toResponse(incidentEntity.getUser()),
                incidentEntity.getDate(),
                incidentEntity.getType(),
                incidentEntity.getDescription(),
                incidentEntity.getStatus()
        );
    }

}

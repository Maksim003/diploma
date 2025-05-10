package com.example.diploma.mapper;

import com.example.diploma.controller.request.incident.CreateIncidentRequest;
import com.example.diploma.controller.request.incident.UpdateIncidentRequest;
import com.example.diploma.controller.response.IncidentResponse;
import com.example.diploma.entity.IncidentEntity;
import com.example.diploma.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IncidentMapper {

    private final FullnameMapper fullnameMapper;

    public IncidentEntity toEntity(CreateIncidentRequest createIncident) {
        IncidentEntity incidentEntity = new IncidentEntity();
        incidentEntity.setDate(createIncident.date());
        incidentEntity.setDescription(createIncident.description());
        incidentEntity.setType(createIncident.type());
        incidentEntity.setActions(createIncident.actions());
        List<UserEntity> users = createIncident.users().stream()
                .map(UserEntity::new).toList();
        incidentEntity.setUsers(users);
        return incidentEntity;
    }

    public void updateEntity(IncidentEntity incidentEntity, UpdateIncidentRequest updateIncident) {
        incidentEntity.setDate(updateIncident.date());
        incidentEntity.setDescription(updateIncident.description());
        incidentEntity.setType(updateIncident.type());
        incidentEntity.setActions(updateIncident.actions());
    }

    public IncidentResponse toResponse(IncidentEntity incidentEntity) {
        return new IncidentResponse(
                incidentEntity.getId(),
                incidentEntity.getUsers().stream()
                        .map(fullnameMapper::toResponse)
                        .toList(),
                incidentEntity.getDate(),
                incidentEntity.getType(),
                incidentEntity.getDescription(),
                incidentEntity.getActions()
        );
    }

}

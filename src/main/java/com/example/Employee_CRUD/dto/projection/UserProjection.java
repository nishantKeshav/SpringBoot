package com.example.Employee_CRUD.dto.projection;

import org.springframework.data.web.ProjectedPayload;

@ProjectedPayload
public interface UserProjection {
    String getUserId();
    String getWard();
    String getBlock();
    String getVillage();
    String getEmailId();
    String getDistrict();
    String getFullName();
    String getMunicipality();
    String getMobileNumber();
}


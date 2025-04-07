package com.example.Employee_CRUD.dto.projection;

import org.springframework.data.web.ProjectedPayload;

@ProjectedPayload
public interface BlocksProjection {
    String getBlock();
}

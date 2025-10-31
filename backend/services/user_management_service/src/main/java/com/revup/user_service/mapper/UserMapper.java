package com.revup.user_service.mapper;

import com.revup.user_service.model.dto.CreateUserRequest;
import com.revup.user_service.model.dto.UpdateUserRequest;
import com.revup.user_service.model.dto.UserResponse;
import com.revup.user_service.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    
    User toEntity(CreateUserRequest request);
    
    UserResponse toResponse(User user);
    
    void updateEntityFromRequest(UpdateUserRequest request, @MappingTarget User user);
}

package com.revup.user_service.mapper;

import com.revup.user_service.model.dto.CreateUserRequest;
import com.revup.user_service.model.dto.UpdateUserRequest;
import com.revup.user_service.model.dto.UserResponse;
import com.revup.user_service.model.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T12:55:08+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.44.0.v20251001-1143, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(CreateUserRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.address( request.getAddress() );
        user.city( request.getCity() );
        user.country( request.getCountry() );
        user.email( request.getEmail() );
        user.firebaseUid( request.getFirebaseUid() );
        user.firstName( request.getFirstName() );
        user.lastName( request.getLastName() );
        user.phone( request.getPhone() );
        user.role( request.getRole() );
        user.state( request.getState() );
        user.zipCode( request.getZipCode() );

        return user.build();
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.address( user.getAddress() );
        userResponse.city( user.getCity() );
        userResponse.country( user.getCountry() );
        userResponse.createdAt( user.getCreatedAt() );
        userResponse.email( user.getEmail() );
        userResponse.firebaseUid( user.getFirebaseUid() );
        userResponse.firstName( user.getFirstName() );
        userResponse.id( user.getId() );
        userResponse.lastName( user.getLastName() );
        userResponse.phone( user.getPhone() );
        userResponse.profileImageUrl( user.getProfileImageUrl() );
        userResponse.role( user.getRole() );
        userResponse.state( user.getState() );
        userResponse.status( user.getStatus() );
        userResponse.updatedAt( user.getUpdatedAt() );
        userResponse.zipCode( user.getZipCode() );

        return userResponse.build();
    }

    @Override
    public void updateEntityFromRequest(UpdateUserRequest request, User user) {
        if ( request == null ) {
            return;
        }

        if ( request.getAddress() != null ) {
            user.setAddress( request.getAddress() );
        }
        if ( request.getCity() != null ) {
            user.setCity( request.getCity() );
        }
        if ( request.getCountry() != null ) {
            user.setCountry( request.getCountry() );
        }
        if ( request.getFirstName() != null ) {
            user.setFirstName( request.getFirstName() );
        }
        if ( request.getLastName() != null ) {
            user.setLastName( request.getLastName() );
        }
        if ( request.getPhone() != null ) {
            user.setPhone( request.getPhone() );
        }
        if ( request.getProfileImageUrl() != null ) {
            user.setProfileImageUrl( request.getProfileImageUrl() );
        }
        if ( request.getRole() != null ) {
            user.setRole( request.getRole() );
        }
        if ( request.getState() != null ) {
            user.setState( request.getState() );
        }
        if ( request.getZipCode() != null ) {
            user.setZipCode( request.getZipCode() );
        }
    }
}

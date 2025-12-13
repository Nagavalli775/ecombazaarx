package com.ecobazaarx.backend.mapper;

import com.ecobazaarx.backend.dto.UserProfileDTO;
import com.ecobazaarx.backend.dto.UserSafeDTO;
import com.ecobazaarx.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserSafeDTO toSafeDTO(User user) {
        return UserSafeDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .ecoScore(user.getEcoScore())
                .build();
    }

    public UserProfileDTO toProfileDTO(User user) {
        return UserProfileDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isPhoneVerified(Boolean.TRUE.equals(user.getIsPhoneVerified()))
                .isEmailVerified(Boolean.TRUE.equals(user.getIsEmailVerified()))
                .ecoScore(user.getEcoScore())
                .build();
    }
}

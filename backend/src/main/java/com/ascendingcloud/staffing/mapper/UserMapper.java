package com.ascendingcloud.staffing.mapper;

import com.ascendingcloud.staffing.dto.UserDTO;
import com.ascendingcloud.staffing.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
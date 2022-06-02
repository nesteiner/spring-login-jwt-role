package com.example.springloginjwtrole.model;

import lombok.Data;

@Data
public class UserDto {
    String name;
    String password;
    String email;
    String phone;
    String nickname;
    String businessTitle;

    public User getUserFromDto() {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setNickname(nickname);
        user.setBusinessTitle(businessTitle);

        return user;
    }
}

package com.example.demo.controller.DTO;

import com.example.demo.repository.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserDTO {
    private long id;
    private String account;
    private String username;

    public UserDTO(long id, String account, String username) {
        this.id = id;
        this.account = account;
        this.username = username;
    }

    protected UserDTO(UserEntity user){
        this(user.getId(),
                user.getAccount(),
                user.getUsername());
    }

    public static UserDTO createByEntity(UserEntity user){
        return new UserDTO(user.getId(), user.getAccount(), user.getUsername());
    }



}

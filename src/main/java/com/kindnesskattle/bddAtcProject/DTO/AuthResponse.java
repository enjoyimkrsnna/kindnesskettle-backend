package com.kindnesskattle.bddAtcProject.DTO;

import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private UserAccount userAccount;


}

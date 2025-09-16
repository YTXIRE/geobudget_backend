package com.geobudget.geobudget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String phone;
    private String city;
    private String country;
    private Role role;
}

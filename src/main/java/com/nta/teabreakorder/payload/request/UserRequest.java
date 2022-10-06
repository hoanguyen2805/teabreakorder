package com.nta.teabreakorder.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {


    private Long id;

    private String username;

    private String email;


    private String fullName;

    private String img;

}

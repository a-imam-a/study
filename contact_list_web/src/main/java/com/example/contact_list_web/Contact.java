package com.example.contact_list_web;

import lombok.Data;

@Data
public class Contact {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}

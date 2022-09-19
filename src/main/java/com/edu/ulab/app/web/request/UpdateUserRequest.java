package com.edu.ulab.app.web.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
	private String fullName;
    private String title;
    private int age;
    private Long id;
}

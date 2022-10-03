package com.edu.ulab.app.web.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailedUserBookResponse {
	private Long id;
    private String fullName;
    private String title;
    private int age;
    private String resume;
    List<DetailedBookResponse> bookResponceList;
}

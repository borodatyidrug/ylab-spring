package com.edu.ulab.app.dto;

import java.util.List;

import com.edu.ulab.app.storage.Idable;

import lombok.Data;

@Data
public class UserDto implements Idable<Long> {
    private Long id;
    private String fullName;
    private String title;
    private int age;
    private List<Long> booksId;
}

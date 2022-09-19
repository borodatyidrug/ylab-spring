package com.edu.ulab.app.dto;

import com.edu.ulab.app.storage.Idable;

import lombok.Data;

@Data
public class BookDto implements Idable<Long> {
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private long pageCount;
}

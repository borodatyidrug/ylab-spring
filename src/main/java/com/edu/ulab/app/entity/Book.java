package com.edu.ulab.app.entity;

import com.edu.ulab.app.storage.Idable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book implements Idable<Long> {
	private Long id;
	private Long userId;
	private String author;
	private String title;
	private long pageCount;
}

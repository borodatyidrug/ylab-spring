package com.edu.ulab.app.entity;

import java.util.List;

import com.edu.ulab.app.storage.Idable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User implements Idable<Long> {
	private Long id;
	private String fullName;
	private String title;
	private int age;
	private List<Long> booksId;
}

package com.edu.ulab.app.web.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdateUserBookRequest {
	private Long userId;
    private List<Long> bookIdList;
}

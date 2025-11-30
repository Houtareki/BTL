package com.example.demo.Model.CommonModel;

import org.springframework.data.domain.Sort;

public enum SortType {

	NEWEST("movieId", Sort.Direction.DESC), MOST_VIEWED("views", Sort.Direction.DESC),
	HIGHEST_RATED("views", Sort.Direction.DESC);

	private final String fieldName;
	private final Sort.Direction direction;

	SortType(String fieldName, Sort.Direction direction) {

		this.fieldName = fieldName;
		this.direction = direction;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Sort.Direction getDirection() {

		return direction;
	}

	public Sort toSort() {
		return Sort.by(direction, fieldName);
	}

	// Parse "newest" -> NEWEST
	public static SortType fromString(String value) {
		if (value == null)
			return NEWEST;
		try {
			return SortType.valueOf(value.toUpperCase());
		} catch (Exception e) {
			return NEWEST;
		}
	}
}
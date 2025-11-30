package com.example.demo.Service.Strategy;

import org.springframework.stereotype.Component;

import com.example.demo.Model.CommonModel.SortType;

// ===== NEWEST =====
@Component
public class NewestSortStrategy implements MovieSortStrategy {
	@Override
	public SortType getSortType() {
		return SortType.NEWEST;
	}
}

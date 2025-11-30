package com.example.demo.Service.Strategy;

import org.springframework.stereotype.Component;

import com.example.demo.Model.CommonModel.SortType;

@Component
public class HighestRatedSortStrategy implements MovieSortStrategy {
	@Override
	public SortType getSortType() {
		return SortType.HIGHEST_RATED;
	}
}

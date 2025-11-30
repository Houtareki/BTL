package com.example.demo.Service.Strategy;

import org.springframework.stereotype.Component;

import com.example.demo.Model.CommonModel.SortType;

@Component
public class MostViewedSortStrategy implements MovieSortStrategy {
	@Override
	public SortType getSortType() {
		return SortType.MOST_VIEWED;
	}
}

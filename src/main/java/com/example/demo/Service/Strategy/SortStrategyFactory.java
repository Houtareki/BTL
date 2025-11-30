package com.example.demo.Service.Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.Model.CommonModel.SortType;

/**
 * FACTORY PATTERN Quản lý và cung cấp Strategy objects
 */
@Component
public class SortStrategyFactory {

	private final Map<SortType, MovieSortStrategy> strategies;

	@Autowired
	public SortStrategyFactory(List<MovieSortStrategy> strategyList) {
		Map<SortType, MovieSortStrategy> map = new HashMap<>();

		for (MovieSortStrategy strategy : strategyList) {
			// Lấy kiểu sắp xếp (SortType) làm key
			SortType key = strategy.getSortType();

			// Thêm cặp key-value vào Map
			map.put(key, strategy);
		}

		// Gán Map đã tạo cho trường strategies
		this.strategies = map;
	}

	// Lấy chiến lược dựa trên kiểu sắp xếp
	public MovieSortStrategy getStrategy(SortType sortType) {
		return strategies.getOrDefault(sortType, strategies.get(SortType.NEWEST));
	}
}
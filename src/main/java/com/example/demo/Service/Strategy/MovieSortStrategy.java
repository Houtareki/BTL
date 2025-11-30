package com.example.demo.Service.Strategy;

import org.springframework.data.domain.Sort;

import com.example.demo.Model.CommonModel.SortType;

//Định nghĩa khung cho các logic sắp xếp phim
public interface MovieSortStrategy {
	// Loại sắp xếp
	SortType getSortType();

	// Lấy đối tượng Sort tương ứng với loại sắp xếp
	default Sort getSort() {
		return getSortType().toSort();
	}
}
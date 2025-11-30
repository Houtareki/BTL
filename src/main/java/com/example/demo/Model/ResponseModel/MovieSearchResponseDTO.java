package com.example.demo.Model.ResponseModel;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.Entity.Movie;


public class MovieSearchResponseDTO {

	// Data
	private List<Movie> content;

	// Pagination
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean hasNext;
	private boolean hasPrevious;

	// Sort info
	private String sortBy;

	public MovieSearchResponseDTO() {
	}

	public static MovieSearchResponseDTO of(Page<Movie> pageData, String sortBy) {
		MovieSearchResponseDTO dto = new MovieSearchResponseDTO();
		dto.setContent(pageData.getContent());
		dto.setPage(pageData.getNumber());
		dto.setSize(pageData.getSize());
		dto.setTotalElements(pageData.getTotalElements());
		dto.setTotalPages(pageData.getTotalPages());
		dto.setHasNext(pageData.hasNext());
		dto.setHasPrevious(pageData.hasPrevious());
		dto.setSortBy(sortBy);
		return dto;
	}

	public List<Movie> getContent() {
		return content;
	}

	public void setContent(List<Movie> content) {
		this.content = content;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}

	public void setHasPrevious(boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
}

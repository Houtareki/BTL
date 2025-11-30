package com.example.demo.Model.RequestModel;

public class MovieSearchRequestDTO {

	// Tìm kiếm
	private String keyword;

	// Lọc
	private Integer genreId;
	private Integer releaseYear;
	private String country;

	// Sắp xếp
	private String sortBy; // newest, most_viewed, highest_rated

	// Phân trang
	private int page = 0;
	private int size = 12;

	public MovieSearchRequestDTO() {
	}

	// Getters and Setters với validation
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword != null ? keyword.trim() : null;
	}

	public Integer getGenreId() {
		return genreId;
	}

	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSortBy() {
		return sortBy != null ? sortBy : "newest";
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = Math.max(0, page);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size > 0 && size <= 100 ? size : 12;
	}

	// Business logic - kiểm tra có filter không
	public boolean hasFilters() {
		return keyword != null || genreId != null || releaseYear != null || country != null;
	}

	@Override
	public String toString() {
		return "MovieSearchDTO{" + "keyword='" + keyword + '\'' + ", genreId=" + genreId + ", releaseYear="
				+ releaseYear + ", country='" + country + '\'' + ", sortBy='" + sortBy + '\'' + ", page=" + page
				+ ", size=" + size + '}';
	}
}
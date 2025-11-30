package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.Genre;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Service.MovieService;

/**
 * Controller for Filter Options Provides data for frontend dropdowns
 * OOP Principles: - SINGLE RESPONSIBILITY: Chỉ lo filter options - SEPARATION
 * OF CONCERNS: Tách riêng khỏi search logic
 */
@Controller
@RequestMapping("/api/movies/filters")
public class FilterOptionsController {

	@Autowired
	private MovieService movieService;

	// Lấy dữ liệu tất cả các thể loại phim hiện có
	@GetMapping("/get-genres")
	public ResponseEntity<?> getGenres(@RequestParam(name = "userId") int userId) {
		CustomData<List<Genre>> data = new CustomData<>();
		CustomResponse<List<Genre>> response = new CustomResponse<>();
		List<Genre> genres = this.movieService.getAllGenres(userId);
		if (genres != null && !genres.isEmpty()) {
			data.setData(genres);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Lấy dữ liệu tất cả các quốc gia sản xuất phim hiện có
	@GetMapping("/get-countries")
	public ResponseEntity<?> getCountries(@RequestParam(name = "userId") int userId) {
		CustomData<List<String>> data = new CustomData<>();
		CustomResponse<List<String>> response = new CustomResponse<>();
		List<String> countries = this.movieService.getAllCountries(userId);
		if (countries != null && !countries.isEmpty()) {
			data.setData(countries);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Lấy dữ liệu tất cả các năm phát hành phim hiện có
	@GetMapping("/get-years")
	public ResponseEntity<?> getYears(@RequestParam(name = "userId") int userId) {
		CustomData<List<Integer>> data = new CustomData<>();
		CustomResponse<List<Integer>> response = new CustomResponse<>();
		List<Integer> years = this.movieService.getAllYears(userId);
		if (years != null && !years.isEmpty()) {
			data.setData(years);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}
}
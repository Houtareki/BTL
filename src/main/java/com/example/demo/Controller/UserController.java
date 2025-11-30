package com.example.demo.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Episode;
import com.example.demo.Entity.Genre;
import com.example.demo.Entity.Movie;
import com.example.demo.Entity.User;

import com.example.demo.Model.CommonModel.CommentForm;
import com.example.demo.Model.CommonModel.State;
import com.example.demo.Model.ResponseModel.CommentData;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Model.ResponseModel.MovieInteractionData;
import com.example.demo.Service.MovieService;
import com.example.demo.Service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private MovieService movieService;

	// Tải trang đăng nhập
	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}

	// Tải trang đăng ký
	@GetMapping("/register")
	public String getRegisterPage() {
		return "register";
	}

	// Tải trang điền email để được gửi email đặt lại mật khẩu
	@GetMapping("/forgot_password")
	public String getForgotPasswordPage() {
		return "forgot_password";
	}

	// Tải trang đặt lại mật khẩu
	@GetMapping("/reset-password")
	public String getResetPasswordPage() {
		return "reset-password";
	}

	// Tải trang chọn phim theo thể loại
	@GetMapping("/movies-of-genre")
	public String getMoiesOfGenrePage(@RequestParam(name = "userId") int userId) {
		User user = this.userService.findByUserId(userId);
		if (user != null) {
			if (user.getState() != State.ACTIVE) {
				return "error";
			}
			return "genre";
		}
		return "error";
	}

	// Tải trang chọn phim theo quốc gia
	@GetMapping("/movies-of-country")
	public String getMoviesOfCountryPage(@RequestParam(name = "userId") int userId) {
		User user = this.userService.findByUserId(userId);
		if (user != null) {
			if (user.getState() != State.ACTIVE) {
				return "error";
			}
			return "country";
		}
		return "error";
	}

	// Tải trang chủ
	@GetMapping("/home")
	public String getHomePage(@RequestParam(name = "userId") int userId) {
		User user = this.userService.findByUserId(userId);
		if (user != null) {
			if (user.getState() != State.ACTIVE) {
				return "error";
			}
			return "home";
		}
		return "error";
	}

	// Tải trang chứa thông tin người dùng
	@GetMapping("/personal")
	public String getUserPage(@RequestParam(name = "userId") int userId) {
		User user = this.userService.findByUserId(userId);
		if (user != null) {
			if (user.getState() != State.ACTIVE) {
				return "error";
			}
			return "user";
		}
		return "error";
	}

	// Lấy dữ liệu tất cả bộ phim trong banner
	@GetMapping("/get-banner")
	public ResponseEntity<?> getBannerMovies() {
		CustomData<List<Movie>> data = new CustomData<>();
		CustomResponse<List<Movie>> response = new CustomResponse<>();
		List<Movie> bannerMovies = this.movieService.getBannerMovies();
		if (bannerMovies != null && !bannerMovies.isEmpty()) {
			data.setData(bannerMovies);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Lấy dữ liệu một bộ phim theo id của nó
	@GetMapping("/get-movie")
	public ResponseEntity<?> getMovieById(@RequestParam(name = "movieId") int movieId) {
		CustomData<Movie> data = new CustomData<>();
		CustomResponse<Movie> response = new CustomResponse<>();
		Movie movie = this.movieService.getMovieByMovieId(movieId);
		if (movie != null) {
			data.setData(movie);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Lấy dư liệu tất cả tập phim theo id của bộ phim
	@GetMapping("/get-episodes")
	public ResponseEntity<?> getAllEpisodeByMovieId(@RequestParam(name = "movieId") int movieId) {
		CustomData<List<Episode>> data = new CustomData<>();
		CustomResponse<List<Episode>> response = new CustomResponse<>();
		List<Episode> episodes = this.movieService.findAllEpisodeByMovieId(movieId);
		if (episodes != null && !episodes.isEmpty()) {
			data.setData(episodes);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Lấy dữ liệu tương tác của người dùng với bộ phim
	@GetMapping("/get-movie-interaction")
	public ResponseEntity<?> getMovieInteractionInfor(@RequestParam(name = "userId") int userId,
			@RequestParam(name = "movieId") int movieId) {
		CustomData<MovieInteractionData> data = new CustomData<>();
		CustomResponse<MovieInteractionData> response = new CustomResponse<>();
		MovieInteractionData movieInteractionData = this.movieService.getMovieInteractionData(userId, movieId);
		if (movieInteractionData != null) {
			data.setData(movieInteractionData);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Lấy dữ liệu các bộ phim đang chiếu
	@GetMapping("/get-showing-movies")
	public ResponseEntity<?> getShowingMovies(@RequestParam(name = "userId") int userId) {
		CustomData<List<Movie>> data = new CustomData<>();
		CustomResponse<List<Movie>> response = new CustomResponse<>();
		List<Movie> movieInteractionData = this.movieService.getShowingMovies(userId);
		if (movieInteractionData != null) {
			data.setData(movieInteractionData);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Lấy dữ liệu các bộ phim được gợi ý cho người dùng
	@GetMapping("/get-suggested-movies")
	public ResponseEntity<?> getSuggestedMovies(@RequestParam(name = "userId") int userId) {
		CustomData<List<Movie>> data = new CustomData<>();
		CustomResponse<List<Movie>> response = new CustomResponse<>();
		List<Movie> suggestedMovies = this.movieService.getSuggestedMovies(userId);
		if (suggestedMovies != null && !suggestedMovies.isEmpty()) {
			data.setData(suggestedMovies);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

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

	// Lấy dữ liệu tất cả các quốc gia snar xuất phim hiện có
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

	// Lấy dữ liệu các bình luận của một bộ phim theo id của phim đó
	@GetMapping("/get-comments-by-movieId")
	public ResponseEntity<?> getCommentsByMovieId(@RequestParam(name = "userId") int userId,
			@RequestParam(name = "movieId") int movieId) {
		CustomData<List<CommentData>> data = new CustomData<>();
		CustomResponse<List<CommentData>> response = new CustomResponse<>();
		List<CommentData> comments = this.movieService.getAllCommentsByMovieId(userId, movieId);
		if (comments != null && !comments.isEmpty()) {
			data.setData(comments);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy dữ liệu thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy dữ liệu thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Thêm bình luận cho một bộ phim
	@PostMapping("/add-comment")
	public ResponseEntity<?> addComment(@RequestBody CommentForm commentForm) {
		CustomResponse<Comment> response = new CustomResponse<>();
		Comment comments = this.movieService.addComment(commentForm);
		if (comments != null) {
			response.setStatus("Success");
			response.setMessage("Thêm comment thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Thêm comment thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Cập nhật dữ liệu like của người dùng cho một bộ phim
	@PutMapping("/interaction-like")
	public ResponseEntity<?> updateLikedRepo(@RequestParam(name = "userId") int userId,
			@RequestParam(name = "movieId") int movieId) {
		CustomData<User> data = new CustomData<>();
		CustomResponse<User> response = new CustomResponse<>();
		User user = this.userService.updateUserFavoritedRepo(userId, movieId);
		if (user != null) {
			data.setData(user);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Like thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Like thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Cập nhật dữ liệu đã lưu của người dùng cho một bộ phim
	@PutMapping("/interaction-save")
	public ResponseEntity<?> updateSavedRepo(@RequestParam(name = "userId") int userId,
			@RequestParam(name = "movieId") int movieId) {
		CustomData<User> data = new CustomData<>();
		CustomResponse<User> response = new CustomResponse<>();
		User user = this.userService.updateUserSavedRepo(userId, movieId);
		if (user != null) {
			data.setData(user);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Save thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Save thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Cập nhật dữ liệu lượt xem của người dùng cho một bộ phim
	@PutMapping("/interaction-watch")
	public ResponseEntity<?> updateMovieViews(@RequestParam(name = "userId") int userId,
			@RequestParam(name = "movieId") int movieId) {
		CustomData<Movie> data = new CustomData<>();
		CustomResponse<Movie> response = new CustomResponse<>();
		Movie movie = this.movieService.updateMovieViews(userId, movieId);
		if (movie != null) {
			data.setData(movie);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Update view thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Update view thất bại!");
		}
		return ResponseEntity.ok(response);
	}

	// Lấy dữ liệu tất cả phim theo thể loại
	@GetMapping("/get-all-movies-by-genreId")
	public ResponseEntity<?> getAllMoviesByGenreId(@RequestParam(name = "userId") int userId,
			@RequestParam(name = "genreId") int genreId) {
		CustomData<List<Movie>> data = new CustomData<>();
		CustomResponse<List<Movie>> response = new CustomResponse<>();
		List<Movie> movies = this.movieService.getAllMoviesByGenreId(userId, genreId);
		if (movies != null && !movies.isEmpty()) {
			data.setData(movies);
			response.setData(data);
			response.setStatus("Success");
			response.setMessage("Lấy phim thành công!");
		} else {
			response.setStatus("Error");
			response.setMessage("Lấy phim thất bại!");
		}
		return ResponseEntity.ok(response);
	}



    // Lấy dữ liệu tất cả phim theo quốc gia
    @GetMapping("/get-all-movies-by-country")
    public ResponseEntity<?> getAllMoviesByCountry(@RequestParam(name = "userId") int userId, @RequestParam(name = "country") String country) {
        CustomData<List<Movie>> data = new CustomData<>();
        CustomResponse<List<Movie>> response = new CustomResponse<>();
        country = country.replaceAll("_", " ");
        List<Movie> movies = this.movieService.getAllMoviesByCountry(userId, country);
        if(movies!=null && !movies.isEmpty()) {
            data.setData(movies);
            response.setData(data);
            response.setStatus("Success");  
            response.setMessage("Lấy phim thành công!");
        }
        else {
            response.setStatus("Error");
            response.setMessage("Lấy phim thất bại!");
        }
        return ResponseEntity.ok(response);
    }



    // Lấy danh sách phim đã like
    @GetMapping("/get-liked-movies-by-userId")
    public ResponseEntity<?> getLikedMoviesByUserId(@RequestParam(name = "userId") int userId) {
        CustomData<List<Movie>> data = new CustomData<>();
        CustomResponse<List<Movie>> response = new CustomResponse<>();
        List<Movie> movies = this.movieService.getLikedMoviesByUserId(userId);
        if(movies!=null && !movies.isEmpty()) {
            data.setData(movies);
            response.setData(data);
            response.setStatus("Success");
            response.setMessage("Lấy dữ liệu thành công!");
        }
        else {
            response.setStatus("Error");
            response.setMessage("Chưa có phim nào!");
        }
        return ResponseEntity.ok(response);
    }

    // Lấy danh sách phim đã save
    @GetMapping("/get-saved-movies-by-userId")
    public ResponseEntity<?> getSavedMoviesByUserId(@RequestParam(name = "userId") int userId) {
        CustomData<List<Movie>> data = new CustomData<>();
        CustomResponse<List<Movie>> response = new CustomResponse<>();
        List<Movie> movies = this.movieService.getSavedMoviesByUserId(userId);
        if(movies!=null && !movies.isEmpty()) {
            data.setData(movies);
            response.setData(data);
            response.setStatus("Success");
            response.setMessage("Lấy dữ liệu thành công!");
        }
        else {
            response.setStatus("Error");
            response.setMessage("Chưa có phim nào!");
        }
        return ResponseEntity.ok(response);
    }

    // Lấy danh sách phim đã xem
    @GetMapping("/get-watched-movies-by-userId")
    public ResponseEntity<?> getWatchedMoviesByUserId(@RequestParam(name = "userId") int userId) {
        CustomData<List<Movie>> data = new CustomData<>();
        CustomResponse<List<Movie>> response = new CustomResponse<>();
        List<Movie> movies = this.movieService.getWatchedMoviesByUserId(userId);
        if(movies!=null && !movies.isEmpty()) {
            data.setData(movies);
            response.setData(data);
            response.setStatus("Success");
            response.setMessage("Lấy dữ liệu thành công!");
        }
        else {
            response.setStatus("Error");
            response.setMessage("Chưa có phim nào!");
        }
        return ResponseEntity.ok(response);
    }

    // Lấy thông tin user
    @GetMapping("/get-user-info")
    public ResponseEntity<?> getUserInfo(@RequestParam(name = "userId") int userId) {
        CustomData<User> data = new CustomData<>();
        CustomResponse<User> response = new CustomResponse<>();
        User user = this.userService.findByUserId(userId);
        if(user!=null) {
            data.setData(user);
            response.setData(data);
            response.setStatus("Success");
            response.setMessage("Lấy thông tin thành công!");
        }
        else {
            response.setStatus("Error");
            response.setMessage("Không tìm thấy người dùng!");
        }
        return ResponseEntity.ok(response);
    }

    // Đổi mật khẩu (với xác thực mật khẩu cũ)
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam(name = "userId") int userId,
            @RequestBody Map<String, String> passwords
    ) {
        CustomResponse<User> response = new CustomResponse<>();

        String currentPassword = passwords.get("currentPassword");
        String newPassword = passwords.get("password");
        String confirmPassword = passwords.get("confirmPassword");

        User user = this.userService.findByUserId(userId);

        if(user == null) {
            response.setStatus("Error");
            response.setMessage("Người dùng không tồn tại!");
            return ResponseEntity.ok(response);
        }

        // Kiểm tra mật khẩu hiện tại
        if(!user.getPassword().equals(currentPassword)) {
            response.setStatus("Error");
            response.setMessage("Mật khẩu hiện tại không đúng!");
            return ResponseEntity.ok(response);
        }

        // Kiểm tra mật khẩu mới khớp nhau
        if(!newPassword.equals(confirmPassword)) {
            response.setStatus("Error");
            response.setMessage("Mật khẩu xác nhận không khớp!");
            return ResponseEntity.ok(response);
        }

        // Đổi mật khẩu
        User updatedUser = this.userService.changeUserPassword(userId, newPassword, confirmPassword);

        if(updatedUser != null) {
            response.setStatus("Success");
            response.setMessage("Đổi mật khẩu thành công!");
        } else {
            response.setStatus("Error");
            response.setMessage("Đổi mật khẩu thất bại!");
        }

        return ResponseEntity.ok(response);
    }

}




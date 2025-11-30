package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Model.CommonModel.EpisodeAndMovieTitle;
import com.example.demo.Model.CommonModel.UserForm;
import com.example.demo.Respository.*;
import com.example.demo.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.RequestModel.MovieUpdateRequestDTO;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Model.ResponseModel.MovieDetailResponseDTO;

import java.util.*;

@Controller 
@RequestMapping("/admin") 
public class AdminMovieController {

    @Autowired
    private MovieService  movieService;
    @Autowired
    private UserService userService;
    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private MovieActorRepo movieActorRepo;
    @Autowired
    private MovieDirectorRepo movieDirectorRepo;
    @Autowired
    private EpisodeRepo episodeRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private MovieServiceAdmin movieServiceAdmin;
    @Autowired
    private UserRepo userRepo;



    //---------------------------------------------------------------------

    private boolean isNotAdmin(int userId) {
        try {
            User user = userService.getUserById(userId);
            return user == null || user.getRole() != com.example.demo.Model.CommonModel.Role.ADMIN;
        }
        catch (Exception e) {
            return true;
        }
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.ok(
                new CustomResponse<>("Error", "Bạn không có quyền truy cập!", null)
        );
    }

    @GetMapping("/dashboard")
    public String getAdminDashboardPage() {
        return "admin";
    }

    @GetMapping("/stats")
    @ResponseBody
    public ResponseEntity<?> getDashboardStats(@RequestParam(name = "userId") int userId){
        if (isNotAdmin(userId)){
            return unauthorized();
        }

        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", userRepo.count());
            stats.put("totalMovies", movieRepo.count());
            stats.put("totalComments", commentRepo.count());
            stats.put("totalViews", movieRepo.sumTotalViews());
            return ResponseEntity.ok(new CustomResponse<>("Success", null, new CustomData<>(stats)));
        }
        catch (Exception e){
            return ResponseEntity.ok(new CustomResponse<>("Error", "Lỗi", null));
        }
    }

    @GetMapping("/movies")
    @ResponseBody
    public ResponseEntity<?> getMovies(
            @RequestParam("userId") int userId,
            @RequestParam(defaultValue = "0") int pageNo) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Movie> moviePage = movieService.getAllMovies(pageNo);
        return ResponseEntity.ok(new CustomResponse<>("Success", "Lấy danh sách phim thành công", new CustomData<>(moviePage)));
    }

    @PostMapping("/search/movies")
    @ResponseBody
    public ResponseEntity<?> searchMovies(
            @RequestParam("userId") int userId,
            @RequestParam("pageNo") int pageNo,
            @RequestParam("keyword") String keyword) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Movie> moviePage = movieService.searchMoviesByTitle(keyword, pageNo);
        return ResponseEntity.ok(new CustomResponse<>("Success", null, new CustomData<>(moviePage)));
    }

    @GetMapping("/get/movie")
    @ResponseBody
    public ResponseEntity<?> getMovieDetailForModal(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") int movieId) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Movie movie = movieService.getMovieById(movieId);

        if (movie == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Không tìm thấy phim", null));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("movie", movie);
        map.put("actors", movieActorRepo.findActorsByMovieId(movieId));
        map.put("directors", movieDirectorRepo.findDirectorsByMovieId(movieId));
        map.put("episodes", episodeRepo.findByMovieId(movieId));
        map.put("comments", commentRepo.getAllCommentsByMovieId(movieId));

        return ResponseEntity.ok(new CustomResponse<>("Success", null, new CustomData<>(map)));
    }

    @PutMapping("/update/movie")
    @ResponseBody
    public ResponseEntity<?> updateMovie(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") int movieId,
            @RequestBody Movie movie) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Movie updatedMovie = movieService.updateMovie(movieId, movie);
        return ResponseEntity.ok(new CustomResponse<>("Success", "Cập nhật phim thành công", new CustomData<>(updatedMovie)));
    }

    @DeleteMapping("/delete/movie")
    @ResponseBody
    public ResponseEntity<?> deleteMovie(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") int movieId) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        movieService.deleteMovie(movieId);
        return ResponseEntity.ok(new CustomResponse<>("Success", "Xóa phim thành công", null));
    }

    @PostMapping("/add/actors")
    @ResponseBody
    public ResponseEntity<?> addActors(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") int movieId,
            @RequestBody List<Integer> actorIds){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        try {
            List<MovieActor> list = new ArrayList<>();
            for (Integer actorId : actorIds) {
                list.add(new MovieActor(movieId, actorId));
            }
            movieActorRepo.saveAll(list);
            return ResponseEntity.ok(new CustomResponse<>("Success", "Đã thêm diễn viên vào phim", null));
        }
        catch (Exception e) {
            return ResponseEntity.ok(new CustomResponse<>("Success", "Thêm diễn viên thất bại", null));
        }
    }

    @DeleteMapping("/delete/actors-from-movie")
    @ResponseBody
    public ResponseEntity<?> deleteActors(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") int movieId,
            @RequestBody List<Integer> actorIds){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        try {
            List<MovieActor> list = new ArrayList<>();
            for (Integer actorId : actorIds) {
                list.add(new MovieActor(movieId, actorId));
            }
            movieActorRepo.deleteAll(list);
            return ResponseEntity.ok(new CustomResponse<>("Success", "Đã xóa diễn viên khỏi phim", null));
        }
        catch (Exception e) {
            return ResponseEntity.ok(new CustomResponse<>("Success", "Xóa diễn viên thất bại", null));
        }
    }

    @DeleteMapping("/delete/directors-from-movie")
    @ResponseBody
    public ResponseEntity<?> deleteDirectors(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") int movieId,
            @RequestBody List<Integer> directorIds){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        try {
            List<MovieActor> list = new ArrayList<>();
            for (Integer directorId : directorIds) {
                list.add(new MovieActor(movieId, directorId));
            }
            movieActorRepo.deleteAll(list);
            return ResponseEntity.ok(new CustomResponse<>("Success", "Đã xóa đạo diễn khỏi phim", null));
        }
        catch (Exception e) {
            return ResponseEntity.ok(new CustomResponse<>("Success", "Xóa đạo diễn thất bại", null));
        }
    }

    @PostMapping("/add/directors")
    @ResponseBody
    public ResponseEntity<?> addDirectors(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") int movieId,
            @RequestBody List<Integer> directorIds){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        try {
            List<MovieActor> list = new ArrayList<>();
            for (Integer actorId : directorIds) {
                list.add(new MovieActor(movieId, actorId));
            }
            movieActorRepo.saveAll(list);
            return ResponseEntity.ok(new CustomResponse<>("Success", "Đã thêm diễn viên vào phim", null));
        }
        catch (Exception e) {
            return ResponseEntity.ok(new CustomResponse<>("Success", "Thêm diễn viên thất bại", null));
        }
    }

    @PostMapping("/add/episodes")
    @ResponseBody
    public ResponseEntity<?> addEpisodes(
            @RequestParam("userId") int userId,
            @RequestParam("movieId") int movieId,
            @RequestBody List<Integer> episodeIds){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        try {
            List<Episode> episodes = episodeRepo.findAllById(episodeIds);
            for(Episode episode : episodes){
                episode.setMovieId(movieId);
            }
            episodeRepo.saveAll(episodes);
            return ResponseEntity.ok(new CustomResponse<>("Success", "Đã thêm tập phim", null));
        }
        catch (Exception e) {
            return ResponseEntity.ok(new CustomResponse<>("Success", "Thêm tập phim thất bại", null));
        }
    }

    @DeleteMapping("/delete/episodes-from-movie")
    @ResponseBody
    public ResponseEntity<?> deleteEpisodes(
            @RequestParam("userId") int userId,
            @RequestBody List<Integer> episodeIds){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        try {
            List<Episode> episodes = episodeRepo.findAllById(episodeIds);
            for(Episode episode : episodes){
                episode.setMovieId(-1);
            }
            episodeRepo.saveAll(episodes);
            return ResponseEntity.ok(new CustomResponse<>("Success", "Đã gỡ tập phim", null));
        }
        catch (Exception e) {
            return ResponseEntity.ok(new CustomResponse<>("Success", "Gỡ tập phim thât bại", null));
        }
    }

    //-------------------------------Genre-----------------------------------
    @Autowired
    private GenreService genreService;

    @GetMapping("/genres")
    @ResponseBody
    public ResponseEntity<?> getGenres(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Genre> genrePage = genreService.getAllGenre(pageNo);
        CustomData<Page<Genre>> data = new CustomData<>(genrePage);
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @GetMapping("/search/genres")
    @ResponseBody
    public ResponseEntity<?> searchGenres(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo,
            @RequestParam(name = "keyword") String keyword){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Genre> genresPage = genreService.searchGenre(keyword, pageNo);
        CustomData<Page<Genre>> data = new CustomData<>(genresPage);
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @GetMapping("/genre")
    @ResponseBody
    public ResponseEntity<?> getGenreData(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "genreId") int genreId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Optional<Genre> genre = genreService.getGenreById(genreId);
        if (genre.isEmpty()) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Không tìm thấy thể loại", null));
        }

        CustomData<Genre> data = new CustomData<>(genre.get());
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @PostMapping("/add/genre")
    @ResponseBody
    public ResponseEntity<?> addGenre(
            @RequestParam(name = "userId") int userId,
            @RequestBody Genre genre) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Genre newGenre = genreService.addGenre(genre);

        if (newGenre == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Thêm thất bại", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", "Thêm thành công", null));
    }

    @PutMapping("/update/genre")
    @ResponseBody
    public ResponseEntity<?> updateGenre(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "genreId") int genreId,
            @RequestBody Genre genre){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Genre updatedGenre = genreService.updateGenre(genreId, genre);

        if (updatedGenre == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Cập nhật thất bại!", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", "Cập nhật thành công!", null));
    }

    @DeleteMapping("/delete/genre")
    @ResponseBody
    public ResponseEntity<?> deleteGenre(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "genreId") int genreId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        boolean isDeleted = genreService.deleteGenre(genreId);

        if (!isDeleted) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Xóa thất bại!", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", "Xóa thành công!", null));
    }


    //----------------------------------Director-----------------------------------

    @Autowired
    private DirectorService directorService;

    @GetMapping("/directors")
    @ResponseBody
    public ResponseEntity<?> getAllDirectors(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Director> directorPage = directorService.getAllDirectors(pageNo);
        CustomData<Page<Director>> data = new CustomData<>(directorPage);

        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @GetMapping("/search/directors")
    @ResponseBody
    public ResponseEntity<?> searchDirectors(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo,
            @RequestParam(name = "keyword") String keyword){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Director> directorPage = directorService.searchDirectors(keyword, pageNo);
        CustomData<Page<Director>> data = new CustomData<>(directorPage);
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @GetMapping("/director")
    @ResponseBody
    public ResponseEntity<?> getDirectorData(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "directorId") int directorId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Optional<Director> director = directorService.findById(directorId);

        if (director.isEmpty()) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Không tìm thấy đạo diễn", null));
        }

        CustomData<Director> data = new CustomData<>(director.get());
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @PostMapping("/add/director")
    @ResponseBody
    public ResponseEntity<?> addDirector(
            @RequestParam(name = "userId") int userId,
            @RequestBody Director director){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Director newDirector = directorService.addDirector(director);

        if (newDirector == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Thêm thất bại.", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", null, null));
    }

    @PutMapping("/update/director")
    @ResponseBody
    public ResponseEntity<?> updateDirector(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "directorId") int directorId,
            @RequestBody Director director){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Director updatedDirector = directorService.updateDirector(directorId, director);
        if (updatedDirector == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Cập nhật thất bại.", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", null, null));
    }

    @DeleteMapping("/delete/director")
    @ResponseBody
    public ResponseEntity<?> deleteDirector(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "directorId") int directorId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        boolean isDeleted = directorService.deleteDirector(directorId);

        if (!isDeleted) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Xóa thất bại.", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", null, null));
    }

    //------------------------------Actor---------------------------------

    @Autowired
    private ActorService actorService;

    @GetMapping("/actors")
    @ResponseBody
    public ResponseEntity<?> getAllActors(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Actor> actorPage = actorService.getAllActors(pageNo);
        CustomData<Page<Actor>> data = new CustomData<>(actorPage);
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @GetMapping("/search/actors")
    @ResponseBody
    public ResponseEntity<?> searchActors(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo,
            @RequestParam(name = "keyword") String keyword){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Actor> actorPage = actorService.searchActors(keyword, pageNo);
        CustomData<Page<Actor>> data = new CustomData<>(actorPage);
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @GetMapping("/actor")
    @ResponseBody
    public ResponseEntity<?> getActorData(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "actorId") int actorId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Optional<Actor> actor = actorService.getActorById(actorId);

        if (actor.isEmpty()) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Không tìm thấy diễn viên", null));
        }

        CustomData<Actor> data = new CustomData<>(actor.get());
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @PostMapping("/add/actor")
    @ResponseBody
    public ResponseEntity<?> addActor(
            @RequestParam(name = "userId") int userId,
            @RequestBody Actor actor){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Actor newActor = actorService.addActor(actor);
        if (newActor == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Thêm thất bại.", null));
        }
        return ResponseEntity.ok(new CustomResponse<>("Success", null, null));
    }

    @PutMapping("/update/actor")
    @ResponseBody
    public ResponseEntity<?> updateActor(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "actorId") int actorId,
            @RequestBody Actor actor){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Actor updatedActor = actorService.updateActor(actorId, actor);
        if (updatedActor == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Cập nhật thất bại.", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", null, null));
    }

    @DeleteMapping("/delete/actor")
    @ResponseBody
    public ResponseEntity<?> deleteActor(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "actorId") int actorId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        boolean isDeleted = actorService.deleteActor(actorId);

        if (!isDeleted) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Xóa thất bại.", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", null, null));
    }

    //-------------------------Episode------------------
    @Autowired
    private EpisodeService episodeService;

    @GetMapping("/search/episodes")
    @ResponseBody
    public ResponseEntity<?> searchEpisodes(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo,
            @RequestParam(name = "keyword") String keyword){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<EpisodeAndMovieTitle> episodes = episodeService.searchEpisode(keyword, pageNo);
        CustomData<Page<EpisodeAndMovieTitle>> data = new CustomData<>(episodes);
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @GetMapping("/episode")
    @ResponseBody
    public ResponseEntity<?> getEpisode(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "episodeId") int episodeId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Optional<Episode> episode = episodeService.getEpisodeById(episodeId);

        if (episode.isEmpty()) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Không tìm thấy tập phim", null));
        }

        CustomData<Episode> data = new CustomData<>(episode.get());
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @GetMapping("/episodes/unlinked")
    @ResponseBody
    public ResponseEntity<?> getUnlinkedEpisodes(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<Episode> episodes = episodeService.getUnlinkedEpisodes(pageNo);
        CustomData<Page<Episode>> data = new CustomData<>(episodes);
        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @PostMapping("/add/episode")
    @ResponseBody
    public ResponseEntity<?> addEpisode(
            @RequestParam(name = "userId") int userId,
            @RequestBody Episode episode){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Episode newEpisode = episodeService.addEpisode(episode);
        if (newEpisode == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Thêm thất bại!", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", "Thêm thành công!", null));
    }

    @PutMapping("/update/episode")
    @ResponseBody
    public ResponseEntity<?> updateEpisode(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "episodeId") int episodeId,
            @RequestBody Episode episode){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Episode updatedEpisode = episodeService.updateEpisode(episodeId, episode);
        if (updatedEpisode == null) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Cập nhật thất bại!", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", "Cập nhật thành công!", null));
    }

    @DeleteMapping("/delete/episode")
    @ResponseBody
    public ResponseEntity<?> deleteEpisode(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "episodeId") int episodeId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        boolean isDeleted = episodeService.deleteEpisode(episodeId);
        if (isDeleted) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Xóa thất bại!", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Error", "Xóa thành công!", null));
    }


    @GetMapping("/hot-movies")
    @ResponseBody
    public ResponseEntity<?> getHotMovies(
            @RequestParam("userId") int userId) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        try {
            int limit=20;
            List<Movie> seriesMovies = movieRepo.findTopSeriesMoviesByTotalViews(limit);

            List<Movie> fullMovies = movieRepo.findTopFullMoviesByViews(limit);

            Map<String, List<Movie>> data = new HashMap<>();
            data.put("series", seriesMovies);
            data.put("full", fullMovies);

            return ResponseEntity.ok(new CustomResponse<>("Success", "Lấy danh sách hot movies thành công", new CustomData<>(data)));
        } catch (Exception e) {
            return ResponseEntity.ok(new CustomResponse<>("Error", "Lỗi: " + e.getMessage(), null));
        }
    }

    //--------------------------Comment----------------------

    @GetMapping("/comments")
    @ResponseBody
    public ResponseEntity<?> getComments(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Pageable pageable = PageRequest.of(pageNo, 10);
        Page<Comment> page = commentRepo.findAll(pageable);
        CustomData<Page<Comment>> data = new CustomData<>(page);

        return ResponseEntity.ok(new CustomResponse<>("Success", null, data));
    }

    @DeleteMapping("/delete/comment")
    @ResponseBody
    public ResponseEntity<?> deleteComment(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "commentId") int commentId){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        if (commentRepo.existsById(commentId)) {
            commentRepo.deleteById(commentId);
            return ResponseEntity.ok(new CustomResponse<>("Success", "Đã xóa comment", null));
        }
        return ResponseEntity.ok(new CustomResponse<>("Error", "Comment không tồn tại", null));
    }

    @DeleteMapping("/delete/comments")
    @ResponseBody
    public ResponseEntity<?> deleteComments(
            @RequestParam("userId") int userId,
            @RequestBody List<Integer> commentIds){

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        try {
            commentRepo.deleteAllById(commentIds);
            return ResponseEntity.ok(new CustomResponse<>("Success", "Đã xóa các comments đã chọn", null));
        }
        catch (Exception e) {
            return ResponseEntity.ok(new CustomResponse<>("Success", "Xóa các comments thất bại", null));
        }
    }
    
    // Lấy chi tiết phim kèm episodes, actors và genres
    @GetMapping("/{id}/detail")
    public ResponseEntity<?> getMovieDetail(@PathVariable("id") int id) {
        try {
            MovieDetailResponseDTO movieDetail = movieServiceAdmin.getMovieDetail(id);
            CustomData<MovieDetailResponseDTO> data = new CustomData<>(movieDetail);
            CustomResponse<MovieDetailResponseDTO> response = new CustomResponse<>(
                "Success", 
                "Lấy thông tin chi tiết phim thành công!", 
                data
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                new CustomResponse<>("Error", e.getMessage(), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                new CustomResponse<>("Error", "Không thể lấy thông tin phim: " + e.getMessage(), null)
            );

        }
    }


    //--------------------------User----------------------

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<?> getUsers(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<User> usersPage = userService.getAllUsers(pageNo);
        CustomData<Page<User>> data = new CustomData<>(usersPage);

        return ResponseEntity.ok(new CustomResponse<>("Success", "Lấy danh sách user thành công", data));
    }

    @GetMapping("/search/users")
    @ResponseBody
    public ResponseEntity<?> searchUsers(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "pageNo") int pageNo,
            @RequestParam(name = "keyword") String keyword) {

        if (isNotAdmin(userId)) {
            return unauthorized();
        }

        Page<User> usersPage = userService.searchUsers(keyword, pageNo);
        CustomData<Page<User>> data = new CustomData<>(usersPage);

        return ResponseEntity.ok(new CustomResponse<>("Success", "Tìm kiếm user thành công", data));
    }

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<?> getUserData(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "targetUserId") int targetUserId){

        if (isNotAdmin(userId)){
            return unauthorized();
        }

        User user = userService.getUserById(targetUserId);

        if (user == null){
            return ResponseEntity.ok(new CustomResponse<>("Error", "Không tìm thấy user", null));
        }

        CustomData<User> data = new CustomData<>(user);
        return ResponseEntity.ok(new CustomResponse<>("Success", "Lấy thành công", data));
    }

    @PostMapping("/add/user")
    @ResponseBody
    public ResponseEntity<?> addUser(
            @RequestParam(name = "userId") int userId,
            @RequestBody UserForm userForm){

        if (isNotAdmin(userId)){
            return unauthorized();
        }

        User user = userService.createUser(userForm);

        if (user == null){
            return ResponseEntity.ok(new CustomResponse<>("Error", "Tạo thất bại. Username hoặc email đã được sử dụng.", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", "Tạo user thành công", null));
    }

    // 7. API Cập nhật User
    @PutMapping("/update/user")
    @ResponseBody
    public ResponseEntity<?> updateUser(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "targetUserId") int targetUserId,
            @RequestBody UserForm userForm){

        if (isNotAdmin(userId)){
            return unauthorized();
        }

        User user = userService.updateUser(targetUserId, userForm);

        if (user == null){
            return ResponseEntity.ok(new CustomResponse<>("Error", "Cập nhật thất bại!", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", "Cập nhật thành công", null));
    }

    // 8. API Xóa User
    @DeleteMapping("/delete/user")
    @ResponseBody
    public ResponseEntity<?> deleteUser(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "targetUserId") int targetUserId){

        if (isNotAdmin(userId)){
            return unauthorized();
        }

        boolean isDeleted = userService.deleteUser(targetUserId);

        if (!isDeleted){
            return ResponseEntity.ok(new CustomResponse<>("Error", "Xóa thất bại!", null));
        }

        return ResponseEntity.ok(new CustomResponse<>("Success", "Xóa thành công", null));
    }

    
}

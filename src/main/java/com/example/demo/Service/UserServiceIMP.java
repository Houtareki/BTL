package com.example.demo.Service;

import java.time.LocalDateTime;

import com.example.demo.Model.CommonModel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Movie;
import com.example.demo.Entity.User;
import com.example.demo.Respository.FavoriteRepo;
import com.example.demo.Respository.MovieRepo;
import com.example.demo.Respository.SaveRepo;
import com.example.demo.Respository.UserRepo;

import java.util.List;

@Service
public class UserServiceIMP implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FavoriteRepo favoriteRepo;

    @Autowired
    private SaveRepo saveRepo;

    @Autowired
    private MovieRepo movieRepo;

    public User comfirmUserLogin(LoginForm loginForm) {
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();
        User user = userRepo.findByEmail(email);
        if(user!=null) {
            if(!email.equals(user.getEmail()) || !password.equals(user.getPassword())) return null;
            return user;
        }
        return null;
    }

    public User checkRegisterForm(RegisterForm registerForm) {
        String username = registerForm.getUsername();
        String email = registerForm.getEmail();
        String password = registerForm.getPassword();
        User userWithThisUserName = userRepo.findByUsername(username);
        User userWithThisEmail = userRepo.findByEmail(email);
        if(userWithThisUserName != null || userWithThisEmail != null) {
            return null;
        }
        User user = new User(username, email, password);
        userRepo.save(user);
        return user;
    }

    public User findByUserId(int userId) {
        return userRepo.findByUserId(userId);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User activateUser(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if(user!=null) {
            this.userRepo.activateUser(userId);
            return user;
        }
        return null;
    }

    public User changeUserPassword(int userId, String newPassword, String comfirmPassword) {
        User user = this.userRepo.findByUserId(userId);
        if(user!=null && newPassword.equals(comfirmPassword)) {
            this.userRepo.changeUserPassword(userId, newPassword);
            return this.userRepo.findByUserId(userId);
        }
        return null;
    }

    public User updateUserFavoritedRepo(int userId, int movieId) {
        User user = userRepo.findByUserId(userId);
        Movie movie = movieRepo.findByMovieId(movieId);
        if(user!=null && movie!=null) {
            int isLikedThisMovie = favoriteRepo.isMovieFavoritedByUser(userId, movieId);
            // Nếu chưa like
            if(isLikedThisMovie==0) {
                favoriteRepo.addToRepo(userId, movieId, LocalDateTime.now());
            }
            else {
                favoriteRepo.removeFromRepo(userId, movieId);
            }
            return user;
        }
        return null;
    }

    public User updateUserSavedRepo(int userId, int movieId) {
        User user = userRepo.findByUserId(userId);
        Movie movie = movieRepo.findByMovieId(movieId);
        if(user!=null && movie!=null) {
            int isSavedThisMovie = saveRepo.isMovieSavedByUser(userId, movieId);
            // Nếu chưa like
            if(isSavedThisMovie==0) {
                saveRepo.addToRepo(userId, movieId, LocalDateTime.now());
            }
            else {
                saveRepo.removeFromRepo(userId, movieId);
            }
            return user;
        }
        return null;
    }

    private static final int PAGE_SIZE = 10;

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepo.findByRole(role);
    }

    @Override
    public User lockUserAccount(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if(user != null) {
            user.setState(State.REMOVED);
            return userRepo.save(user);
        }
        return null;
    }

    @Override
    public User unlockUserAccount(int userId) {
        User user = this.userRepo.findByUserId(userId);
        if(user != null) {
            user.setState(State.ACTIVE);
            return userRepo.save(user);
        }
        return null;
    }

    @Override
    public User changeUserRole(int userId, Role newRole) {
        User user = this.userRepo.findByUserId(userId);
        if(user != null) {
            user.setRole(newRole);
            return userRepo.save(user);
        }
        return null;
    }

    @Override
    public User createAdmin(RegisterForm registerForm) {
        if (userRepo.findByUsername(registerForm.getUsername()) != null) {
            return null; //ton tai user
        }

        if (userRepo.findByEmail(registerForm.getEmail()) != null) {
            return null; //ton tai email
        }

        User admin = new User();
        admin.setUsername(registerForm.getUsername());
        admin.setEmail(registerForm.getEmail());
        admin.setPassword(registerForm.getPassword());

        admin.setRole(Role.ADMIN);
        admin.setState(State.ACTIVE);

        return userRepo.save(admin);
    }

    @Override
    public User getUserById(int userId){
        return userRepo.findById(userId).orElse(null);
    }

    @Override
    public Page<User> getAllUsers(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "userId"));
        return userRepo.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "userId"));
        return userRepo.searchByUsernameOrEmail(keyword, pageable);
    }

    @Override
    public User createUser(UserForm userForm) {
        if (userRepo.existsByUsername(userForm.getUsername())) {
            return null;
        }
        if (userRepo.existsByEmail(userForm.getEmail())) {
            return null;
        }

        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setRole(userForm.getRole());
        user.setState(userForm.getState());

        return userRepo.save(user);
    }

    @Override
    public User updateUser(int userId, UserForm userForm) {
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        if (!user.getUsername().equals(userForm.getUsername()) && userRepo.existsByUsername(userForm.getUsername())) {
            return null;
        }

        if (!user.getEmail().equals(userForm.getEmail()) && userRepo.existsByEmail(userForm.getEmail())) {
            return null;
        }

        user.setUsername(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setRole(userForm.getRole());
        user.setState(userForm.getState());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(userForm.getPassword());
        }
        return userRepo.save(user);
    }

    @Override
    public boolean deleteUser(int userId) {
        if (!userRepo.existsById(userId)) {
            return false;
        }
        try {
            userRepo.deleteById(userId);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

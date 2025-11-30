package com.example.demo.Service;

import com.example.demo.Model.CommonModel.UserForm;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.User;
import com.example.demo.Model.CommonModel.LoginForm;
import com.example.demo.Model.CommonModel.RegisterForm;
import com.example.demo.Model.CommonModel.Role;
import java.util.List;

@Service
public interface UserService {
    User comfirmUserLogin(LoginForm loginForm);
    User checkRegisterForm(RegisterForm registerForm);
    User findByUserId(int userId);
    User findByEmail(String email);
    User activateUser(int userId);
    User changeUserPassword(int userId, String newPassword, String comfirmPassword);
    User updateUserFavoritedRepo(int userId, int movieId);
    User updateUserSavedRepo(int userId, int movieId);

    List<User> getUsersByRole(Role role);
    User lockUserAccount(int userId);
    User unlockUserAccount(int userId);
    User changeUserRole(int userId, Role newRole);
    User createAdmin(RegisterForm registerForm);

    User getUserById(int userId);
    Page<User> getAllUsers(int pageNo);
    Page<User> searchUsers(String keyword, int pageNo);
    User createUser(UserForm userForm);
    User updateUser(int userId, UserForm userForm);
    boolean deleteUser(int userId);
}

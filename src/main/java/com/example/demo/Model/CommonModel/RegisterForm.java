package com.example.demo.Model.CommonModel;

public class RegisterForm {
    private String username;
    private String email;
    private String password;
    private String comfirmPassword;

    public RegisterForm(String username, String email, String password, String comfirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.comfirmPassword = comfirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getComfirmPassword() {
        return comfirmPassword;
    }

    public void setComfirmPassword(String comfirmPassword) {
        this.comfirmPassword = comfirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}

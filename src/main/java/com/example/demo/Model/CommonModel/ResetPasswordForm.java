package com.example.demo.Model.CommonModel;

public class ResetPasswordForm {
    private String password;
    private String confirmPassword;

    public ResetPasswordForm(String password, String comfirnPassword) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}

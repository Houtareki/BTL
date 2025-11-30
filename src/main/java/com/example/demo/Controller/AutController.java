package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.User;
import com.example.demo.Model.CommonModel.EmailDetail;
import com.example.demo.Model.CommonModel.LoginForm;
import com.example.demo.Model.CommonModel.RegisterForm;
import com.example.demo.Model.CommonModel.ResetPasswordForm;
import com.example.demo.Model.CommonModel.State;
import com.example.demo.Model.ResponseModel.CustomData;
import com.example.demo.Model.ResponseModel.CustomResponse;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.UserService;

@Controller
@RequestMapping("/auth")
public class AutController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/check-registration")
    public ResponseEntity<?> checkRegistration(@RequestBody RegisterForm registerForm) {
        CustomData<User> data = new CustomData<>();
        CustomResponse<User> response = new CustomResponse<>();
        User user = userService.checkRegisterForm(registerForm);
        if(user != null) {
            data.setData(user);
            response.setData(data);
            response.setStatus("Success");
            response.setMessage("Đăng kí thành công!\nChúng tôi đã gửi mail cho bạn để kích hoạt tài khoản!");
        } 
        else {
            response.setStatus("Error");
            response.setMessage("Username hoặc Email đã được sử dụng!");
        }
        return ResponseEntity.ok(response);
    }

    // Xác thực đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> postMethodName(@RequestBody LoginForm loginForm) {
        CustomData<User> data = new CustomData<>();
        CustomResponse<User> response = new CustomResponse<>();
        User user = userService.comfirmUserLogin(loginForm);
        if (user != null) {
            if (user.getState() != State.ACTIVE) {
                response.setStatus("Error");
                response.setMessage("Tài khoản này chưa được kích hoạt!");
            } else {
                data.setData(user);
                response.setStatus("Success");
                response.setMessage("Đăng nhập thành công!");
                response.setData(data);
            }
        } else {
            response.setStatus("Error");
            response.setMessage("Sai email hoặc password!");
        }

        return ResponseEntity.ok(response);
    }

    // Gửi email để xác nhận đăng kí tài khoản
    @GetMapping("/send/confirmation-email")
    public ResponseEntity<?> sendConfirmMsgViaEmail(@RequestParam(value = "id") int id) {
        CustomResponse<User> response = new CustomResponse<>();
        try {
            String subject = "Xác nhận đăng ký tài khoản";
            String body = "<h3>Chào mừng bạn đến với PTIT CINEMA!</h3>"
                    + "<p>Bạn đã đăng ký tài khoản thành công.</p>"
                    + "<p>Nhấn vào liên kết bên dưới để xác nhận tài khoản đồng thời bạn sẽ được chuyển đến trang đăng nhập:</p>"
                    + "<a href='http://localhost:8080/auth/activate?id=" + id + "'>Xác nhận tài khoản</a>"
                    + "<p>Nếu bạn không đăng ký tài khoản, vui lòng bỏ qua email này.</p>"
                    + "<br><p>Trân trọng,<br>PTIT CINEMA Team</p>";
            User targetUser = userService.findByUserId(id);
            this.emailService.sendEmail(new EmailDetail(targetUser.getEmail(), body, subject));
            response.setStatus("Success");
            response.setMessage("Gửi thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("Error");
            response.setMessage("Người dùng không tồn tại!");
            return ResponseEntity.ok(response);
        }
    }

    // Kích hoạt tài khoản
    @GetMapping("/activate")
    public String getMethodName(@RequestParam(name = "id") int userId) {
        User user = this.userService.activateUser(userId);
        if(user!=null) {
            return "login";
        }
        return "error";
    }

    // Gửi email chứa link lấy lại mật khẩu tới email của người dùng
    @GetMapping("/send/recover-email")
    public ResponseEntity<?> sendRecoverMsgViaEmail(@RequestParam(value = "email") String email) {
        CustomResponse<User> response = new CustomResponse<>();
        try {
            User targetUser = userService.findByEmail(email);
            String subject = "Đặt lại mật khẩu tài khoản";
            String body = "<h3>Cảm ơn bạ vì đã sử dụng dịch vụ của PTIT CINEMA!</h3>"
                    + "<p>Bạn đã gửi yếu cầu lấy lại tài khoản thành công.</p>"
                    + "<p>Nhấn vào liên kết bên dưới để được chuyển đến trang đổi lại mật khẩu:</p>"
                    + "<a href='http://localhost:8080/user/reset-password?id=" + targetUser.getUserId()
                    + "'>Xác nhận tài khoản</a>"
                    + "<p>Nếu bạn không yêu cầu lấy lại mật khẩu tài khoản, vui lòng bỏ qua email này.</p>"
                    + "<br><p>Trân trọng,<br>PTIT CINEMA Team</p>";
            this.emailService.sendEmail(new EmailDetail(targetUser.getEmail(), body, subject));
            response.setStatus("Success");
            response.setMessage("Gửi thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus("Error");
            response.setMessage("Người dùng không tồn tại!");
            return ResponseEntity.ok(response);
        }
    }

    // Đặt lại mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam(name="id") int userId, @RequestBody ResetPasswordForm resetPasswordForm) {
        CustomResponse<User> response = new CustomResponse<>();
        User user = this.userService.findByUserId(userId);
        if(user!=null) {
            // Kiểm tra tài khoản đã được kích hoạt chưa
            if(user.getState() != State.ACTIVE) {
                response.setStatus("Error");
                response.setMessage("Tài khoản này chưa được kích hoạt!");
                return ResponseEntity.ok(response);    
            }
            // Kiêm tra mật khẩu mới có trùng với mật khẩu cũ không
            if(user.getPassword().equals(resetPasswordForm.getPassword())) {
                response.setStatus("Error");
                response.setMessage("Mật khẩu mới không được trùng với mật khẩu cũ!");
                return ResponseEntity.ok(response);
            }
            // Kiểm tra mật khẩu xác nhận có trùng với mật khẩu mới không
            if(!resetPasswordForm.getPassword().equals(resetPasswordForm.getConfirmPassword())) {
                response.setStatus("Error");
                response.setMessage("Mật khẩu xác nhận không khớp!");
                return ResponseEntity.ok(response);
            }

            User updatedUser = this.userService.changeUserPassword(userId, resetPasswordForm.getPassword(), resetPasswordForm.getConfirmPassword());
            if(updatedUser!= null) {
                response.setStatus("Success");
                response.setMessage("Đặt lại mật khẩu thành công!");
                return ResponseEntity.ok(response);
            }
        }
        response.setStatus("Error");
        response.setMessage("Thất bại! Hãy kiểm tra lại!");
        return ResponseEntity.ok(response);
    }
}

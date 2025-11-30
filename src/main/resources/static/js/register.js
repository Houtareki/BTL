
const form = document.getElementById("registerForm");
const messageBox = document.getElementById("message");

form.addEventListener("submit", async function (e) {
    e.preventDefault();
    messageBox.textContent = "";
    messageBox.className = "";

    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (!username || !email || !password || !confirmPassword) {
        showMessage("Vui lòng điền đầy đủ thông tin", "error");
        return;
    }
    if (!email.endsWith("@gmail.com")) {
        showMessage("Tài khoản phải có định dạng email '@gmail.com'", "error");
        return;
    }
    if (password.length < 6) {
        showMessage("Mật khẩu phải có ít nhất 6 ký tự", "error");
        return;
    }
    if (password !== confirmPassword) {
        showMessage("Mật khẩu xác nhận không khớp", "error");
        return;
    }

    try {
        // B1: gọi API check-registration
        const response = await fetch("http://localhost:8080/auth/check-registration", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, email, password })
        });

        const result = await response.json();

        if (result.status === "Success") {
            showMessage(result.message || "Đăng ký thành công!", "success");
            setTimeout(() => {
                window.location.href = "/user/login";
            }, 1000);

            // lấy userId từ object trả về
            const userId = result.data?.data?.userId;
            if (userId) {
                // gọi API gửi mail
                await fetch(`http://localhost:8080/auth/send/confirmation-email?id=${userId}`);
            }
        }
        else {
            showMessage(result.message || "Đăng ký thất bại!", "error");
        }
    } catch (err) {
        console.error("Register error:", err);
        showMessage("Không thể kết nối đến server!", "error");
    }
});

function showMessage(text, type) {
    messageBox.textContent = text;
    messageBox.className = type;
}

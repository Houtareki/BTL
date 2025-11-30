
document.getElementById("resetForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const msg = document.getElementById("message");

    // Lấy id từ URL (ví dụ: reset-password.html?id=5)
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get("id");

    if (!userId) {
        msg.textContent = "Liên kết đặt lại mật khẩu không hợp lệ!";
        msg.className = "message error";
        return;
    }

    fetch(`http://localhost:8080/auth/reset-password?id=${userId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ password, confirmPassword })
    })
        .then(res => res.json())
        .then(data => {
            if (data.status === "Success") {
                msg.textContent = data.message;
                msg.className = "message success";
                setTimeout(() => {
                    window.location.href = "/user/login";
                }, 1000);
            } else {
                msg.textContent = data.message;
                msg.className = "message error";
            }
        })
        .catch(err => {
            msg.textContent = "Lỗi kết nối tới server!";
            msg.className = "message error";
        });
});

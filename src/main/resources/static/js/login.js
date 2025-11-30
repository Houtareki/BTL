
const form = document.getElementById("loginForm");
const errorBox = document.getElementById("error");

form.addEventListener("submit", async function (e) {
    e.preventDefault();
    errorBox.textContent = "";
    errorBox.className = "";

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    // Kiểm tra email có bị bỏ trông không
    if (!email) {
        showMessage("Vui lòng nhập tên đăng nhập", "error");
        return;
    }
    // Kiểm tra định dạng có đúng là email không
    if (!email.endsWith("@gmail.com")) {
        showMessage("Tài khoản phải có định dạng email '@gmail.com'", "error");
        return;
    }
    // Kiểm tra mật khẩu có bị bỏ trống không
    if (!password) {
        showMessage("Vui lòng nhập mật khẩu", "error");
        return;
    }
    // Kiểm tra độ dài mật khẩu
    if (password.length < 6) {
        showMessage("Mật khẩu phải có ít nhất 6 ký tự", "error");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        const result = await response.json();

        if (result.status === "Success") {
            showMessage(result.message || "Đăng nhập thành công!", "success");

            const userId = result.data?.data?.userId; // lấy userId từ response
            const userRole = result.data?.data?.role;
            const username = result.data?.data?.username;

            setTimeout(() => {
                //ktra role
                if (userRole === "ADMIN") {
                    window.location.href = `/admin/dashboard?userId=${userId}`;
                }
                else {
                    window.location.href = `/user/home?userId=${userId}`;
                }
            }, 1000);
        } else {
            showMessage(result.message || "Đăng nhập thất bại!", "error");
        }

    } catch (err) {
        console.error("Login error:", err);
        showMessage("Không thể kết nối đến server!", "error");
    }
});

function showMessage(text, type) {
    errorBox.textContent = text;
    errorBox.className = type;
}

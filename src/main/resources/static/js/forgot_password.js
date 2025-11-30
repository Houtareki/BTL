
document.getElementById("forgotForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const email = document.getElementById("emailInput").value;
    const msg = document.getElementById("message");

    if(!email) {
        msg.textContent = "Vui lòng nhập email!";
        msg.className = "message error";
        return;
    }

    if(email.endsWith("@gmail.com") === false) {
        msg.textContent = "Vui lòng nhập email hợp lệ có đuôi '@gmail.com'";
        msg.className = "message error";
        return;
    }

    fetch(`http://localhost:8080/auth/send/recover-email?email=${encodeURIComponent(email)}`, {
        method: "GET"
    })
        .then(res => res.json())
        .then(data => {
            if (data.status === "Success") {
                msg.textContent = data.message;
                msg.className = "message success";
                // Chờ 1s rồi redirect
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

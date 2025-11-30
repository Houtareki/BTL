let currentMovieId = null;
const params = new URLSearchParams(window.location.search);
const userId = params.get("userId");

function goHome() {
    window.location.href = `home?userId=${userId}`;
}

// API helper
async function fetchApi(url, options = {}) {
    try {
        const res = await fetch(url, options);
        return await res.json();
    } catch (err) {
        console.error('fetchApi error:', err);
        return { status: 'Error', data: { data: [] } };
    }
}

// Load user info
async function loadUserInfo() {
    const json = await fetchApi(`http://localhost:8080/user/get-user-info?userId=${userId}`);
    if (json.status === "Success" && json.data?.data) {
        const user = json.data.data;
        document.getElementById('username').textContent = user.username;
        document.getElementById('email').textContent = user.email;

        const statusEl = document.getElementById('status');
        statusEl.textContent = user.state === 'ACTIVE' ? 'Đang hoạt động' : 'Chưa kích hoạt';
        statusEl.style.color = user.state === 'ACTIVE' ? '#4caf50' : '#ff9800';
    }
}

// Change password
document.getElementById('changePasswordForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const currentPassword = document.getElementById('currentPassword').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const messageEl = document.getElementById('password-message');

    // Validation
    if (newPassword.length < 6) {
        messageEl.textContent = 'Mật khẩu mới phải có ít nhất 6 ký tự!';
        messageEl.className = 'message error';
        return;
    }

    if (newPassword !== confirmPassword) {
        messageEl.textContent = 'Mật khẩu xác nhận không khớp!';
        messageEl.className = 'message error';
        return;
    }

    // Call API
    const json = await fetchApi(`http://localhost:8080/user/change-password?userId=${userId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            currentPassword: currentPassword,
            password: newPassword,
            confirmPassword: confirmPassword
        })
    });

    if (json.status === "Success") {
        messageEl.textContent = 'Đổi mật khẩu thành công!';
        messageEl.className = 'message success';
        document.getElementById('changePasswordForm').reset();

        setTimeout(() => {
            messageEl.style.display = 'none';
        }, 3000);
    } else {
        messageEl.textContent = json.message || 'Đổi mật khẩu thất bại!';
        messageEl.className = 'message error';
    }
});

// Tab switching
function showTab(tabName) {
    const tabs = document.querySelectorAll('.tab-content');
    const buttons = document.querySelectorAll('.tab-btn');

    tabs.forEach(tab => tab.classList.remove('active'));
    buttons.forEach(btn => btn.classList.remove('active'));

    document.getElementById(tabName).classList.add('active');
    event.target.closest('.tab-btn').classList.add('active');
}

// Render movies
function renderMovies(movies, containerId) {
    const container = document.getElementById(containerId);
    container.innerHTML = '';

    if (!movies || movies.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <i class="fa-solid fa-film"></i>
                <h3>Chưa có phim nào</h3>
                <p>Hãy khám phá và thêm phim yêu thích của bạn!</p>
            </div>
        `;
        return;
    }

    movies.forEach(movie => {
        const card = document.createElement('div');
        card.className = 'movie-card';
        card.innerHTML = `
            <img src="${movie.thumbUrl}" alt="${movie.title}">
            <div class="overlay">
                <h4>${movie.title}</h4>
                <p>${movie.description || 'Chưa có mô tả'}</p>
            </div>
        `;
        card.onclick = () => showMovieDetail(movie);
        container.appendChild(card);
    });
}

// Load watchlist
async function loadWatchlist() {
    const likedJson = await fetchApi(`http://localhost:8080/user/get-liked-movies-by-userId?userId=${userId}`);
    renderMovies(likedJson.data?.data, 'liked-movies');

    const savedJson = await fetchApi(`http://localhost:8080/user/get-saved-movies-by-userId?userId=${userId}`);
    renderMovies(savedJson.data?.data, 'saved-movies');

    const watchedJson = await fetchApi(`http://localhost:8080/user/get-watched-movies-by-userId?userId=${userId}`);
    renderMovies(watchedJson.data?.data, 'watched-movies');
}

// Movie detail functions
async function fetchMovieById(movieId) {
    const json = await fetchApi(`http://localhost:8080/user/get-movie?movieId=${movieId}`);
    return json.data?.data || null;
}

async function fetchEpisodes(movieId) {
    return await fetchApi(`http://localhost:8080/user/get-episodes?movieId=${movieId}`);
}

async function fetchComments(movieId) {
    return await fetchApi(`http://localhost:8080/user/get-comments-by-movieId?userId=${userId}&movieId=${movieId}`);
}

async function fetchInteraction(movieId, userId) {
    try {
        const res = await fetch(`http://localhost:8080/user/get-movie-interaction?userId=${userId}&movieId=${movieId}`);
        const result = await res.json();

        if (result.status === "Success") {
            const data = result.data.data;
            document.getElementById("like-btn").innerHTML =
                `<i class="fa-solid fa-thumbs-up"></i> ${data.userLikedThis ? "Liked" : "Like"} (${data.totalLikes})`;
            document.getElementById("save-btn").innerHTML =
                `<i class="fa-solid fa-bookmark"></i> ${data.userSavedThis ? "Saved" : "Save"} (${data.totalSaves})`;
            document.getElementById("view-btn").innerHTML =
                `<i class="fa-solid fa-eye"></i> View (${data.totalView})`;
        }
    } catch (err) {
        console.error("fetchInteraction error:", err);
    }
}

async function toggleInteraction(movieId, userId, action) {
    try {
        let url = action === "LIKE"
            ? `http://localhost:8080/user/interaction-like?userId=${userId}&movieId=${movieId}`
            : `http://localhost:8080/user/interaction-save?userId=${userId}&movieId=${movieId}`;

        const res = await fetch(url, { method: "PUT" });
        const result = await res.json();

        if (result.status === "Success") {
            await fetchInteraction(movieId, userId);
            await loadWatchlist();
        }
    } catch (err) {
        console.error("toggleInteraction error:", err);
    }
}

function showMovieDetail(movie) {
    if (!movie) return;
    currentMovieId = movie.movieId;

    document.getElementById('detail-img').src = movie.posterUrl || movie.thumbUrl || '';
    document.getElementById('detail-title').innerText = movie.title || '';
    document.getElementById('detail-desc').innerText = movie.description || '';
    document.getElementById('detail-year').innerText = movie.releaseYear || '';
    document.getElementById('detail-country').innerText = movie.country || '';
    document.getElementById('detail-language').innerText = movie.language || '';
    document.getElementById('detail-status').innerText = movie.movieStatus || '';
    document.getElementById('detail-trailer').href = movie.trailerUrl || '#';

    fetchInteraction(movie.movieId, userId);
    document.getElementById('movie-detail').style.display = 'block';

    fetchEpisodes(movie.movieId).then(episodes => {
        const container = document.getElementById('episode-list');
        container.innerHTML = '';

        if (episodes.status === 'Error' || !episodes.data?.data || episodes.data.data.length === 0) {
            container.innerHTML = '<div style="color: #666; text-align: center;">Lỗi tải tập phim!</div>';
            return;
        }

        episodes.data.data.forEach(ep => {
            const div = document.createElement('div');
            div.className = 'episode-card';

            const a = document.createElement('a');
            a.href = ep.videoUrl;
            a.target = "_blank";
            a.textContent = ep.name;

            a.addEventListener('click', async () => {
                await fetchApi(`http://localhost:8080/user/interaction-watch?userId=${userId}&movieId=${currentMovieId}`, { method: 'PUT' });
                await fetchInteraction(currentMovieId, userId);
            });

            div.appendChild(a);
            container.appendChild(div);
        });
    });

    fetchComments(movie.movieId).then(comments => {
        const container = document.getElementById('comment-list');
        container.innerHTML = '';

        if (comments.status === "Error" || !comments.data?.data || comments.data.data.length === 0) {
            container.innerHTML = '<p class="no-comment">Chưa có bình luận nào.</p>';
            return;
        }

        comments.data.data.forEach(c => {
            const div = document.createElement('div');
            div.className = 'comment-item';
            div.innerHTML = `
                <div class="comment-header">
                    <span class="comment-user">👤${c.username}</span>
                    <span class="comment-time">${new Date(c.createdAt).toLocaleString('vi-VN')}</span>
                </div>
                <div class="comment-content">${c.content}</div>
            `;
            container.appendChild(div);
        });
    });
}

function closeMovieDetail() {
    document.getElementById('movie-detail').style.display = 'none';
    document.getElementById('comment-input').value = '';
    currentMovieId = null;
}

async function submitComment() {
    if (!currentMovieId || !userId) {
        alert("Thiếu thông tin!");
        return;
    }

    const content = document.getElementById('comment-input').value.trim();
    if (!content) {
        alert('Nhập nội dung bình luận!');
        return;
    }

    const json = await fetchApi(`http://localhost:8080/user/add-comment`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            userId: parseInt(userId),
            movieId: currentMovieId,
            content: content
        })
    });

    if (json.status?.toLowerCase() === 'success') {
        document.getElementById('comment-input').value = '';
        showMovieDetail(await fetchMovieById(currentMovieId));
    } else {
        alert('Gửi comment thất bại!');
    }
}


// Hàm xử lý đăng xuất - MỚI
function handleLogout() {
    // Hiển thị confirmation dialog
    if (confirm('Bạn có chắc chắn muốn đăng xuất?')) {
        // Clear session storage nếu có
        sessionStorage.clear();

        // Clear local storage nếu có
        localStorage.clear();

        // Redirect về trang login
        window.location.href = '/user/login';
    }
}
// Event listeners
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("like-btn")?.addEventListener("click", () => {
        if (currentMovieId) toggleInteraction(currentMovieId, userId, "LIKE");
    });

    document.getElementById("save-btn")?.addEventListener("click", () => {
        if (currentMovieId) toggleInteraction(currentMovieId, userId, "SAVE");
    });

    document.getElementById("logoutBtn")?.addEventListener("click", handleLogout);

    loadUserInfo();
    loadWatchlist();
});


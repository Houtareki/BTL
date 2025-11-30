
const params = new URLSearchParams(window.location.search);
const userId = parseInt(params.get("userId"));
const countryName = params.get("country");

function goHome() {
    window.location.href = `home?userId=${userId}`;
}

async function fetchApi(url, options = {}) {
    try {
        const res = await fetch(url, options);
        return await res.json();
    } catch (err) {
        console.error(err);
        return null;
    }
}

async function loadMoviesByCountry() {
    if (!countryName) {
        document.getElementById("page-title").innerText = "Không có quốc gia hợp lệ!";
        return;
    }

    const json = await fetchApi(`http://localhost:8080/user/get-all-movies-by-country?userId=${userId}&country=${encodeURIComponent(countryName)}`);
    if (!json || json.status !== "Success") {
        document.getElementById("page-title").innerText = "Lỗi tải phim!";
        return;
    }

    const movies = json.data?.data || [];
    document.getElementById("page-title").innerText = `Quốc gia: ${countryName.replaceAll("_", " ")}`;

    const container = document.getElementById("movies");
    container.innerHTML = "";



    movies.forEach(movie => {
        const card = document.createElement('div');
        card.className = 'movie-card';

        card.innerHTML = `
            <img src="${movie.thumbUrl}" alt="${movie.title}" title="${movie.title}">
            <div class="overlay">
                <h4>${movie.title}</h4>
                <p>${movie.description || 'Chưa có mô tả'}</p>
            </div>
        `;
        card.addEventListener('click', () => showMovieDetail(movie));
        container.appendChild(card);
    });
}

function closeMovieDetail() {
    document.getElementById('movie-detail').style.display = 'none';
}

async function fetchMovieById(movieId) { const json = await fetchApi(`http://localhost:8080/user/get-movie?movieId=${movieId}`); return json.data?.data || null; }
async function fetchEpisodes(movieId) { const json = await fetchApi(`http://localhost:8080/user/get-episodes?movieId=${movieId}`); return Array.isArray(json.data?.data) ? json.data.data : [json.data?.data]; }
async function fetchComments(movieId) { const json = await fetchApi(`http://localhost:8080/user/get-comments-by-movieId?userId=${userId}&movieId=${movieId}`); return Array.isArray(json.data?.data) ? json.data.data : [json.data?.data]; }


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

        episodes.forEach(ep => {
            const div = document.createElement('div');
            div.className = 'episode-card';

            // Tạo link nhưng xử lý nhấn qua JS để chèn logic update view
            const a = document.createElement('a');
            a.href = ep.videoUrl;
            a.target = "_blank";
            a.textContent = ep.name;

            a.addEventListener('click', async (e) => {
                // Gọi API update lượt xem
                await fetchApi(`http://localhost:8080/user/interaction-watch?userId=${parseInt(userId)}&movieId=${currentMovieId}`, { method: 'PUT' });

                // Sau khi update thì gọi lại API lấy movie mới
                //const updatedMovie = await fetchMovieById(movie.movieId);
                const updatedMovie = await fetchInteraction(currentMovieId, parseInt(userId));
            });

            div.appendChild(a);
            container.appendChild(div);
        });
    });


    fetchComments(movie.movieId).then(comments => {
        const container = document.getElementById('comment-list');
        container.innerHTML = '';

        if (comments.length === 0) {
            container.innerHTML = '<p class="no-comment">Chưa có bình luận nào. Hãy là người đầu tiên!</p>';
            return;
        }

        comments.forEach(c => {
            const div = document.createElement('div');
            div.className = 'comment-item';
            div.innerHTML = `
            <div class="comment-header">
                <span class="comment-user">👤 User ${c.userId}</span>
                <span class="comment-time">${new Date(c.createdAt).toLocaleString('vi-VN')}</span>
            </div>
            <div class="comment-content">${c.content}</div>
        `;
            container.appendChild(div);
        });
    });

}

async function submitComment() {
    if (!currentMovieId || !userId) {
        alert("Thiếu thông tin userId hoặc movieId!");
        return;
    }

    const content = document.getElementById('comment-input').value.trim();
    if (!content) {
        alert('Nhập nội dung bình luận!');
        return;
    }

    const body = {
        userId: parseInt(userId, 10),
        movieId: currentMovieId,
        content: content
    };

    const json = await fetchApi(`http://localhost:8080/user/add-comment`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });

    if (json.status && json.status.toLowerCase() === 'success') {
        document.getElementById('comment-input').value = '';
        // load lại movie detail để refresh comment
        showMovieDetail(await fetchMovieById(currentMovieId));
    } else {
        alert('Gửi comment thất bại!');
    }
}

async function fetchInteraction(movieId, userId) {
    try {
        const res = await fetch(`http://localhost:8080/user/get-movie-interaction?userId=${userId}&movieId=${movieId}`);
        if (!res.ok) throw new Error("HTTP error " + res.status);

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
        let interactionUrl = '';
        if (action === "LIKE") {
            interactionUrl = `http://localhost:8080/user/interaction-like?userId=${userId}&movieId=${movieId}`;
        }
        else if (action === "SAVE") {
            interactionUrl = `http://localhost:8080/user/interaction-save?userId=${userId}&movieId=${movieId}`;
        }

        const res = await fetch(interactionUrl, {
            method: "PUT",
        });
        const result = await res.json();
        if (result.status === "Success") {
            await fetchInteraction(movieId, userId);
        }
    } catch (err) {
        console.error("toggleInteraction error:", err);
    }

}

// Gắn sự kiện click
document.addEventListener("DOMContentLoaded", () => {
    const likeBtn = document.getElementById("like-btn");
    const saveBtn = document.getElementById("save-btn");
    if (likeBtn) {
        likeBtn.addEventListener("click", () => {
            if (currentMovieId) toggleInteraction(currentMovieId, userId, "LIKE");
        });
    }
    if (saveBtn) {
        saveBtn.addEventListener("click", () => {
            if (currentMovieId) toggleInteraction(currentMovieId, userId, "SAVE");
        });
    }

});

loadMoviesByCountry();


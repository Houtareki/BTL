document.addEventListener('DOMContentLoaded', () => {

    const modal = document.getElementById('movieModal');
    const openAddModalBtn = document.getElementById('openAddModalBtn');
    const closeModalBtn = document.getElementById('closeModalBtn');
    const movieForm = document.getElementById('movieForm');
    const movieTableBody = document.getElementById('movieTableBody');
    const modalTitle = document.getElementById('modalTitle');
    const movieIdInput = document.getElementById('movieId'); 
    const btnPrevious = document.getElementById('btnPrevious');
    const btnNext = document.getElementById('btnNext');
    const pageIndicator = document.getElementById('pageIndicator');
    const posterUrlInput = document.getElementById('posterUrl');
    const posterPreview = document.getElementById('posterPreview');
    const posterPlaceholder = document.getElementById('posterPlaceholder');
    const btnUploadPoster = document.getElementById('btnUploadPoster');
    const posterFileInput = document.getElementById('posterFileInput');

    const API_BASE_URL = '/api/admin/movies';
    const API_ACTORS_URL = '/api/actors';
    const API_GENRES_URL = '/api/genres';

    let allMoviesData = [];
    let currentPage = 0;
    let totalPages = 0;
    let pageSize = 10;
    
    // Lưu trữ danh sách actors, genres và episodes hiện tại
    let currentActors = [];
    let currentGenres = [];
    let currentEpisodes = [];

    // Toast notification function
    function showToast(message, type = 'info') {
        const toastContainer = document.getElementById('toastContainer');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        
        const icons = {
            success: '✓',
            error: '✕',
            warning: '⚠',
            info: 'ℹ'
        };
        
        toast.innerHTML = `
            <span class="toast-icon">${icons[type] || icons.info}</span>
            <span class="toast-message">${message}</span>
            <button class="toast-close" onclick="this.parentElement.remove()">×</button>
        `;
        
        toastContainer.appendChild(toast);
        
        // Auto remove after 4 seconds
        setTimeout(() => {
            toast.classList.add('removing');
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    }
    
    // Lưu trữ tất cả actors và genres từ DB
    let allActors = [];
    let allGenres = [];

    async function fetchAndRenderMovies(page = 0, size = 10) {
        try {
            const response = await fetch(`${API_BASE_URL}?page=${page}&size=${size}&sortBy=movieId&sortDirection=desc`, {
                method: 'GET',
            });

            if (!response.ok) {
                throw new Error(`Lỗi HTTP: ${response.status}`);
            }

            const pageData = await response.json();
            
            // Xử lý dữ liệu phân trang từ Spring Data Page
            allMoviesData = pageData.content; // Lấy danh sách phim từ content
            currentPage = pageData.number; // Trang hiện tại
            totalPages = pageData.totalPages; // Tổng số trang
            pageSize = pageData.size; // Kích thước trang

            movieTableBody.innerHTML = '';

            if (allMoviesData.length === 0) {
                movieTableBody.innerHTML = '<tr><td colspan="5" style="text-align:center;">Không có phim nào</td></tr>';
                return;
            }

            allMoviesData.forEach(movie => {
                const row = `
                    <tr data-id="${movie.movieId}">
                        <td>${movie.movieId}</td>
                        <td>${movie.title}</td>
                        <td>${movie.releaseYear || 'N/A'}</td>
                        <td>${movie.country || 'N/A'}</td>
                        <td class="action-buttons">
                            <button class="btn btn-edit">Sửa</button>
                            <button class="btn btn-delete">Xóa</button>
                        </td>
                    </tr>
                `;
                movieTableBody.innerHTML += row;
            });

            // Cập nhật thông tin phân trang
            updatePaginationInfo();
            updatePaginationButtons();

        } catch (error) {
            console.error('Lỗi khi tải danh sách phim:', error);
            showToast('Không thể tải danh sách phim. Vui lòng kiểm tra console.', 'error');
        }
    }

    function updatePaginationInfo() {
        const paginationInfo = document.getElementById('paginationInfo');
        if (paginationInfo) {
            paginationInfo.textContent = `Hiển thị ${allMoviesData.length} phim - Tổng ${totalPages} trang`;
        }
        
        if (pageIndicator) {
            pageIndicator.textContent = `Trang ${currentPage + 1} / ${totalPages}`;
        }
    }

    function updatePaginationButtons() {
        // Disable/Enable nút Previous
        if (btnPrevious) {
            btnPrevious.disabled = currentPage === 0;
        }
        
        // Disable/Enable nút Next
        if (btnNext) {
            btnNext.disabled = currentPage >= totalPages - 1;
        }
    }

    function goToPreviousPage() {
        if (currentPage > 0) {
            fetchAndRenderMovies(currentPage - 1, pageSize);
        }
    }

    function goToNextPage() {
        if (currentPage < totalPages - 1) {
            fetchAndRenderMovies(currentPage + 1, pageSize);
        }
    }

    // Helper functions cho poster preview
    function updatePosterPreview(url) {
        if (url) {
            // Hiển thị placeholder với loading
            posterPreview.style.display = 'none';
            posterPlaceholder.style.display = 'flex';
            posterPlaceholder.innerHTML = '<span class="placeholder-icon">⏳</span><span class="placeholder-text">Đang tải ảnh...</span>';
            
            // Đợi 0.5 giây trước khi load ảnh
            setTimeout(() => {
                posterPreview.src = url;
                posterPreview.style.display = 'block';
                posterPlaceholder.style.display = 'none';
                
                // Xử lý lỗi load ảnh
                posterPreview.onerror = () => {
                    posterPreview.style.display = 'none';
                    posterPlaceholder.style.display = 'flex';
                    posterPlaceholder.innerHTML = '<span>❌ Không thể tải ảnh</span>';
                };
            }, 500); // Đợi 500ms = 0.5 giây
        } else {
            posterPreview.style.display = 'none';
            posterPlaceholder.style.display = 'flex';
            posterPlaceholder.innerHTML = '<span>🖼️ Chưa có ảnh</span>';
        }
    }

    function openModalForAdd() {
        modalTitle.textContent = 'Thêm Phim Mới';
        movieForm.reset(); 
        movieIdInput.value = ''; 
        
        // Reset poster preview
        posterPreview.style.display = 'none';
        posterPlaceholder.style.display = 'flex';
        posterPlaceholder.innerHTML = '<span class="placeholder-icon">🖼️</span><span class="placeholder-text">Chưa có ảnh</span>';
        
        // Reset file input
        if (posterFileInput) {
            posterFileInput.value = '';
        }
        
        // Hiển thị section episodes với danh sách rỗng
        currentEpisodes = [];
        renderEpisodes([]);
        
        // Hiển thị actors và genres sections với danh sách rỗng
        // Người dùng có thể thêm actors/genres ngay khi tạo phim mới
        currentActors = [];
        currentGenres = [];
        
        // Render sections để hiển thị nút "Thêm"
        const actorsSection = document.getElementById('actorsSection');
        const actorsList = document.getElementById('actorsList');
        actorsSection.style.display = 'block';
        actorsList.innerHTML = '<div class="empty-state">Chưa có diễn viên. Nhấn "➕ Thêm Diễn Viên" để thêm.</div>';
        
        const genresSection = document.getElementById('genresSection');
        const genresList = document.getElementById('genresList');
        genresSection.style.display = 'block';
        genresList.innerHTML = '<div class="empty-state">Chưa có thể loại. Nhấn "➕ Thêm Thể Loại" để thêm.</div>';
        
        modal.style.display = 'block';
    }

    async function openModalForEdit(movieId) {
        try {
            // Gọi API detail để lấy đầy đủ thông tin
            const response = await fetch(`${API_BASE_URL}/${movieId}/detail`, {
                method: 'GET',
            });

            if (!response.ok) {
                throw new Error(`Lỗi HTTP: ${response.status}`);
            }

            const result = await response.json();
            const movieDetail = result.data.data; // Lấy data từ CustomResponse

            modalTitle.textContent = `Sửa Phim (ID: ${movieDetail.movieId})`;
            
            // Điền thông tin cơ bản
            movieIdInput.value = movieDetail.movieId;
            document.getElementById('title').value = movieDetail.title;
            document.getElementById('description').value = movieDetail.description || '';
            document.getElementById('releaseYear').value = movieDetail.releaseYear || '';
            document.getElementById('country').value = movieDetail.country || '';
            document.getElementById('posterUrl').value = movieDetail.posterUrl || '';

            // Cập nhật preview poster
            updatePosterPreview(movieDetail.posterUrl || '');

            // Hiển thị episodes
            renderEpisodes(movieDetail.episodes);

            // Hiển thị actors
            renderActors(movieDetail.actors);

            // Hiển thị genres
            renderGenres(movieDetail.genres);

            modal.style.display = 'block';
        } catch (error) {
            console.error('Lỗi khi tải chi tiết phim:', error);
            showToast('Không thể tải thông tin chi tiết phim.', 'error');
        }
    }

    function renderEpisodes(episodes) {
        const episodesSection = document.getElementById('episodesSection');
        const episodesList = document.getElementById('episodesList');

        // Lưu vào biến global
        currentEpisodes = episodes ? [...episodes] : [];

        // Luôn hiển thị section
        episodesSection.style.display = 'block';
        episodesList.innerHTML = '';

        if (!currentEpisodes || currentEpisodes.length === 0) {
            episodesList.innerHTML = '<div class="empty-state">Chưa có tập phim. Nhấn "➕ Thêm Tập Phim" để thêm.</div>';
            return;
        }

        currentEpisodes.forEach((episode, index) => {
            const episodeItem = document.createElement('div');
            episodeItem.className = 'episode-item';
            episodeItem.innerHTML = `
                <div class="episode-info">
                    <div class="episode-name">${episode.name}</div>
                    <div class="episode-url">${episode.videoUrl || 'Chưa có URL'}</div>
                </div>
                <div class="episode-actions">
                    <button type="button" class="btn-edit-episode" data-index="${index}">✏️ Sửa</button>
                    <button type="button" class="btn-delete-episode" data-index="${index}">🗑️ Xóa</button>
                </div>
            `;
            episodesList.appendChild(episodeItem);
        });

        // Thêm event listeners cho các nút sửa/xóa
        episodesList.querySelectorAll('.btn-edit-episode').forEach(btn => {
            btn.addEventListener('click', function() {
                const index = parseInt(this.dataset.index);
                openEpisodeModalForEdit(index);
            });
        });

        episodesList.querySelectorAll('.btn-delete-episode').forEach(btn => {
            btn.addEventListener('click', function() {
                const index = parseInt(this.dataset.index);
                deleteEpisode(index);
            });
        });
    }

    function renderActors(actors) {
        const actorsSection = document.getElementById('actorsSection');
        const actorsList = document.getElementById('actorsList');

        // Lưu vào biến global
        currentActors = actors ? [...actors] : [];

        actorsSection.style.display = 'block';
        actorsList.innerHTML = '';

        if (!currentActors || currentActors.length === 0) {
            actorsList.innerHTML = '<div class="empty-state">Chưa có diễn viên</div>';
            return;
        }

        currentActors.forEach((actor, index) => {
            const actorTag = document.createElement('span');
            actorTag.className = 'tag-item actor';
            actorTag.innerHTML = `
                <span class="tag-name">🎭 ${actor.name}</span>
                <button type="button" class="btn-remove-tag" data-index="${index}" data-type="actor">×</button>
            `;
            actorsList.appendChild(actorTag);
        });

        // Thêm event listeners cho các nút xóa
        actorsList.querySelectorAll('.btn-remove-tag').forEach(btn => {
            btn.addEventListener('click', function() {
                const index = parseInt(this.dataset.index);
                removeActor(index);
            });
        });
    }

    function renderGenres(genres) {
        const genresSection = document.getElementById('genresSection');
        const genresList = document.getElementById('genresList');

        // Lưu vào biến global
        currentGenres = genres ? [...genres] : [];

        genresSection.style.display = 'block';
        genresList.innerHTML = '';

        if (!currentGenres || currentGenres.length === 0) {
            genresList.innerHTML = '<div class="empty-state">Chưa có thể loại</div>';
            return;
        }

        currentGenres.forEach((genre, index) => {
            const genreTag = document.createElement('span');
            genreTag.className = 'tag-item genre';
            genreTag.innerHTML = `
                <span class="tag-name">🎬 ${genre.name}</span>
                <button type="button" class="btn-remove-tag" data-index="${index}" data-type="genre">×</button>
            `;
            genresList.appendChild(genreTag);
        });

        // Thêm event listeners cho các nút xóa
        genresList.querySelectorAll('.btn-remove-tag').forEach(btn => {
            btn.addEventListener('click', function() {
                const index = parseInt(this.dataset.index);
                removeGenre(index);
            });
        });
    }

    function closeModal() {
        modal.style.display = 'none';
        movieForm.reset();
    }

    async function handleFormSubmit(event) {
        event.preventDefault(); 

        const movieData = {
            title: document.getElementById('title').value,
            description: document.getElementById('description').value,
            releaseYear: document.getElementById('releaseYear').value,
            country: document.getElementById('country').value,
            posterUrl: document.getElementById('posterUrl').value,
            language: "N/A", 
            trailerUrl: "",
            thumbUrl: "",
            movieStatus: "showing" 
        };

        const id = movieIdInput.value; 

        try {
            let response;
            if (id) {
                // Khi update, gửi kèm actorIds và genreIds
                movieData.actorIds = currentActors.map(actor => actor.actorId);
                movieData.genreIds = currentGenres.map(genre => genre.genreId);
                
                response = await fetch(`${API_BASE_URL}/${id}`, {
                    method: 'PUT',
                    headers: { 
                        'Content-Type': 'application/json',
                        
                    },
                    body: JSON.stringify(movieData)
                });
            } else {
                // Khi tạo mới, gửi kèm actorIds và genreIds (giống như update)
                movieData.actorIds = currentActors.map(actor => actor.actorId);
                movieData.genreIds = currentGenres.map(genre => genre.genreId);
                
                response = await fetch(API_BASE_URL, {
                    method: 'POST',
                    headers: { 
                        'Content-Type': 'application/json',
                        
                    },
                    body: JSON.stringify(movieData)
                });
            }

            if (!response.ok) {
                
                const errorData = await response.json();
                throw new Error(errorData.message || `Lỗi HTTP ${response.status}`);
            }

            const result = await response.json();
            showToast(result.message, 'success'); 
            
            closeModal();
            fetchAndRenderMovies(); 

        } catch (error) {
            console.error('Lỗi khi lưu phim:', error);
            showToast(`Lỗi: ${error.message}`, 'error');
        }
    }

    async function handleTableClick(event) {
        const target = event.target;
        const row = target.closest('tr');
        if (!row) return;

        const id = row.dataset.id; 

        if (target.classList.contains('btn-edit')) {
            openModalForEdit(id);
        }

        if (target.classList.contains('btn-delete')) {
            if (confirm(`Bạn có chắc muốn xóa phim ID ${id} không?`)) {
                try {
                    const response = await fetch(`${API_BASE_URL}/${id}`, {
                        method: 'DELETE',
                        headers: {
                            
                        }
                    });

                    if (!response.ok) {
                        const errorData = await response.json();
                        throw new Error(errorData.message || `Lỗi HTTP ${response.status}`);
                    }
                    
                    const result = await response.json();
                    showToast(result.message, 'success'); 

                    fetchAndRenderMovies(); 

                } catch (error) {
                    console.error('Lỗi khi xóa phim:', error);
                    showToast(`Lỗi: ${error.message}`, 'error');
                }
            }
        }
    }

    openAddModalBtn.addEventListener('click', openModalForAdd);

    closeModalBtn.addEventListener('click', closeModal);

    modal.addEventListener('click', (event) => {
        if (event.target === modal) {
            closeModal();
        }
    });

    movieForm.addEventListener('submit', handleFormSubmit);

    movieTableBody.addEventListener('click', handleTableClick);

    // Event listeners cho nút phân trang
    if (btnPrevious) {
        btnPrevious.addEventListener('click', goToPreviousPage);
    }

    if (btnNext) {
        btnNext.addEventListener('click', goToNextPage);
    }

    // Event listeners cho poster upload
    if (posterUrlInput) {
        posterUrlInput.addEventListener('input', handlePosterUrlChange);
    }

    if (btnUploadPoster) {
        btnUploadPoster.addEventListener('click', () => {
            posterFileInput.click();
        });
    }

    if (posterFileInput) {
        posterFileInput.addEventListener('change', handleFileUpload);
    }

    // Handler cho poster input change và file upload
    function handlePosterUrlChange(event) {
        const url = event.target.value.trim();
        updatePosterPreview(url);
    }

    async function handleFileUpload(event) {
        const file = event.target.files[0];
        if (!file) return;

        // Kiểm tra file có phải là ảnh không
        if (!file.type.startsWith('image/')) {
            showToast('Vui lòng chọn file ảnh!', 'warning');
            return;
        }

        // Kiểm tra kích thước file (max 10MB)
        if (file.size > 10 * 1024 * 1024) {
            showToast('Kích thước ảnh không được vượt quá 10MB!', 'warning');
            return;
        }

        // Hiển thị loading state
        btnUploadPoster.disabled = true;
        btnUploadPoster.innerHTML = '<span>⏳</span><span>Đang tải lên...</span>';

        try {
            // Upload file lên server
            const formData = new FormData();
            formData.append('file', file);
            
            const response = await fetch('/api/upload/poster', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                throw new Error('Upload thất bại');
            }

            const result = await response.json();
            
            console.log('Response từ server:', result); // Debug
            
            if (result.status === 'Success' && result.url) {
                // Lưu URL từ server vào input
                posterUrlInput.value = result.url;
                updatePosterPreview(result.url);
                
                // Hiển thị thông báo thành công
                console.log('Upload thành công:', result.filename);
                console.log('URL ảnh:', result.url);
            } else {
                throw new Error(result.message || 'Upload thất bại');
            }
        } catch (error) {
            console.error('Lỗi khi upload ảnh:', error);
            showToast('Lỗi khi upload ảnh: ' + error.message, 'error');
            
            // Xóa file đã chọn
            posterFileInput.value = '';
        } finally {
            // Restore button state
            btnUploadPoster.disabled = false;
            btnUploadPoster.innerHTML = '<span class="upload-icon">📤</span><span>Chọn Ảnh</span>';
        }
    }

    // Fetch tất cả actors từ API
    async function fetchAllActors() {
        try {
            const response = await fetch(API_ACTORS_URL);
            if (!response.ok) throw new Error('Không thể tải danh sách diễn viên');
            
            const result = await response.json();
            allActors = result.data.data || [];
        } catch (error) {
            console.error('Lỗi khi tải actors:', error);
            allActors = [];
        }
    }

    // Fetch tất cả genres từ API
    async function fetchAllGenres() {
        try {
            const response = await fetch(API_GENRES_URL);
            if (!response.ok) throw new Error('Không thể tải danh sách thể loại');
            
            const result = await response.json();
            allGenres = result.data.data || [];
        } catch (error) {
            console.error('Lỗi khi tải genres:', error);
            allGenres = [];
        }
    }

    // Hàm mở modal chọn diễn viên
    function addActor() {
        const actorModal = document.getElementById('actorSelectModal');
        const actorSearchInput = document.getElementById('actorSearchInput');
        const actorSelectList = document.getElementById('actorSelectList');
        
        actorModal.style.display = 'block';
        actorSearchInput.value = '';
        
        renderActorSelectList(allActors);
    }

    // Render danh sách actors để chọn
    function renderActorSelectList(actors) {
        const actorSelectList = document.getElementById('actorSelectList');
        actorSelectList.innerHTML = '';
        
        if (!actors || actors.length === 0) {
            actorSelectList.innerHTML = '<div class="empty-state">Không có diễn viên nào</div>';
            return;
        }
        
        actors.forEach(actor => {
            // Kiểm tra xem actor đã được chọn chưa
            const isSelected = currentActors.some(a => a.actorId === actor.actorId);
            
            const item = document.createElement('div');
            item.className = `select-item ${isSelected ? 'selected' : ''}`;
            item.innerHTML = `
                <span class="select-item-icon">🎭</span>
                <span class="select-item-name">${actor.name}</span>
            `;
            
            if (!isSelected) {
                item.addEventListener('click', () => {
                    currentActors.push(actor);
                    renderActors(currentActors);
                    closeActorModal();
                });
            }
            
            actorSelectList.appendChild(item);
        });
    }

    // Hàm đóng modal actor
    function closeActorModal() {
        document.getElementById('actorSelectModal').style.display = 'none';
    }

    // Hàm xóa diễn viên
    function removeActor(index) {
        if (confirm('Bạn có chắc muốn xóa diễn viên này?')) {
            currentActors.splice(index, 1);
            renderActors(currentActors);
        }
    }

    // Hàm mở modal chọn thể loại
    function addGenre() {
        const genreModal = document.getElementById('genreSelectModal');
        const genreSearchInput = document.getElementById('genreSearchInput');
        const genreSelectList = document.getElementById('genreSelectList');
        
        genreModal.style.display = 'block';
        genreSearchInput.value = '';
        
        renderGenreSelectList(allGenres);
    }

    // Render danh sách genres để chọn
    function renderGenreSelectList(genres) {
        const genreSelectList = document.getElementById('genreSelectList');
        genreSelectList.innerHTML = '';
        
        if (!genres || genres.length === 0) {
            genreSelectList.innerHTML = '<div class="empty-state">Không có thể loại nào</div>';
            return;
        }
        
        genres.forEach(genre => {
            // Kiểm tra xem genre đã được chọn chưa
            const isSelected = currentGenres.some(g => g.genreId === genre.genreId);
            
            const item = document.createElement('div');
            item.className = `select-item ${isSelected ? 'selected' : ''}`;
            item.innerHTML = `
                <span class="select-item-icon">🎬</span>
                <span class="select-item-name">${genre.name}</span>
            `;
            
            if (!isSelected) {
                item.addEventListener('click', () => {
                    currentGenres.push(genre);
                    renderGenres(currentGenres);
                    closeGenreModal();
                });
            }
            
            genreSelectList.appendChild(item);
        });
    }

    // Hàm đóng modal genre
    function closeGenreModal() {
        document.getElementById('genreSelectModal').style.display = 'none';
    }

    // Hàm xóa thể loại
    function removeGenre(index) {
        if (confirm('Bạn có chắc muốn xóa thể loại này?')) {
            currentGenres.splice(index, 1);
            renderGenres(currentGenres);
        }
    }

    // Event listeners cho nút thêm actors và genres
    const btnAddActor = document.getElementById('btnAddActor');
    const btnAddGenre = document.getElementById('btnAddGenre');

    if (btnAddActor) {
        btnAddActor.addEventListener('click', addActor);
    }

    if (btnAddGenre) {
        btnAddGenre.addEventListener('click', addGenre);
    }

    // Event listeners cho modal actors
    const closeActorModalBtn = document.getElementById('closeActorModalBtn');
    const actorSelectModal = document.getElementById('actorSelectModal');
    const actorSearchInput = document.getElementById('actorSearchInput');

    if (closeActorModalBtn) {
        closeActorModalBtn.addEventListener('click', closeActorModal);
    }

    if (actorSelectModal) {
        actorSelectModal.addEventListener('click', (e) => {
            if (e.target === actorSelectModal) {
                closeActorModal();
            }
        });
    }

    if (actorSearchInput) {
        actorSearchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            const filtered = allActors.filter(actor => 
                actor.name.toLowerCase().includes(searchTerm)
            );
            renderActorSelectList(filtered);
        });
    }

    // Event listeners cho modal genres
    const closeGenreModalBtn = document.getElementById('closeGenreModalBtn');
    const genreSelectModal = document.getElementById('genreSelectModal');
    const genreSearchInput = document.getElementById('genreSearchInput');

    if (closeGenreModalBtn) {
        closeGenreModalBtn.addEventListener('click', closeGenreModal);
    }

    if (genreSelectModal) {
        genreSelectModal.addEventListener('click', (e) => {
            if (e.target === genreSelectModal) {
                closeGenreModal();
            }
        });
    }

    if (genreSearchInput) {
        genreSearchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            const filtered = allGenres.filter(genre => 
                genre.name.toLowerCase().includes(searchTerm)
            );
            renderGenreSelectList(filtered);
        });
    }

    // ============ Episode Management ============
    const episodeModal = document.getElementById('episodeModal');
    const btnAddEpisode = document.getElementById('btnAddEpisode');
    const closeEpisodeModalBtn = document.getElementById('closeEpisodeModalBtn');
    const btnSaveEpisode = document.getElementById('btnSaveEpisode');
    const btnCancelEpisode = document.getElementById('btnCancelEpisode');
    const episodeModalTitle = document.getElementById('episodeModalTitle');
    const episodeEditIndex = document.getElementById('episodeEditIndex');
    const episodeName = document.getElementById('episodeName');
    const episodeVideoUrl = document.getElementById('episodeVideoUrl');
    const btnUploadVideo = document.getElementById('btnUploadVideo');
    const episodeVideoFileInput = document.getElementById('episodeVideoFileInput');
    const videoFileName = document.getElementById('videoFileName');
    const videoPreview = document.getElementById('videoPreview');
    const videoPlaceholder = document.getElementById('videoPlaceholder');

    function updateVideoPreview(url) {
        if (url) {
            videoPreview.src = url;
            videoPreview.style.display = 'block';
            videoPlaceholder.style.display = 'none';
            
            // Xử lý lỗi load video
            videoPreview.onerror = () => {
                videoPreview.style.display = 'none';
                videoPlaceholder.style.display = 'flex';
                videoPlaceholder.innerHTML = '<span class="placeholder-icon">❌</span><span class="placeholder-text">Không thể tải video</span>';
            };
        } else {
            videoPreview.style.display = 'none';
            videoPlaceholder.style.display = 'flex';
            videoPlaceholder.innerHTML = '<span class="placeholder-icon">🎬</span><span class="placeholder-text">Chưa có video</span>';
        }
    }

    function openEpisodeModalForAdd() {
        episodeModalTitle.textContent = 'Thêm Tập Phim';
        episodeEditIndex.value = '';
        episodeName.value = '';
        episodeVideoUrl.value = '';
        videoFileName.textContent = 'Chưa chọn file';
        episodeVideoFileInput.value = '';
        updateVideoPreview('');
        episodeModal.style.display = 'block';
    }

    function openEpisodeModalForEdit(index) {
        const episode = currentEpisodes[index];
        episodeModalTitle.textContent = 'Sửa Tập Phim';
        episodeEditIndex.value = index;
        episodeName.value = episode.name;
        episodeVideoUrl.value = episode.videoUrl || '';
        
        // Hiển thị tên file và preview nếu có URL
        if (episode.videoUrl) {
            const fileName = episode.videoUrl.split('/').pop();
            videoFileName.textContent = fileName || 'Video đã upload';
            updateVideoPreview(episode.videoUrl);
        } else {
            videoFileName.textContent = 'Chưa chọn file';
            updateVideoPreview('');
        }
        
        episodeVideoFileInput.value = '';
        episodeModal.style.display = 'block';
    }

    function closeEpisodeModal() {
        episodeModal.style.display = 'none';
        episodeName.value = '';
        episodeVideoUrl.value = '';
        episodeEditIndex.value = '';
        videoFileName.textContent = 'Chưa chọn file';
        episodeVideoFileInput.value = '';
        updateVideoPreview('');
    }

    // Handle video file selection
    if (btnUploadVideo) {
        btnUploadVideo.addEventListener('click', () => {
            episodeVideoFileInput.click();
        });
    }

    if (episodeVideoFileInput) {
        episodeVideoFileInput.addEventListener('change', handleVideoUpload);
    }

    async function handleVideoUpload(event) {
        const file = event.target.files[0];
        if (!file) return;

        // Kiểm tra file có phải là video không
        if (!file.type.startsWith('video/')) {
            showToast('Vui lòng chọn file video!', 'warning');
            return;
        }

        // Kiểm tra kích thước file (max 500MB)
        const maxSize = 500 * 1024 * 1024; // 500MB
        if (file.size > maxSize) {
            showToast('Kích thước video không được vượt quá 500MB!', 'warning');
            return;
        }

        // Hiển thị loading state
        btnUploadVideo.disabled = true;
        btnUploadVideo.innerHTML = '<span>⏳</span><span>Đang tải lên...</span>';
        videoFileName.textContent = 'Đang upload...';

        try {
            const formData = new FormData();
            formData.append('file', file);

            // Giả sử bạn có API endpoint để upload video
            const response = await fetch('/api/upload/video', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                throw new Error('Upload thất bại');
            }

            const result = await response.json();
            
            if (result.url) {
                // Lưu URL từ server vào input
                episodeVideoUrl.value = result.url;
                videoFileName.textContent = file.name;
                
                // Hiển thị preview video
                updateVideoPreview(result.url);
                
                showToast('Upload video thành công!', 'success');
                
                console.log('Upload video thành công:', result.filename);
                console.log('URL video:', result.url);
            } else {
                throw new Error(result.message || 'Upload thất bại');
            }
        } catch (error) {
            console.error('Lỗi khi upload video:', error);
            showToast('Lỗi khi upload video: ' + error.message, 'error');
            
            // Xóa file đã chọn
            episodeVideoFileInput.value = '';
            videoFileName.textContent = 'Chưa chọn file';
            updateVideoPreview('');
        } finally {
            // Restore button state
            btnUploadVideo.disabled = false;
            btnUploadVideo.innerHTML = '<span class="upload-icon">📤</span><span>Chọn Video</span>';
        }
    }

    async function saveEpisode() {
        const name = episodeName.value.trim();
        const videoUrl = episodeVideoUrl.value.trim();
        
        if (!name) {
            showToast('Vui lòng nhập tên tập phim!', 'warning');
            return;
        }

        const movieId = movieIdInput.value;
        if (!movieId) {
            // Nếu chưa có movieId (đang thêm phim mới), lưu vào local
            const editIndex = episodeEditIndex.value;
            
            if (editIndex !== '') {
                // Sửa episode local
                const index = parseInt(editIndex);
                currentEpisodes[index] = {
                    ...currentEpisodes[index],
                    name: name,
                    videoUrl: videoUrl
                };
                showToast('Đã cập nhật tập phim!', 'success');
            } else {
                // Thêm episode mới local
                currentEpisodes.push({
                    name: name,
                    videoUrl: videoUrl
                });
                showToast('Đã thêm tập phim mới!', 'success');
            }
            renderEpisodes(currentEpisodes);
            closeEpisodeModal();
            return;
        }

        // Nếu đã có movieId (đang sửa phim), gọi API
        const editIndex = episodeEditIndex.value;
        
        try {
            if (editIndex !== '') {
                // Sửa episode qua API
                const index = parseInt(editIndex);
                const episode = currentEpisodes[index];
                const episodeId = episode.episodeId;
                
                const response = await fetch(`/api/episodes/${episodeId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        name: name,
                        videoUrl: videoUrl,
                        movieId: parseInt(movieId)
                    })
                });

                if (!response.ok) {
                    throw new Error('Cập nhật tập phim thất bại');
                }

                const result = await response.json();
                showToast(result.message, 'success');
                
                // Cập nhật local
                currentEpisodes[index] = {
                    ...currentEpisodes[index],
                    name: name,
                    videoUrl: videoUrl
                };
            } else {
                // Thêm episode mới qua API
                const response = await fetch('/api/episodes', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        name: name,
                        videoUrl: videoUrl,
                        movieId: parseInt(movieId)
                    })
                });

                if (!response.ok) {
                    throw new Error('Thêm tập phim thất bại');
                }

                const result = await response.json();
                showToast(result.message, 'success');
                
                // Thêm vào local với ID từ server
                const newEpisode = result.data.data;
                currentEpisodes.push(newEpisode);
            }
            
            renderEpisodes(currentEpisodes);
            closeEpisodeModal();
        } catch (error) {
            console.error('Lỗi khi lưu tập phim:', error);
            showToast('Lỗi: ' + error.message, 'error');
        }
    }

    async function deleteEpisode(index) {
        const episode = currentEpisodes[index];
        const movieId = movieIdInput.value;
        
        if (!confirm(`Bạn có chắc muốn xóa tập "${episode.name}" không?`)) {
            return;
        }

        // Nếu chưa có movieId hoặc episode chưa có ID (local), xóa local
        if (!movieId || !episode.episodeId) {
            currentEpisodes.splice(index, 1);
            renderEpisodes(currentEpisodes);
            showToast('Đã xóa tập phim!', 'success');
            return;
        }

        // Nếu đã có ID, gọi API xóa
        try {
            const response = await fetch(`/api/episodes/${episode.episodeId}`, {
                method: 'DELETE'
            });

            if (!response.ok) {
                throw new Error('Xóa tập phim thất bại');
            }

            const result = await response.json();
            showToast(result.message, 'success');
            
            // Xóa khỏi local
            currentEpisodes.splice(index, 1);
            renderEpisodes(currentEpisodes);
        } catch (error) {
            console.error('Lỗi khi xóa tập phim:', error);
            showToast('Lỗi: ' + error.message, 'error');
        }
    }

    // Event listeners for episode modal
    if (btnAddEpisode) {
        btnAddEpisode.addEventListener('click', openEpisodeModalForAdd);
    }

    if (closeEpisodeModalBtn) {
        closeEpisodeModalBtn.addEventListener('click', closeEpisodeModal);
    }

    if (btnCancelEpisode) {
        btnCancelEpisode.addEventListener('click', closeEpisodeModal);
    }

    if (btnSaveEpisode) {
        btnSaveEpisode.addEventListener('click', saveEpisode);
    }

    if (episodeModal) {
        episodeModal.addEventListener('click', (e) => {
            if (e.target === episodeModal) {
                closeEpisodeModal();
            }
        });
    }

    // Load data ban đầu
    fetchAllActors();
    fetchAllGenres();
    fetchAndRenderMovies();
});

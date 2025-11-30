package com.example.demo.Service;

import com.example.demo.Entity.Episode;
import com.example.demo.Model.CommonModel.EpisodeAndMovieTitle;
import com.example.demo.Respository.EpisodeRepo;
import com.example.demo.Respository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EpisodeService {

    @Autowired
    private EpisodeRepo episodeRepo;

    @Autowired
    private MovieRepo movieRepo;

    private static final int PAGE_SIZE = 10;

    public Page<EpisodeAndMovieTitle> searchEpisode(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE,
                Sort.by(Sort.Order.asc("movie_id"),
                        Sort.Order.asc("episode_id"))
        );

        return episodeRepo.searchByNameWithMovieTitle(keyword, pageable);
    }

    public Optional<Episode> getEpisodeById(int id) {
        return episodeRepo.findById(id);
    }

    public Page<Episode> getUnlinkedEpisodes(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE,
                Sort.by(Sort.Order.asc("movie_id"),
                        Sort.Order.asc("episode_id"))
        );

        return episodeRepo.findUnlinkedEpisodes(pageable);
    }

    public Episode addEpisode(Episode episode) {
        int movieId = episode.getMovieId();

        if (movieId != -1 && !movieRepo.existsById(movieId)) {
            return null;
        }

        return episodeRepo.save(episode);
    }

    public Episode updateEpisode(int id, Episode episode) {
        Optional<Episode> existingEpisode = episodeRepo.findById(id);

        if (existingEpisode.isEmpty()) {
            return null;
        }

        int movieId = existingEpisode.get().getMovieId();
        if (movieId != -1 && !movieRepo.existsById(movieId)) {
            return null;
        }

        Episode updatedEpisode = existingEpisode.get();
        updatedEpisode.setMovieId(episode.getMovieId());
        updatedEpisode.setName(episode.getName());
        updatedEpisode.setVideoUrl(episode.getVideoUrl());

        return episodeRepo.save(updatedEpisode);
    }

    public boolean deleteEpisode(int id) {
        if (!episodeRepo.existsById(id)) {
            return false;
        }

        try {
            episodeRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

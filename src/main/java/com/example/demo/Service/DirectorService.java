package com.example.demo.Service;

import com.example.demo.Entity.Director;
import com.example.demo.Respository.DirectorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DirectorService {

    @Autowired
    private DirectorRepo directorRepo;

    private static final int PAGE_SIZE = 10;

    public Page<Director> getAllDirectors(int pageNo){
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "director_id"));
        return directorRepo.getAllDirectors(pageable);
    }

    public Page<Director> searchDirectors(String keyword, int pageNo){
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "director_id"));
        return directorRepo.searchDirectorsByName(keyword, pageable);
    }

    public Optional<Director> findById(int id){
        return directorRepo.findById(id);
    }

    public Director addDirector(Director director){
        return directorRepo.save(director);
    }

    public Director updateDirector(int id, Director director){
        Optional<Director> existingDirector = directorRepo.findById(id);
        if (existingDirector.isEmpty()){
            return null;
        }

        Director updatedDirector = existingDirector.get();
        updatedDirector.setName(director.getName());
        return directorRepo.save(updatedDirector);
    }

    public boolean deleteDirector(int id){
        if (!directorRepo.existsById(id)){
            return false;
        }

        try {
            directorRepo.deleteById(id);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}

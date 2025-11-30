package com.example.demo.Service;

import com.example.demo.Entity.Actor;
import com.example.demo.Respository.ActorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActorService {

    @Autowired
    private ActorRepo actorRepo;

    private static final int PAGE_SIZE = 10;

    public Page<Actor> getAllActors(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "actor_id"));
        return actorRepo.getAllActors(pageable);
    }

    public Page<Actor> searchActors(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "actor_id"));
        return actorRepo.searchActorsByName(keyword, pageable);
    }

    public Optional<Actor> getActorById(int id) {
        return actorRepo.findById(id);
    }

    public Actor addActor(Actor actor) {
        return actorRepo.save(actor);
    }

    public Actor updateActor(int id, Actor actor) {
        Optional<Actor> existingActor = actorRepo.findById(id);
        if (existingActor.isEmpty()) {
            return null;
        }

        Actor updatedActor = existingActor.get();
        updatedActor.setName(actor.getName());
        return actorRepo.save(updatedActor);
    }

    public boolean deleteActor(int id) {
        if (!actorRepo.existsById(id)) {
            return false;
        }

        try {
            actorRepo.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


}

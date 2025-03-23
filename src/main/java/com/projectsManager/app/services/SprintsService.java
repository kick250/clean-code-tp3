package com.projectsManager.app.services;

import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.exceptions.SprintNotFoundException;
import com.projectsManager.app.interfaces.SprintsRepositoryInterface;
import com.projectsManager.app.interfaces.SprintsServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class SprintsService implements SprintsServiceInterface {
    private final SprintsRepositoryInterface sprintsRepository;

    public SprintsService(SprintsRepositoryInterface sprintsRepository) {
        this.sprintsRepository = sprintsRepository;
    }

    @Override
    public void update(Sprint sprint) throws SprintNotFoundException {
        sprintsRepository.update(sprint);
    }

    @Override
    public Sprint getById(Long id) throws SprintNotFoundException {
        return sprintsRepository.getById(id);
    }
}

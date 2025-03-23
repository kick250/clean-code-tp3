package com.projectsManager.app.services;

import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.exceptions.SprintNotFoundException;
import com.projectsManager.app.interfaces.SprintsRepositoryInterface;
import com.projectsManager.app.interfaces.SprintsServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintsService implements SprintsServiceInterface {
    private final SprintsRepositoryInterface sprintsRepository;

    public SprintsService(SprintsRepositoryInterface sprintsRepository) {
        this.sprintsRepository = sprintsRepository;
    }

    @Override
    public void update(Sprint sprint) throws SprintNotFoundException {
        this.sprintsRepository.update(sprint);
    }
}

package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.exceptions.SprintNotFoundException;

public interface SprintsServiceInterface {
    void update(Sprint sprint) throws SprintNotFoundException;

    Sprint getById(Long id) throws SprintNotFoundException;
}

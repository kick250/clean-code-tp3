package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.exceptions.SprintNotFoundException;

public interface SprintsRepositoryInterface {

    public void update(Sprint sprint) throws SprintNotFoundException;
}

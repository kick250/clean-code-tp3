package com.projectsManager.app.interfaces;

import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.exceptions.SprintNotFoundException;

import java.util.List;

public interface SprintsServiceInterface {
    public void update(Sprint sprint) throws SprintNotFoundException;
}

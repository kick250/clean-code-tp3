package com.projectsManager.app.DomainGenerator;

import com.github.javafaker.Faker;
import com.projectsManager.app.domain.Project;
import org.instancio.Instancio;
import org.instancio.Select;

public class ProjectGenerator {
    public static Project createProject() {
        return Instancio.of(Project.class)
                .set(Select.field("id"), null)
                .set(Select.field("name"), Faker.instance().gameOfThrones().house())
                .set(Select.field("description"), Faker.instance().lorem().characters(100,150))
                .create();
    }
}

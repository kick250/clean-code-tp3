package com.projectsManager.app.domainGenerator;

import com.github.javafaker.Faker;
import com.projectsManager.app.domain.Task;
import com.projectsManager.app.enums.TaskStatus;
import org.instancio.Instancio;
import org.instancio.Select;

public class TaskGenerator {
    public static Task createTask() {
        return Instancio.of(Task.class)
                .set(Select.field("id"), null)
                .set(Select.field("title"), Faker.instance().lorem().characters(25, 50))
                .set(Select.field("description"), Faker.instance().lorem().characters(50, 100))
                .set(Select.field("status"), TaskStatus.TODO)
                .set(Select.field("owner"), null)
                .set(Select.field("sprint"), null)
                .set(Select.field("markedForDelete"), false)
                .create();
    }
}

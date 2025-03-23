package com.projectsManager.app.domainGenerator;

import com.github.javafaker.Faker;
import com.projectsManager.app.domain.Sprint;
import com.projectsManager.app.infra.TimeConfig;
import org.instancio.Instancio;
import org.instancio.Select;

import java.time.ZoneOffset;

public class SprintGenerator {
    public static Sprint createSprint() {
        return Instancio.of(Sprint.class)
                .set(Select.field("id"), null)
                .set(Select.field("startDate"), Faker.instance().date().birthday().toInstant().atOffset(ZoneOffset.of(TimeConfig.OFFSET_ID)).toLocalDate())
                .set(Select.field("endDate"), Faker.instance().date().birthday().toInstant().atOffset(ZoneOffset.of(TimeConfig.OFFSET_ID)).toLocalDate())
                .set(Select.field("project"), null)
                .set(Select.field("markedForDelete"), false)
                .create();
    }
}

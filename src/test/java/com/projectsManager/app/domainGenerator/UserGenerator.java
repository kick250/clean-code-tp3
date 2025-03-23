package com.projectsManager.app.domainGenerator;

import com.github.javafaker.Faker;
import com.projectsManager.app.domain.User;
import com.projectsManager.app.enums.UserPosition;
import org.instancio.Instancio;
import org.instancio.Select;

public class UserGenerator {
    public static User createUser() {
        return Instancio.of(User.class)
                .set(Select.field("id"), null)
                .set(Select.field("name"), Faker.instance().gameOfThrones().character())
                .set(Select.field("email"), Faker.instance().internet().emailAddress())
                .set(Select.field("position"), UserPosition.DEVELOPER)
                .create();
    }
}

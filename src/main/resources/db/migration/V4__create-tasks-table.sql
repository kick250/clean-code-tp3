CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    owner_id INTEGER NOT NULL REFERENCES users(id),
    sprint_id INTEGER NOT NULL REFERENCES sprints(id)
);
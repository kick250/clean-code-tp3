CREATE TABLE sprints (
    id SERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    project_id INTEGER NOT NULL REFERENCES projects(id)
);
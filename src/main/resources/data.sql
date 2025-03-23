insert into projects (name, description) values ('Gerenciador de projetos', 'App para gerenciar projetos');
insert into projects (name, description) values ('Testador de site de compras', 'Testar site de compras');

insert into sprints (start_date, end_date, project_id) values ('2025-03-01', '2025-04-01', 1);
insert into sprints (start_date, end_date, project_id) values ('2025-04-01', '2025-05-01', 2);

insert into users (name, email, position) values ('Breno Lobato', 'breno.lobato@test.com.br', 'DEVELOPER');
insert into users (name, email, position) values ('Ana Silva', 'ana.silva@test.com.br', 'DESIGNER');

insert into tasks (title, description, status, owner_id, sprint_id) values ('Criar controller de projects', 'Criar controller de projects para fazer operacoes na tabela', 'DONE', 1, 1);
insert into tasks (title, description, status, owner_id, sprint_id) values ('Subir projeto no github', 'Subir projeto no github para equipe iniciar projeto.', 'TODO', 1, 2);


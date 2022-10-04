alter table ulab_edu.person add column resume text;

comment on column ulab_edu.person.resume is 'Коротко о пользователе (опционально)';
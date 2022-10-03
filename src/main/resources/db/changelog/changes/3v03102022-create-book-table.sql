create table ulab_edu.book
(
	id bigint not null,
	title varchar(255) not null,
	author varchar(255) not null,
	page_count integer not null,
	user_id bigint
);

comment on table ulab_edu.book is 'Справочник используется для хранения книг';
comment on column ulab_edu.book.id is 'Идентификатор книги';
comment on column ulab_edu.book.user_id is 'Идентификатор пользователя';
comment on column ulab_edu.book.title is 'Заголовок';
comment on column ulab_edu.book.author is 'Автор';
comment on column ulab_edu.book.page_count is 'Количество страниц';
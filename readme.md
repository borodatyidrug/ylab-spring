Актуатор: http://localhost:8091/app/actuator<br>
Swagger: http://localhost:8091/app/swagger-ui/index.html<br>
rqid: requestId1010101<br><br>

В приложении есть три реализации сервисов для манипуляции сущностями и их хранения:<br>
* Работающие с in-memory хранилищем собственной реализации ("storage-user-service" и "storage-book-service")<br>
* Осуществляющие ORM в БД H2 посредством JPA ("jpa-user-service" и "jpa-book-service")<br>
* Работающие с БД H2 непосредственно через JdbcTemplate ("jdbc-user-service" и "jdbc-book-service")<br><br>
	
Для второго варианта приложение нужно запустить с профилем "jpa", для третьего варианта - с профилем "jdbc".<br>
Соответственно нужно и изменить внедряемую в фасад реализацию через @Qualifier, указывая там "{storage|jpa|jdbc}-{user|book}-service".<br>
Для варианта "storage-{user|book}-service" подойдет любой из двух профилей<br>

# Тестовые данные для собственной реализации хранилища

### Первый POST-запрос: добавляем юзера и его книги
```
{
  "userRequest": {
    "fullName": "user1",
    "title": "type1",
    "age": 77
  },
  "bookRequests": [
    {
      "title": "book1",
      "author": "author1",
      "pageCount": 100
    }, {
      "title": "book2",
      "author": "author2",
      "pageCount": 200
    }, {
      "title": "book3",
      "author": "author3",
      "pageCount": 300
    }
  ]
}
```
#### Ответ:
```
{
  "userId": 0,
  "booksIdList": [
    1,
    2,
    3
  ]
}
```
### Второй POST-запрос: добавляем другого юзера и его другие книги
```
{
  "userRequest": {
    "fullName": "user2",
    "title": "type1",
    "age": 27
  },
  "bookRequests": [
    {
      "title": "book4",
      "author": "author1",
      "pageCount": 150
    }, {
      "title": "book5",
      "author": "author2",
      "pageCount": 250
    }, {
      "title": "book6",
      "author": "author3",
      "pageCount": 350
    }
  ]
}
```
#### Ответ:
```
{
  "userId": 4,
  "booksIdList": [
    5,
    6,
    7
  ]
}
```
### PUT-запрос: обновляем у юзера с id = 0 список книг. В новом списке книг две книги, уже существующие в хранилище
```
{
  "userId": 0,
  "bookIdList": [
    1, 5
  ]
}
```
#### Ответ: 
```
{
  "userId": 0,
  "booksIdList": [
    1,
    5
  ]
}
```
### GET-запрос: id = 0

#### Ответ:
```
{
  "id": 0,
  "fullName": "user1",
  "title": "type1",
  "age": 77,
  "bookResponceList": [
    {
      "id": 1,
      "userId": 0,
      "title": "book1",
      "author": "author1",
      "pageCount": 100
    },
    {
      "id": 5,
      "userId": 0,
      "title": "book4",
      "author": "author1",
      "pageCount": 150
    }
  ]
}
```
### DELETE-запрос: id = 0

### GET-запрос: id = 0

### GET-запрос: id = 4

#### Ответ: книга с id = 5 исчезла у пользователя c id = 4, когда пользователь с id = 0 покинул хранилище с книгами, в числе которых была с id = 5
```
{
  "id": 4,
  "fullName": "user2",
  "title": "type1",
  "age": 27,
  "bookResponceList": [
    {
      "id": 6,
      "userId": 4,
      "title": "book5",
      "author": "author2",
      "pageCount": 250
    },
    {
      "id": 7,
      "userId": 4,
      "title": "book6",
      "author": "author3",
      "pageCount": 350
    }
  ]
}
```
### Третий POST-запрос: добавляем нового юзера с книгами. Юзеру и книгам должны быть присвоены хранилищем ранее высвобожденные id
```
{
  "userRequest": {
    "fullName": "user3",
    "title": "type1",
    "age": 99
  },
  "bookRequests": [
    {
      "title": "book7",
      "author": "author1",
      "pageCount": 100
    }, {
      "title": "book8",
      "author": "author2",
      "pageCount": 200
    }, {
      "title": "book9",
      "author": "author3",
      "pageCount": 300
    }
  ]
}
```
#### Ответ: 
```
{
  "userId": 0,
  "booksIdList": [
    5,
    1,
    8
  ]
}
```
# Тестовые данные для JPA и JdbcTemplate

### Первый POST-запрос: добавляем юзера и его книги
```
{
  "userRequest": {
    "fullName": "user1",
    "title": "type1",
    "age": 77
  },
  "bookRequests": [
    {
      "title": "book1",
      "author": "author1",
      "pageCount": 100
    }, {
      "title": "book2",
      "author": "author2",
      "pageCount": 200
    }, {
      "title": "book3",
      "author": "author3",
      "pageCount": 300
    }
  ]
}
```
#### Ответ:
```
{
  "userId": 1,
  "booksIdList": [
    1,
    2,
    3
  ]
}
```
### Второй POST-запрос: добавляем другого юзера и его другие книги
```
{
  "userRequest": {
    "fullName": "user2",
    "title": "type1",
    "age": 27
  },
  "bookRequests": [
    {
      "title": "book4",
      "author": "author1",
      "pageCount": 150
    }, {
      "title": "book5",
      "author": "author2",
      "pageCount": 250
    }, {
      "title": "book6",
      "author": "author3",
      "pageCount": 350
    }
  ]
}
```
#### Ответ:
```
{
  "userId": 2,
  "booksIdList": [
    4,
    5,
    6
  ]
}
```
### PUT-запрос: обновляем у юзера с id = 1 список книг. В новом списке книг две книги, уже существующие в БД
```
{
  "userId": 1,
  "bookIdList": [
    1, 5
  ]
}
```
#### Ответ:
```
{
  "userId": 1,
  "bookIdList": [
    1, 5
  ]
}
```
### GET-запрос: id = 1

#### Ответ:
```
{
  "id": 1,
  "fullName": "user1",
  "title": "type1",
  "age": 77,
  "bookResponceList": [
    {
      "id": 1,
      "userId": 1,
      "title": "book1",
      "author": "author1",
      "pageCount": 100
    },
    {
      "id": 5,
      "userId": 1,
      "title": "book5",
      "author": "author2",
      "pageCount": 250
    }
  ]
}
```
### DELETE-запрос: id = 1

### GET-запрос: id = 1

### GET-запрос: id = 2

#### Ответ: книга с id = 5 исчезла у пользователя c id = 2, когда пользователь с id = 1 был удален из БД со своими книгами, в числе которых была с id = 5
```
{
  "id": 2,
  "fullName": "user2",
  "title": "type1",
  "age": 27,
  "bookResponceList": [
    {
      "id": 4,
      "userId": 2,
      "title": "book4",
      "author": "author1",
      "pageCount": 150
    },
    {
      "id": 6,
      "userId": 2,
      "title": "book6",
      "author": "author3",
      "pageCount": 350
    }
  ]
}
```
### Третий POST-запрос: добавляем нового юзера с книгами.
```
{
  "userRequest": {
    "fullName": "user3",
    "title": "type1",
    "age": 99
  },
  "bookRequests": [
    {
      "title": "book7",
      "author": "author1",
      "pageCount": 100
    }, {
      "title": "book8",
      "author": "author2",
      "pageCount": 200
    }, {
      "title": "book9",
      "author": "author3",
      "pageCount": 300
    }
  ]
}
```
#### Ответ:
```
{
  "userId": 3,
  "booksIdList": [
    7,
    8,
    9
  ]
}
```

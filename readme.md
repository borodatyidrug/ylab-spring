# ТЗ:

1. собрать приложение mvn clean install
2. проверить работу приложения возможно по этому ендпоинту: http://localhost:8091/app/actuator
3. посмотреть сваггер возможно тут: http://localhost:8091/app/swagger-ui/index.html

http://localhost:8091/app/api/v1/user/create

{
"userRequest": {
"fullName": "test",
"title": "reader",
"age": 33
},
"bookRequests": [
{
"title": "book name",
"author": "test author",
"pageCount": 222
},
{
"title": "book name test",
"author": "test author second",
"pageCount": 555
}
]
}

rqid requestId1010101

# Тестовые данные

## Первый POST-запрос: добавляем юзера и его книги

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

### Ответ:

{
  "userId": 0,
  "booksIdList": [
    1,
    2,
    3
  ]
}

## Второй POST-запрос: добавляем другого юзера и его другие книги

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

### Ответ:

{
  "userId": 4,
  "booksIdList": [
    5,
    6,
    7
  ]
}

## PUT-запрос: обновляем у юзера с id = 0 список книг. В новом списке книг две книги, уже существующие в хранилище

{
  "userId": 0,
  "bookIdList": [
    1, 5
  ]
}

### Ответ: 

{
  "userId": 0,
  "booksIdList": [
    1,
    5
  ]
}

## GET-запрос: id = 0

### Ответ:

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

## DELETE-запрос: id = 0

## GET-запрос: id = 0

## GET-запрос: id = 4

### Ответ: книга с id = 5 исчезла у пользователя c id = 4, когда пользователь с id = 0 покинул хранилище с книгами, в числе которых была с id = 5

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

## Третий POST-запрос: добавляем нового юзера с книгами. Юзеру и книгам должны быть присвоены хранилищем ранее высвобожденные id

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

### Ответ: 

{
  "userId": 0,
  "booksIdList": [
    5,
    1,
    8
  ]
}
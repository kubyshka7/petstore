# Petstore

REST API зоомагазина на Spring Boot: питомцы и заказы на них. Данные хранятся в PostgreSQL.

## Стек

Java 21, Spring Boot 3.4 (Web, Data JPA), PostgreSQL, Maven.
Тесты: JUnit 5, Mockito, MockMvc, H2.

## API

| Метод | Адрес | Что делает |
| --- | --- | --- |
| GET | `/api/pets` | список питомцев |
| GET | `/api/pets/{id}` | один питомец |
| POST | `/api/pets` | добавить питомца |
| PUT | `/api/pets/{id}` | изменить питомца |
| DELETE | `/api/pets/{id}` | удалить питомца |
| GET | `/api/orders` | список заказов |
| GET | `/api/orders/{id}` | один заказ |
| POST | `/api/orders` | оформить заказ |
| PUT | `/api/orders/{id}` | изменить заказ |
| DELETE | `/api/orders/{id}` | удалить заказ |

Примеры:

```bash
curl -X POST localhost:8080/api/pets -H "Content-Type: application/json" \
  -d '{"name":"Barsik","type":"cat","birthdate":"2024-05-01","owner":"Anna"}'

curl -X POST localhost:8080/api/orders -H "Content-Type: application/json" \
  -d '{"petId":1,"customerName":"Ivan","orderDate":"2026-11-01","status":"new"}'
```

Что проверяется при сохранении:

- имена питомца, владельца и покупателя состоят только из букв;
- дата рождения и дата заказа не могут быть в будущем;
- на одного питомца можно оформить только один заказ.

Если данные не прошли проверку, API отвечает 400 с текстом ошибки. Если записи нет, ответ 404,
а при конфликте с данными в базе (например, при удалении питомца, на которого есть заказ) ответ 409.

## Как запустить

Нужен PostgreSQL. По умолчанию приложение подключается к `localhost:5432/postgres` под пользователем
`postgres`. Свои данные можно передать через переменные окружения `DB_URL`, `DB_USER` и `DB_PASSWORD`.

```bash
./mvnw spring-boot:run
```

При каждом запуске таблицы создаются заново, и в базу добавляются два тестовых питомца.

## Тесты

```bash
./mvnw test
```

18 тестов: модульные тесты сервисов на Mockito и проверка API через MockMvc. Для тестов используется
база H2 в памяти, поэтому PostgreSQL для них не нужен.

## Автор

Елисавета Запорожская

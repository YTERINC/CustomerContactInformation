<h3>REST приложение, обеспечивающее хранение информации о клиентах и их контактной информации </h3>

Приложение предназначено для изучение и исследования технологий BACKEND-разработки.
<br>
<br>
<b>Стек технологий: </b>
- Java <br>
- Spring Boot, Spring Data JPA<br>
- Docker, Docker Compose<br>
- PostgreSQL, liquibase<br>
- Swagger<br>
- JUnit тесты<br>
<br>
<b>Описание:</b>
<br>
Каждый клиент характеризуется именем. <br>
Каждому клиенту в соответствие может быть поставлена информация о его контактах: 0 и более телефонных номеров, 0 и более адресов электронной почты.
<br>
<br>
<b>API обеспечивает следующие функции:</b> <br>
1. Добавление нового клиента.<br>
2. Добавление нового контакта клиента (телефон или email).<br>
3. Получение списка клиентов.<br>
4. Получение информации по заданному клиенту (по id).<br>
5. Получение списка контактов заданного клиента.<br>
6. Получение списка контактов заданного типа заданного клиента.<br>
7. Удаление контактной информации заданного клиента.<br>
8. Удаление информации о клиенте<br>
9. Изменение данных о клиенте.<br>
10. Изменение контактной информации заданного клиента.<br>
<br>
<br>
Swagger доступен по ссылке:<br>
http://IP:8080/swagger-ui/index.html
<br>
<br>
<b>Для сборки и запуска приложения необходимо:</b> <br>
1. Клонировать репозиторий<br>
   git clone https://github.com/YTERINC/CustomerContactInformation.git<br>
2. Выполнить команду на сервере с установленным Docker<br>
   docker compose up --build<br>
3. Проверить работу приложения можно с помощью Postman
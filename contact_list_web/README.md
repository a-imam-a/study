## Консольное приложение "Список контактов"
### Описание
Приложение написано в учебных целях. Оно предназначено для хранения списка контактов: их имя, фамилию, номер телефона и email.
### Доступные настройки при запуске
Для запуска программы необходимо необходимо развернуть развернуть docker-образ из каталога docker.
Команды: 
1. cd docker
2. docker-compose up

В файле **docker-compose.yaml** можно включить инициализацию первоначальных контактов при запуске приложения.
Для этого в переменную среды **INIT_ON_STARTUP** необходимо установить **true**(по умолчанию **false**)
### Доступные действия с контактами
* добавление нового контактного лица - **Create Task**
* изменение контактного лица - **Edit**
* удаление контактного лица - **Delete**
Консольное приложение "Контакты"

Доступны следующие команды (можно вводить как в верхнем, так и нижнем регистре):

1) ADD - сохраняет новый контакт. Формат ввода: ФИО;телефон;email

2) DEL - удяляет контакт по email

3) LIST - просмотр списка контактов

4) SAVE - сохраняет контакты в файл. По умолчанию, название файла default-contacts в каталоге resources.

В конфигурационном файле application.properties можно настроить:
1) файл, куда будут сохраняться контакты - свойство app.fileStorage
2) инициализацию контактов из файла при запуске программыб в случае если spring.profiles.active=init
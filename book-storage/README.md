## Book storage with redis cache
Приложение написано в учебных целях.
#### Виды кешей:
1. **bookFindAll** - ключом выступают передаваемые pageNumber и pageSize;
2. **bookFindByNameAndAuthor** - ключем выступают параметры запроса name и author;
3. **bookFindById** - данный вид кеша неактивен.

#### API предоставляет возможность:
* получать все книги - результат кешируется в **bookFindAll**;
* получать книги по наименованию и автору - результат кешируется в **bookFindByNameAndAuthor**;
* создавать книги - инвалидируется **bookFindAll**;
* редактировать книги - инвалидируются **bookFindAll** и **bookFindByNameAndAuthor**;
* удалять книги - инвалидируются **bookFindAll** и **bookFindByNameAndAuthor**.

В конфигурационном файле можно настроить срок инвалидации кешей: свойство **expiry**, которое есть у каждого кеша

### Доступные методы api 
* **GET** - /api/v1/book?pageNumber={0}&pageSize={0};
* **GET** - /api/v1/book/?name={name}&author={author};
* **POST** - /api/v1/book
* **PUT** - /api/v1/book/{id}
* **DELETE** - /api/v1/book/{id}
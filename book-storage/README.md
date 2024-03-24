## Book storage with redis cache
Приложение написано в учебных целях.
#### Виды кешей:
1. **bookFindAllByCategory** - ключом выступают передаваемые categoryName, pageNumber и pageSize;
2. **bookFindByNameAndAuthor** - ключем выступают параметры запроса name и author;
3. **bookFindById** - данный вид кеша неактивен.

#### API предоставляет возможность:
* получать все книги - результат кешируется в **bookFindAllByCategory**;
* получать книги по наименованию и автору - результат кешируется в **bookFindByNameAndAuthor**;
* создавать книги - инвалидируется **bookFindAllByCategory**;
* редактировать книги - инвалидируются **bookFindAllByCategory** и **bookFindByNameAndAuthor**;
* удалять книги - инвалидируются **bookFindAllByCategory** и **bookFindByNameAndAuthor**.

В конфигурационном файле можно настроить срок инвалидации кешей: свойство **expiry**, которое есть у каждого кеша

### Доступные методы api 
* **GET** - /api/v1/book?pageNumber={0}&pageSize={0};
* **GET** - /api/v1/book/?name={name}&author={author};
* **POST** - /api/v1/book
* **PUT** - /api/v1/book/{id}
* **DELETE** - /api/v1/book/{id}
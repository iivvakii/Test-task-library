<details lang="java">
<summary>How to start:</summary>

1. Clone project.
2. Configure the Database.
```
CREATE DATABASE testDB;
```
3. Configure environment variables.
```
spring.datasource.url=jdbc:postgresql://localhost:5432/library_management
spring.datasource.username=your_username
spring.datasource.password=your_password
```

5. Run project.
```
mvn spring::run
```

</details>

<details lang="java">
<summary>API</summary>

<details lang="java">
<summary>Books</summary>
  
```
  Get all books:
    GET http://localhost:8080/api/v1/books

  Get book by id:
    GET http://localhost:8080/api/v1/books/{id}

  Add book:
    POST http://localhost:8080/api/v1/books

  Update book:  
    PUT http://localhost:8080/api/v1/books/{id}  

  Delete book:
    DELETE http://localhost:8080/api/v1/books/{id}

  Get distinct borrowed books titles:
    GET http://localhost:8080/api/v1/books/borrowed/distinct-titles

  Get borrowed books with count:
    GET http://localhost:8080/api/v1/books/borrowed-summary
    
```
 
</details>

<details lang="java">
<summary>Members</summary>
  
```
  Get all members:
    GET http://localhost:8080/api/v1/members

  Get member by id:
    GET http://localhost:8080/api/v1/members/{id}

  Add member:
    POST http://localhost:8080/api/v1/members

  Update member:  
    PUT http://localhost:8080/api/v1/members/{id}  

  Delete member:
    DELETE http://localhost:8080/api/v1/members/{id}

  Member borrow book:
    POST http://localhost:8080/api/v1/members/{memberId}/borrow/{bookId}

  Member return borrowed book:
    POST http://localhost:8080/api/v1/members/{memberId}/borrow/{bookId}

  Get all borrowed books by a specific member:
    GET http://localhost:8080/api/v1/members/{name}/borrowed-books
```
 
</details>
</details>

<details lang="java">
<summary>Task description:</summary>
  
Swagger doc: http://localhost:8080/swagger-ui/index.html#/

Database:
 - Postgres


Requirements:

Books
 - The system should allow the creation, reading, updating, and deleting of
books.
 - A book should have the following attributes: ID, title, author, and amount.
 - If a book is added with the same name and author that already exists in
    the database, the amount of the existing book should increase by 1.
 - If either the name or the author is different, it should be considered a
    different book.
 - A book can not be deleted if at least one of it is borrowed.

Members
 - The system should allow the creation, reading, updating, and deleting of
    library members.
 - A member should have the following attributes: ID, name, and membership
    date (automatically set a date of member creation).
 - A member can borrow many books but not more than 10. The limit amount
    should come from the env variable or property file.
 - After a member borrows a book, the amount of this book should be
    decreased by 1.
 - If the book amount is 0, the book can not be borrowed.
 - After a member returns a book, the amount of this book should be
    increased by 1.
 - Member can not be deleted if it has borrowed books.


Validations:


Book validation:
 - name - required, should start with a capital letter, min length - 3 symbols
 - author - required, should contain two capital words with name and
    surname and space between. Example: Paulo Coelho.

   
User validation:
 - name - required




Also, implement endpoints:
 - Retrieve all books borrowed by a specific member by name.
 - Retrieve all borrowed distinct book names.
 - Retrieve all borrowed distinct book names and amount how much copy of
this book was borrowed.


Additional requirements:
 - Write unit tests for services.
 - Add swagger.
Technologies:




</details>

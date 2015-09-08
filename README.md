# Library - SQL many-to-many relationship

Java Advanced Database<br>

Author name - Yvonne Peng<br>
Program name - Library<br>
<br>
### Discription -<br>
Catalog a library's books and let patrons check them out. Here are some user stories:<br>
<ul>
<li> As a librarian, I want to create, read, update, delete, and list books in the catalog, so that we can keep track of our inventory.</li>
<li> As a librarian, I want to search for a book by author or title, so that I can find a book when there are a lot of books in the library.</li>
<li> As a librarian, I want to enter multiple authors for a book, so that I can include accurate information in my catalog.</li>
<li> As a patron, I want to check a book out, so that I can take it home with me.</li>
<li> As a patron, I want to know how many copies of a book are on the shelf, so that I can see if any are available.</li>
<li> As a patron, I want to see a history of all the books I checked out, so that I can look up the name of that awesome sci-fi novel I read three years ago.</li>
<li> As a patron, I want to know when a book I checked out is due, so that I know when to return it.
As a librarian, I want to see a list of overdue books, so that I can call up the patron who checked them out and tell them to bring them back - OR ELSE!</li>
</ol>
<br>
### Setup PSQL instructions-<br>
In PSQL:
```
CREATE DATABASE library;
CREATE TABLE authors (id serial PRIMARY KEY, name varchar);
CREATE TABLE books (id serial PRIMARY KEY, title varchar);
CREATE TABLE authors_books (id serial PRIMARY KEY, author_id int, book_id int);
CREATE TABLE copies (id serial PRIMARY KEY, book_id int, available boolean);
CREATE TABLE checkouts (id serial PRIMARY KEY, copy_id int, patron_id int, , due_date varchar, checkout_date varchar);
CREATE TABLE patrons (id serial PRIMARY KEY, name varchar);
CREATE DATABASE library_test WITH TEMPLATE library;
```
<br>
![alt tag](https://raw.githubusercontent.com/YHoP/Library-mm-sql/master/src/main/resources/public/img/Screen%20Shot%202015-09-02%20at%204.03.05%20PM.png)

### Setup instructions -<br>
1. Download this repository files to your computer.<br>
2. Navigate to the folder directory.<br>
3. Open command prompt in Windows or terminal in Mac.<br>
4. If you don't have PostgreSQL installed in your computer, check out <a href="https://www.learnhowtoprogram.com/lessons/installing-postgres">this link</a> and follow the instruction.
5. Create a database: "shoes" in your computer.<br>
6. Run the following command "psql library < library.sql"<br>
7. Run the following command "Gradle run". (If you have not installed Gradle, check out the <a href="https://gradle.org/getting-started-gradle-java/">Gradle website</a> )<br>
8. Open your internet browser and navigate to <a href="http://localhost:4567/">http://localhost:4567/</a><br>
9. The webpage would load automatically. You can manage your own library!!<br>
<br>
Copyright: YHoP<br>
<br>
License information: Open source licensing<br>

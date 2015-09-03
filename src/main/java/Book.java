import java.util.List;
import java.util.ArrayList;
import java.util.*;
import org.sql2o.*;

public class Book {
  private int id;
  private String title;

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Book(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object otherBook){
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getTitle().equals(newBook.getTitle()) &&
             this.getId() == newBook.getId();
    }
  }


  public static List<Book> all() {
    String sql = "SELECT * FROM books ORDER BY title";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Book.class);
    }
  }


  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books (title) VALUES (:title)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", title)
        .executeUpdate()
        .getKey();
    }
  }

  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books where id=:id";
      Book book = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
      return book;
    }
  }

  public void update(String title) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET title = :title WHERE id = :id";
      con.createQuery(sql)
        .addParameter("title", title)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public static List<Book> searchByTitle(String title_search) {
    String sql = "SELECT * FROM books WHERE title LIKE '%" + title_search + "%'";
    List<Book> searchResults;
    try (Connection con = DB.sql2o.open()) {
      searchResults = con.createQuery(sql)
        .executeAndFetch(Book.class);
    }
    return searchResults;
  }

  public void addAuthor(int author_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO authors_books (author_id, book_id) VALUES (:author_id, :book_id)";
      con.createQuery(sql)
        .addParameter("author_id", author_id)
        .addParameter("book_id", id)
        .executeUpdate();
    }
  }

  public List<Author> getAuthors() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT authors.* FROM books JOIN authors_books ON (books.id = authors_books.book_id) JOIN authors ON (authors_books.author_id = authors.id) WHERE books.id =:id";
      List<Author> authors = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Author.class);
      return authors;
    }
  }

  // need to fix init error
  public List<Author> getUnassignedAuthors() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT authors.* FROM books JOIN authors_books ON (books.id = authors_books.book_id) JOIN authors ON (authors_books.author_id = authors.id) WHERE NOT books.id =:id";
      List<Author> authors = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Author.class);
      return authors;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM books where id=:id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
      String joinsql = "DELETE FROM authors_books where book_id=:id";
      con.createQuery(joinsql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public Integer getCopies() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM copies where book_id=:id";
      List<Copy> bookCopies = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Copy.class);
      return bookCopies.size();
    }
  }

  public int countAvailable() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT count(*) FROM copies where book_id =:id AND available = true";
      return (int) con.createQuery(sql)
                .addParameter("id", id)
                .executeAndFetchFirst(Integer.class);
    }
 }

}

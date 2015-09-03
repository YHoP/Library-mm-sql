import java.util.List;
import java.util.ArrayList;
import java.util.*;
import org.sql2o.*;

public class Author {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Author(String name) {
    this.name = name;
  }


  public static List<Author> all() {
    String sql = "SELECT * FROM authors ORDER BY name";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Author.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO authors (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Author find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM authors where id=:id";
      Author author = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Author.class);
      return author;
    }
  }

  public void update(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE authors SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void addBook(int book_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO authors_books (book_id, author_id) VALUES (:book_id, :author_id)";
      con.createQuery(sql)
        .addParameter("book_id", book_id)
        .addParameter("author_id", id)
        .executeUpdate();
    }
  }

  public List<Book> getBooks() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT books.* FROM authors JOIN authors_books ON (authors.id=authors_books.author_id) JOIN books ON (authors_books.book_id=books.id) WHERE authors.id =:id";
      List<Book> books = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Book.class);
        return books;
    }
  }

  public static List<Author> searchByAuthor(String author_name) {
    String sql = "SELECT * FROM authors WHERE name LIKE '%" + author_name + "%'";
    List<Author> searchResults;
    try (Connection con = DB.sql2o.open()) {
      searchResults = con.createQuery(sql)
        .executeAndFetch(Author.class);
    }
    return searchResults;
  }

  // need to fix init error
  public List<Book> getUnassignedBooks() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT books.* FROM authors JOIN authors_books ON (authors.id=authors_books.author_id) JOIN books ON (authors_books.book_id=books.id) WHERE NOT authors.id =:id";
      List<Book> books = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Book.class);
        return books;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM authors where id=:id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
      String joinsql = "DELETE FROM authors_books where author_id=:id";
      con.createQuery(joinsql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

}

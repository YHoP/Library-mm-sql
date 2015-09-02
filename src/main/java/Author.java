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
        .addParameter("author_id", this.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Book> getBooks() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT book_id FROM authors_books WHERE author_id = :author_id";
      List<Integer> book_ids = con.createQuery(sql)
        .addParameter("author_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Book> books = new ArrayList<Book>();

      for (Integer book_id : book_ids) {
          String bookQuery = "Select * From books WHERE id = :book_id";
          Book book = con.createQuery(bookQuery)
            .addParameter("book_id", book_id)
            .executeAndFetchFirst(Book.class);
            books.add(book);
      }
      return books;
    }
  }

}

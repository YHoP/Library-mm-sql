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

  public void addAuthor(int author_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO authors_books (author_id, book_id) VALUES (:author_id, :book_id)";
      con.createQuery(sql)
        .addParameter("author_id", author_id)
        .addParameter("book_id", this.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Author> getAuthors() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT author_id FROM authors_books WHERE book_id = :book_id";
      List<Integer> author_ids = con.createQuery(sql)
        .addParameter("book_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Author> authors = new ArrayList<Author>();

      for (Integer author_id : author_ids) {
          String bookQuery = "Select * From authors WHERE id = :author_id";
          Author author = con.createQuery(bookQuery)
            .addParameter("author_id", author_id)
            .executeAndFetchFirst(Author.class);
            authors.add(author);
      }
      return authors;
    }
  }

}
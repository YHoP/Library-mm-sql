import java.util.List;
import org.sql2o.*;

public class Copy {
  private int id, book_id;
  private boolean available;

  public int getId() {
    return id;
  }

  public int getBookId() {
    return book_id;
  }

  public boolean getAvailable(){
    return available;
  }

  public Copy(int book_id) {
    this.book_id = book_id;
    available = true;
  }

  public static List<Copy> all() {
    String sql = "SELECT * FROM copies";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Copy.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO copies (book_id, available) VALUES (:book_id, :available)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("book_id", this.book_id)
        .addParameter("available", this.available)
        .executeUpdate()
        .getKey();
    }
  }

  public static Copy find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM copies where id=:id";
      Copy copy = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Copy.class);
      return copy;
    }
  }

  public static Integer getBookCopies(int book_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM copies where book_id=:book_id";
      List<Copy> bookCopies = con.createQuery(sql)
        .addParameter("book_id", book_id)
        .executeAndFetch(Copy.class);
      return bookCopies.size();
    }
  }

  public static List<Copy> getCopyList(int book_id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM copies where book_id=:book_id ORDER BY id";
      List<Copy> bookCopies = con.createQuery(sql)
        .addParameter("book_id", book_id)
        .executeAndFetch(Copy.class);
      return bookCopies;
    }
  }

  public static Integer getPatronId(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT patron_id FROM checkouts WHERE copy_id =:id ORDER BY id DESC";
      return (int) con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Integer.class);
    }
  }


  public void setUnavailable() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE copies SET available = false WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }


  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM copies WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }
}

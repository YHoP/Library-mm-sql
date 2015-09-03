import java.util.List;
import org.sql2o.*;

public class Checkout {
  private int id, copy_id, patron_id;
  private String checkout_date, due_date;

  public int getId() {
    return id;
  }

  public int getCopyId() {
    return copy_id;
  }

  public int getPatronId() {
    return patron_id;
  }

  public String getCheckoutDate(){
    return checkout_date;
  }

  public String getDueDate(){
    return due_date;
  }

  public Checkout(int copy_id, int patron_id, String checkout_date, String due_date) {
    this.copy_id = copy_id;
    this.patron_id = patron_id;
    this.checkout_date = checkout_date;
    this.due_date = due_date;
  }

  public static List<Checkout> all() {
    String sql = "SELECT * FROM checkouts";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Checkout.class);
    }
  }


  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO checkouts (copy_id, patron_id, checkout_date, due_date) VALUES (:copy_id, :patron_id, :checkout_date, :due_date)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("copy_id", this.copy_id)
        .addParameter("patron_id", this.patron_id)
        .addParameter("checkout_date", this.checkout_date)
        .addParameter("due_date", this.due_date)
        .executeUpdate()
        .getKey();
    }
  }

  public static Checkout find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM checkouts where id=:id";
      Checkout checkout = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Checkout.class);
      return checkout;
    }
  }

  public String getBookTitle(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT books.title FROM checkouts JOIN copies ON (checkouts.copy_id = copies.id) JOIN books ON (copies.book_id = books.id) WHERE checkouts.id = :id";
      String bookTitle = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(String.class);
      return bookTitle;
    }
  }

  public List<Author> getBookAuthors(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT books.id FROM checkouts JOIN copies ON (checkouts.copy_id = copies.id) JOIN books ON (copies.book_id = books.id) WHERE checkouts.id = :id";
      int book_id =  (int) con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Integer.class);

      String joinsql = "SELECT authors.* FROM books JOIN authors_books ON (books.id = authors_books.book_id) JOIN authors ON (authors_books.author_id = authors.id) WHERE books.id =:book_id";
      List<Author> authors = con.createQuery(joinsql)
        .addParameter("book_id", book_id)
        .executeAndFetch(Author.class);
      return authors;
    }

  }


  public void setAvailable() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE copies SET available = true WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.getCopyId())
        .executeUpdate();
    }
  }


}

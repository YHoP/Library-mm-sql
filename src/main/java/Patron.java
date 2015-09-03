import java.util.List;
import java.util.ArrayList;
import java.util.*;
import org.sql2o.*;

public class Patron {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Patron(String name) {
    this.name = name;
  }

  public static List<Patron> all() {
    String sql = "SELECT * FROM patrons ORDER BY name";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Patron.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patrons (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Patron find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patrons where id=:id";
      Patron patron = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Patron.class);
      return patron;
    }
  }

  public void update(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patrons SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM patrons where id=:id";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public List<Checkout> getCheckouts(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT checkouts.* FROM patrons JOIN checkouts ON (patrons.id = checkouts.patron_id) JOIN copies ON (checkouts.copy_id = copies.id) WHERE checkouts.patron_id =:id AND copies.available = false";
      List<Checkout> checkouts = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Checkout.class);
      return checkouts;
    }
  }

  public List<Checkout> getHistory(){
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT checkouts.* FROM patrons JOIN checkouts ON (patrons.id = checkouts.patron_id) JOIN copies ON (checkouts.copy_id = copies.id) WHERE checkouts.patron_id =:id AND copies.available = true";
      List<Checkout> checkouts = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Checkout.class);
      return checkouts;
    }
  }

}

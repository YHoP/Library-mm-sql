import java.util.HashMap;
import java.util.List;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;


public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("copies", Copy.getCheckoutCopies());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/author_search", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String author_search = request.queryParams("author_search");

      model.put("authors", Author.searchByAuthor(author_search));

      model.put("template", "templates/authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/title_search", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String title_search = request.queryParams("title_search");

      model.put("books", Book.searchByTitle(title_search));
      model.put("authors", Author.all());
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/authors", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("authors", Author.all());
      model.put("template", "templates/authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/authors/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Author newAuthor = new Author(name);
      newAuthor.save();
      model.put("authors", Author.all());
      model.put("template", "templates/authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/authors/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.params("id"));
      Author author = Author.find(author_id);
      model.put("author", author);
      model.put("allBooks", Book.all());
      model.put("books", author.getBooks());
      model.put("template", "templates/author.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/authors/addBook", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.queryParams("autorId"));
      int book_id = Integer.parseInt(request.queryParams("bookId"));
      Author author = Author.find(author_id);
      author.addBook(book_id);
      model.put("books", Book.all());
      response.redirect("/authors/" + author_id);
      return null;
    });

    get("/authors/:id/delete", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.params("id"));
      Author author = Author.find(author_id);
      author.delete();
      response.redirect("/authors");
      return null;
    });

    get("/authors/:id/update", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.params("id"));
      Author author = Author.find(author_id);
      model.put("author", author);
      model.put("template", "templates/updateauthor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/authors/:id/update", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.params("id"));
      Author author = Author.find(author_id);
      String name = request.queryParams("name");
      author.update(name);
      response.redirect("/authors/" + author_id);
      return null;
    });

    post("/books/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String title = request.queryParams("title");
      Integer authorId = Integer.parseInt(request.queryParams("authorId"));
      Book newBook = new Book(title);
      newBook.save();
      newBook.addAuthor(authorId);
      response.redirect("/books/" + newBook.getId());
      return null;
    });

    get("/books", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("books", Book.all());
      model.put("authors", Author.all());
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    get("/books/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int book_id = Integer.parseInt(request.params("id"));
      Book book = Book.find(book_id);
      model.put("copies", Copy.getCopyList(book_id));
      model.put("book", book);
      model.put("authors", Author.all());
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/books/addAuthors", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.queryParams("author_id"));
      int book_id = Integer.parseInt(request.queryParams("book_id"));
      Book book = Book.find(book_id);
      book.addAuthor(author_id);
      model.put("authors", Author.all());
      response.redirect("/books/" + book_id);
      return null;
    });

    get("/books/:id/delete", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int book_id = Integer.parseInt(request.params("id"));
      Book book = Book.find(book_id);
      book.delete();
      response.redirect("/books");
      return null;
    });

    get("/books/:id/update", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int book_id = Integer.parseInt(request.params("id"));
      Book book = Book.find(book_id);
      model.put("book", book);
      model.put("template", "templates/updatebook.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/books/:id/update", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int book_id = Integer.parseInt(request.params("id"));
      Book book = Book.find(book_id);
      String title = request.queryParams("title");
      book.update(title);
      response.redirect("/books/" + book_id);
      return null;
    });

    post("/books/addCopies", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int book_id = Integer.parseInt(request.queryParams("book_id"));
      int copies = Integer.parseInt(request.queryParams("copies"));
      for(int i=1; i<= copies; i++){
        Copy newCopy = new Copy(book_id);
        newCopy.save();
      }
      response.redirect("/books/" + book_id);
      return null;
    });

    get("/patrons", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("patrons", Patron.all());
      model.put("template", "templates/patrons.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patrons/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Patron newPatron = new Patron(name);
      newPatron.save();
      response.redirect("/patrons");
      return null;
    });

    get("/patrons/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int patron_id = Integer.parseInt(request.params("id"));
      Patron patron = Patron.find(patron_id);
      model.put("patron", patron);
      model.put("template", "templates/patron.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/patrons/:id/delete", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int patron_id = Integer.parseInt(request.params("id"));
      Patron patron = Patron.find(patron_id);
      patron.delete();
      response.redirect("/patrons");
      return null;
    });

    get("/patrons/:id/update", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int patron_id = Integer.parseInt(request.params("id"));
      Patron patron = Patron.find(patron_id);
      model.put("patron", patron);
      model.put("template", "templates/updatepatron.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patrons/:id/update", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int patron_id = Integer.parseInt(request.params("id"));
      Patron patron = Patron.find(patron_id);
      String name = request.queryParams("name");
      patron.update(name);
      response.redirect("/patrons/" + patron_id);
      return null;
    });

    get("/copy/:id/checkout", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int copy_id = Integer.parseInt(request.params("id"));
      Copy copy = Copy.find(copy_id);
      int book_id = copy.getBookId();
      Book book = Book.find(book_id);
      model.put("patrons", Patron.all());
      model.put("copy", copy);
      model.put("book", book);
      model.put("template", "templates/checkout.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/copy/:id/checkout", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int copy_id = Integer.parseInt(request.queryParams("copy_id"));
      int patron_id = Integer.parseInt(request.queryParams("patron_id"));
      String checkout_date = request.queryParams("checkout_date");
      String due_date = request.queryParams("due_date");
      Checkout checkout = new Checkout (copy_id, patron_id, checkout_date, due_date);
      checkout.save();

      Copy copy = Copy.find(copy_id);
      copy.setUnavailable();
      int book_id = copy.getBookId();
      Book book = Book.find(book_id);
      model.put("patrons", Patron.all());
      model.put("copy", copy);
      model.put("book", book);
      response.redirect("/patrons/" + patron_id);
      return null;
    });

    get("/patrons/:patron_id/return/:checkout_id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int patron_id = Integer.parseInt(request.params("patron_id"));
      int checkout_id = Integer.parseInt(request.params("checkout_id"));
      Checkout checkout = Checkout.find(checkout_id);
      checkout.setAvailable();
      response.redirect("/patrons/" + patron_id);
      return null;
    });

  // public static void redirectIfCheckedOut(Copy copy, Response response) {
  //   if (copy.isCheckedOut()) {
  //     response.redirect("/patrons/" + patron_id);
  //     return null;
  //   }
  // }
  }
}

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
      model.put("books", Book.all());
      model.put("authors", Author.all());
      model.put("template", "templates/index.vtl");
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

    post("/books/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String title = request.queryParams("title");
      Integer authorId = Integer.parseInt(request.queryParams("authorId"));
      Book newBook = new Book(title);
      newBook.save();
      newBook.addAuthor(authorId);
      Copy newCopy = new Copy(newBook.getId());
      response.redirect("/");
      return null;
    });

    get("/authors/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.params("id"));
      Author author = Author.find(author_id);
      model.put("books", author.getBooks());
      model.put("allBooks", Book.all());
      model.put("author", author);
      model.put("template", "templates/author.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/authors/addBook", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.queryParams("autorId"));
      int book_id = Integer.parseInt(request.queryParams("bookId"));
      Author author = Author.find(author_id);
      author.addBook(book_id);
      response.redirect("/authors/" + author_id);
      return null;
    });


  }
}

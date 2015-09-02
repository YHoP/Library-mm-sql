import org.junit.*;
import static org.junit.Assert.*;

public class CopyTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Copy.all().size(), 0);
  }

  @Test
  public void getBookCopies_returnCorrectCopyCount() {
    Book firstBook = new Book("Even Cowgirls Get the Blues");
    firstBook.save();
    Copy firstCopy = new Copy(firstBook.getId());
    firstCopy.save();
    Copy secondCopy = new Copy(firstBook.getId());
    secondCopy.save();
    Copy thirdCopy = new Copy(firstBook.getId());
    thirdCopy.save();
    assertTrue(Copy.getBookCopies(firstBook.getId()).equals(3));
  }
}

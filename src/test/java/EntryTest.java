import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class EntryTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/address_book_test", null, null);
  }

  @After
  public void tearDown() {
    try (Connection con = DB.sql2o.open()) {
      String deleteContacts = "DELETE FROM contacts *;";
      String deleteEntries = "DELETE FROM entries *;";
      con.createQuery(deleteContacts).executeUpdate();
      con.createQuery(deleteEntries).executeUpdate();
    }
  }

  @Test
  public void entry_instantiatesCorrectly() {
    Entry testEntry = new Entry("name", "phone", "mailing", "email", 1);
    assertTrue(testEntry instanceof Entry);
  }

  @Test
  public void getName_returnsCorrectValue() {
    Entry testEntry = new Entry("name", "phone", "mailing", "email", 1);
    assertEquals("name", testEntry.getName());
  }

  @Test
  public void getPhone_returnsCorrectValue() {
    Entry testEntry = new Entry("name", "phone", "mailing", "email", 1);
    assertEquals("phone", testEntry.getPhone());
  }

  @Test
  public void getMailing_returnsCorrectValue() {
    Entry testEntry = new Entry("name", "phone", "mailing", "email", 1);
    assertEquals("mailing", testEntry.getMailing());
  }

  @Test
  public void getEmail_returnsCorrectValue() {
    Entry testEntry = new Entry("name", "phone", "mailing", "email", 1);
    assertEquals("email", testEntry.getEmail());
  }

  @Test
  public void all_returnsAllEntriesFromDatabase() {
    Entry firstEntry = new Entry("name", "phone", "mailing", "email", 1);
    firstEntry.save();
    Entry secondEntry = new Entry("name2", "phone2", "mailing2", "email2", 1);
    secondEntry.save();
    assertTrue(Entry.all().get(0).equals(firstEntry));
    assertTrue(Entry.all().get(1).equals(secondEntry));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Entry testEntry = new Entry("name", "phone", "mailing", "email", 1);
    testEntry.save();
    assertTrue(Entry.all().get(0).equals(testEntry));
  }

  @Test
  public void equals_returnsTrueIfAllEntriesAreTheSame() {
    Entry firstEntry = new Entry("name", "phone", "mailing", "email", 1);
    Entry secondEntry = new Entry("name", "phone", "mailing", "email", 1);
    assertTrue(firstEntry.equals(secondEntry));
  }

  @Test
  public void save_assignsIdToObject() {
    Entry testEntry = new Entry("name", "phone", "mailing", "email", 1);
    testEntry.save();
    Entry savedEntry = Entry.all().get(0);
    assertEquals(testEntry.getId(), savedEntry.getId());
  }

  @Test
  public void find_returnsEntryWithSameId() {
    Entry firstEntry = new Entry("name", "phone", "mailing", "email", 1);
    firstEntry.save();
    Entry secondEntry = new Entry("name2", "phone2", "mailing2", "email2", 1);
    secondEntry.save();
    assertEquals(secondEntry, Entry.find(secondEntry.getId()));
  }

  @Test
  public void save_savesContactIdIntoDB_true() {
    Contact testContact = new Contact("contactName");
    testContact.save();
    Entry testEntry = new Entry("name", "phone", "mailing", "email", testContact.getId());
    testEntry.save();
    Entry savedEntry = Entry.find(testEntry.getId());
    assertEquals(savedEntry.getContactId(), testContact.getId());
  }

}

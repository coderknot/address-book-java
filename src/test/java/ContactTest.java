import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ContactTest {

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
  public void contact_instantiatesCorrectly() {
    Contact testContact = new Contact("contactName");
    assertTrue(testContact instanceof Contact);
  }

  @Test
  public void getName_returnsCorrectValue() {
    Contact testContact = new Contact("contactName");
    assertEquals("contactName", testContact.getName());
  }

  @Test
  public void all_returnsAllContactsInDatabase() {
    Contact firstContact = new Contact("contact1");
    firstContact.save();
    Contact secondContact = new Contact("contact2");
    secondContact.save();
    assertTrue(Contact.all().get(0).equals(firstContact));
    assertTrue(Contact.all().get(1).equals(secondContact));
  }

  @Test
  public void equals_returnsTrueIfAllContactsAreTheSame() {
    Contact firstContact = new Contact("contact1");
    Contact secondContact = new Contact("contact1");
    assertTrue(firstContact.equals(secondContact));
  }

  @Test
  public void save_returnsTrueIfEqualsSaved() {
    Contact testContact = new Contact("contactName");
    testContact.save();
    assertTrue(Contact.all().get(0).equals(testContact));
  }

  @Test
  public void save_assignsIdToObject() {
    Contact testContact = new Contact("contactName");
    testContact.save();
    Contact savedContact = Contact.all().get(0);
    assertEquals(testContact.getId(), savedContact.getId());
  }

  @Test
  public void find_returnsContactWithSameId() {
    Contact firstContact = new Contact("contact1");
    firstContact.save();
    Contact secondContact = new Contact("contact2");
    secondContact.save();
    assertEquals(secondContact, Contact.find(secondContact.getId()));
  }

  @Test
  public void getEntries_retrievesAllEntriesFromDatabase() {
    Contact testContact = new Contact("contactName");
    testContact.save();
    Entry firstEntry = new Entry("name", "phone", "mailing", "email", testContact.getId());
    firstEntry.save();
    Entry secondEntry = new Entry("name2", "phone2", "mailing2", "email2", testContact.getId());
    secondEntry.save();
    Entry[] entries = new Entry[] {firstEntry, secondEntry};
    assertTrue(testContact.getEntries().containsAll(Arrays.asList(entries)));
  }


}

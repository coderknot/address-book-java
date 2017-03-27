import org.sql2o.*;
import java.util.List;

public class Contact {
  private String name;
  private int id;

  public Contact(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public int getId() {
    return this.id;
  }

  public static List<Contact> all() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT id, name FROM contacts;";
      return con.createQuery(sql).executeAndFetch(Contact.class);
    }
  }

  @Override
  public boolean equals(Object otherContact) {
    if (!(otherContact instanceof Contact)) {
      return false;
    } else {
      Contact newContact = (Contact) otherContact;
      return this.getName().equals(newContact.getName()) &&
             this.getId() == newContact.getId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO contacts (name) VALUES (:name);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Contact find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM contacts WHERE id=:id;";
      Contact contact = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Contact.class);
      return contact;
    }
  }

  public List<Entry> getEntries() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM entries WHERE contactId=:id;";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Entry.class);
    }
  }

}

import org.sql2o.*;
import java.util.List;

public class Entry {
  private String name;
  private String phone;
  private String mailing;
  private String email;
  private int id;
  private int contactId;


  public Entry(String name, String phone, String mailing, String email, int contactId) {
    this.name = name;
    this.phone = phone;
    this.mailing = mailing;
    this.email = email;
    this.contactId = contactId;
  }

  public String getName() {
    return this.name;
  }

  public String getPhone() {
    return this.phone;
  }

  public String getMailing() {
    return this.mailing;
  }

  public String getEmail() {
    return this.email;
  }

  public int getContactId() {
    return this.contactId;
  }

  public int getId() {
    return this.id;
  }

  public static List<Entry> all() {
    String sql = "SELECT id, name, phone, mailing, email, contactId FROM entries;";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Entry.class);
    }
  }

  @Override
  public boolean equals(Object otherEntry) {
    if (!(otherEntry instanceof Entry)) {
      return false;
    } else {
      Entry newEntry = (Entry) otherEntry;
      return this.getName().equals(newEntry.getName()) &&
             this.getPhone().equals(newEntry.getPhone()) &&
             this.getMailing().equals(newEntry.getMailing()) &&
             this.getEmail().equals(newEntry.getEmail()) &&
             this.getId() == newEntry.getId() &&
             this.getContactId() == newEntry.getContactId();
    }
  }

  public void save() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO entries (name, phone, mailing, email, contactId) VALUES (:name, :phone, :mailing, :email, :contactId);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("phone", this.phone)
        .addParameter("mailing", this.mailing)
        .addParameter("email", this.email)
        .addParameter("contactId", this.contactId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Entry find(int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM entries WHERE id=:id;";
      Entry entry = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Entry.class);
      return entry;
    }
  }


}

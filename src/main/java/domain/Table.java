package domain;

public class Table extends Entity<String> {
    public Table(String id) {
        super.setId(id);
    }

    @Override
    public String toString() {
        return "Table{" +
                "id='" + super.getId() + '\'' +
                '}';
    }
}

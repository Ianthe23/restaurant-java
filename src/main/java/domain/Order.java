package domain;

import enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public class Order extends Entity<String>{
    private Table table;
    private LocalDateTime date;
    private Status status;
    private List<MenuItem> items;

    public Order(String id, Table table, LocalDateTime date, Status status, List<MenuItem> items) {
        super.setId(id);
        this.table = table;
        this.date = date;
        this.status = status;
        this.items = items;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + super.getId() + '\'' +
                ", table=" + table +
                ", date=" + date +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}

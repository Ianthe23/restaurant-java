package domain;

import enums.Status;

import java.time.LocalDateTime;

public class Order extends Entity<String>{
    private Table table;
    private LocalDateTime date;
    private Status status;

    public Order(String id, Table table, LocalDateTime date, Status status) {
        super.setId(id);
        this.table = table;
        this.date = date;
        this.status = status;
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

    @Override
    public String toString() {
        return "Order{" +
                "id='" + super.getId() + '\'' +
                ", table=" + table +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}

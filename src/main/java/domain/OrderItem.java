package domain;

public class OrderItem extends Entity<Pair<String, String>>{
    private Order order;
    private MenuItem menuItem;

    public OrderItem(Order order, MenuItem menuItem) {
        super.setId(new Pair(order.getId(), menuItem.getId()));
        this.order = order;
        this.menuItem = menuItem;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "order=" + order +
                ", menuItem=" + menuItem +
                '}';
    }
}

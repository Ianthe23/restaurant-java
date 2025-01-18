package repo.database;

import domain.*;
import domain.validator.IValidator;
import enums.Status;
import repo.database.utils.AbstractDataBaseRepo;
import repo.database.utils.DataBaseAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderItemDataBaseRepo extends AbstractDataBaseRepo<Pair<String, String>, OrderItem> {
    private static final String GET_WITH_SUBENTITES_SQL = "select o.*," +
            "or.table_id as table_id, or.date as date, or.status as status," +
            "mi.id as menu_item_id, mi.category as category, mi.item as item, mi.price as price, mi.currency as currency" +
            "from order_item o" +
            "left join restaurant_order or on o.order_id = or.id" +
            "left join menu_item mi on o.menu_item_id = mi.id";
    private static final String GET_BY_ID_WITH_SUBENTITES_SQL = GET_WITH_SUBENTITES_SQL + " where o.order_id = ? and o.menu_item_id = ?";
    private static final String INSERT_SQL = "INSERT INTO order_item (order_id, menu_item_id) VALUES (?, ?) RETURNING order_id, menu_item_id";
    private static final String DELETE_SQL = "DELETE FROM order_item WHERE order_id = ? and menu_item_id = ?";
    private static final String UPDATE_SQL = "UPDATE order_item SET quantity = ? WHERE order_id = ? and menu_item_id = ?";

    public OrderItemDataBaseRepo(IValidator validator, DataBaseAcces data, String table) {
        super(validator, data, table);
    }

    private static OrderItem getOrderItem(ResultSet resultSet) throws SQLException {
        String order_id = resultSet.getString("order_id");
        String menu_item_id = resultSet.getString("menu_item_id");
        int quantity = resultSet.getInt("quantity");

        String table_id = resultSet.getString("table_id");
        Table table = new Table(table_id);

        Timestamp dateTs = resultSet.getTimestamp("date");
        LocalDateTime date = dateTs != null ? dateTs.toLocalDateTime() : null;
        String status = resultSet.getString("status");

        //check if status is from enum Status
        Status statusEnum = Status.valueOf(status);

        Order order = new Order(order_id, table, date, statusEnum);
        String category = resultSet.getString("category");
        String item = resultSet.getString("item");
        double price = resultSet.getDouble("price");
        String currency = resultSet.getString("currency");
        MenuItem menuItem = new MenuItem(menu_item_id, category, item, price, currency);

        return new OrderItem(order, menuItem);
    }

    @Override
    public Optional<OrderItem> findOne(Pair<String, String> stringStringPair) {
        try (PreparedStatement statement = data.createStatement(GET_BY_ID_WITH_SUBENTITES_SQL)) {
            statement.setString(1, UUID.fromString(stringStringPair.getLeft()).toString());
            statement.setString(2, UUID.fromString(stringStringPair.getRight()).toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getOrderItem(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<OrderItem> findAll() {
        List<OrderItem> orderItems = new ArrayList<>();
        try (PreparedStatement statement = data.createStatement(GET_WITH_SUBENTITES_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orderItems.add(getOrderItem(resultSet));
            }
            return orderItems;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OrderItem> save(OrderItem entity) {
        try (PreparedStatement statement = data.createStatement(INSERT_SQL)) {
            statement.setString(1, entity.getOrder().getId());
            statement.setString(2, entity.getMenuItem().getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(entity);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OrderItem> delete(Pair<String, String> stringStringPair) {
        try (PreparedStatement statement = data.createStatement(DELETE_SQL)) {
            statement.setString(1, UUID.fromString(stringStringPair.getLeft()).toString());
            statement.setString(2, UUID.fromString(stringStringPair.getRight()).toString());
            OrderItem existingEntity = findOne(stringStringPair).orElse(null);
            int response = 0;
            if (existingEntity != null) {
                response = statement.executeUpdate();
            }
            return response == 0 ? Optional.empty() : Optional.of(existingEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OrderItem> update(OrderItem entity) {
        return Optional.empty();
    }
}

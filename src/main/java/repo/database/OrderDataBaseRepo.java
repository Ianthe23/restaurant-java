package repo.database;

import domain.MenuItem;
import domain.Order;
import domain.Table;
import domain.validator.IValidator;
import repo.database.utils.AbstractDataBaseRepo;
import repo.database.utils.DataBaseAcces;
import enums.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderDataBaseRepo extends AbstractDataBaseRepo<String, Order> {
    private static final String GET_ALL_IDS_SQL = """
            SELECT id FROM restaurant_order;
            """;
    private static final String GET_BY_ID_WITH_SUBENTITY_SQL = """
            SELECT
                o.id AS order_id,
                o.table_id AS table_id,
                o.date AS order_date,
                o.status AS order_status,
                m.id AS menu_item_id,
                m.category AS menu_item_category,
                m.item AS menu_item_name,
                m.price AS menu_item_price,
                m.currency AS menu_item_currency
            FROM restaurant_order o
            JOIN order_item oi ON o.id = oi.order_id
            JOIN menu_item m ON oi.menu_item_id = m.id
            WHERE o.id = ?
            """;
    private static final String INSERT_INTO_ORDER_SQL = """
            INSERT INTO restaurant_order (table_id, date, status)
            VALUES (?, ?, ?)
            RETURNING id;
            """;
    private static final String INSERT_INTO_ORDER_ITEMS_SQL = """
            INSERT INTO order_item (order_id, menu_item_id)
            VALUES (?, ?);
            """;
    private static final String DELETE_SQL = """
            DELETE FROM restaurant_order WHERE id = ?;
            """;
    private static final String UPDATE_SQL = """
            UPDATE restaurant_order
            SET table_id = ?, date = ?, status = ?
            WHERE id = ?;
            """;

    public OrderDataBaseRepo(IValidator validator, DataBaseAcces data, String table) {
        super(validator, data, table);
    }

    private static Order getOrder(ResultSet resultSet) throws SQLException {
        // Extract the order details (from the first row for this order)
        String orderId = resultSet.getString("order_id");
        String tableId = resultSet.getString("table_id");
        Table table = new Table(tableId);
        Timestamp dateTs = resultSet.getTimestamp("order_date");
        LocalDateTime date = dateTs != null ? dateTs.toLocalDateTime() : null;
        String status = resultSet.getString("order_status");
        Status statusEnum = Status.valueOf(status);

        // Collect menu items for this order
        List<MenuItem> menuItems = new ArrayList<>();

        // Process the first menu item row
        do {
            // Extract menu item details
            String currentOrderId = resultSet.getString("order_id");

            // If we have already encountered a different order, stop processing
            if (!currentOrderId.equals(orderId)) {
                break;
            }

            String menuItemId = resultSet.getString("menu_item_id");
            String category = resultSet.getString("menu_item_category");
            String name = resultSet.getString("menu_item_name");
            double price = resultSet.getDouble("menu_item_price");
            String currency = resultSet.getString("menu_item_currency");

            // Add menu item to the list
            MenuItem menuItem = new MenuItem(menuItemId, category, name, price, currency);
            menuItems.add(menuItem);

        } while (resultSet.next()); // Move to the next row

        // Return the fully populated order
        return new Order(orderId, table, date, statusEnum, menuItems);
    }

    @Override
    public Optional<Order> findOne(String s) {
        try (PreparedStatement statement = data.createStatement(GET_BY_ID_WITH_SUBENTITY_SQL)) {
            statement.setObject(1, UUID.fromString(s));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getOrder(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = data.createStatement(GET_ALL_IDS_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String orderId = resultSet.getObject("id", UUID.class).toString();
                Optional<Order> order = findOne(orderId);
                order.ifPresent(orders::add);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> save(Order entity) {
        try (PreparedStatement statement = data.createStatement(INSERT_INTO_ORDER_SQL)) {
            statement.setObject(1, UUID.fromString(entity.getTable().getId()));
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setString(3, entity.getStatus().name());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String generatedId = resultSet.getString("id");
                entity.setId(generatedId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement statement = data.createStatement(INSERT_INTO_ORDER_ITEMS_SQL)) {
            for (MenuItem menuItem : entity.getItems()) {
                statement.setObject(1, UUID.fromString(entity.getId()));
                statement.setObject(2, UUID.fromString(menuItem.getId()));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Order> delete(String s) {
        try (PreparedStatement statement = data.createStatement(DELETE_SQL)) {
            statement.setString(1, s);
            Order existingEntity = findOne(s).orElse(null);
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
    public Optional<Order> update(Order entity) {
        try (PreparedStatement statement = data.createStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getTable().getId());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setString(3, entity.getStatus().name());
            statement.setString(4, entity.getId());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

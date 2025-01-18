package repo.database;

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

public class OrderDataBaseRepo extends AbstractDataBaseRepo<String, Order> {
    private static final String GET_ALL_WITH_SUBENTITY_SQL = "SELECT o.* FROM orders o left join restaurant_table t on o.table_id = t.id";
    private static final String GET_BY_ID_WITH_SUBENTITY_SQL = GET_ALL_WITH_SUBENTITY_SQL + " where o.id = ?";
    private static final String INSERT_SQL = "INSERT INTO restaurant_order () VALUES (table_id, date, status) RETURNING id";
    private static final String DELETE_SQL = "DELETE FROM restaurant_order WHERE id = ? ON DELETE CASCADE";
    private static final String UPDATE_SQL = "UPDATE restaurant_order SET table_id = ?, date = ?, status = ? WHERE id = ?";

    public OrderDataBaseRepo(IValidator validator, DataBaseAcces data, String table) {
        super(validator, data, table);
    }

    private static Order getOrder(ResultSet resultSet) throws SQLException {
        String table_id = resultSet.getString("table_id");
        Table table = new Table(table_id);

        String id = resultSet.getString("id");
        String tableId = resultSet.getString("table_id");
        Timestamp dateTs = resultSet.getTimestamp("date");
        LocalDateTime date = dateTs != null ? dateTs.toLocalDateTime() : null;
        String status = resultSet.getString("status");

        //check if status is from enum Status
        Status statusEnum = Status.valueOf(status);

        return new Order(id, table, date, statusEnum);
    }

    @Override
    public Optional<Order> findOne(String s) {
        try (PreparedStatement statement = data.createStatement(GET_BY_ID_WITH_SUBENTITY_SQL)) {
            statement.setString(1, s);
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
        try (PreparedStatement statement = data.createStatement(GET_ALL_WITH_SUBENTITY_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                orders.add(getOrder(resultSet));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> save(Order entity) {
        try (PreparedStatement statement = data.createStatement(INSERT_SQL)) {
            statement.setString(1, entity.getTable().getId());
            statement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            statement.setString(3, entity.getStatus().name());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String generatedId = resultSet.getString("id");
                entity.setId(generatedId);
                return Optional.of(entity);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

package repo.database;

import domain.MenuItem;
import domain.Table;
import domain.validator.IValidator;
import repo.database.utils.AbstractDataBaseRepo;
import repo.database.utils.DataBaseAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemDataBaseRepo extends AbstractDataBaseRepo<String, MenuItem> {
    private static final String GET_BY_ID_SQL = "SELECT * FROM menu_item WHERE id = ?";
    private static final String GET_ALL_SQL = "SELECT * FROM menu_item";
    private static final String INSERT_SQL = "INSERT INTO menu_item (category, item, price, currency) VALUES (?, ?, ?, ?) RETURNING id";
    private static final String DELETE_SQL = "DELETE FROM menu_item WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE menu_item SET category = ?, item = ?, price = ?, currency = ? WHERE id = ?";
    private static final String GET_BY_CATEGORY_SQL = "SELECT category, item, price, currency FROM menu_item ORDER BY category, item";

    public MenuItemDataBaseRepo(IValidator validator, DataBaseAcces data, String table) {
        super(validator, data, table);
    }

    private static MenuItem getMenuItem(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String category = resultSet.getString("category");
        String item = resultSet.getString("item");
        double price = resultSet.getDouble("price");
        String currency = resultSet.getString("currency");
        return new MenuItem(id, category, item, price, currency);
    }

    private List<MenuItem> getList(String sql) {
        List<MenuItem> menus = new ArrayList<>();
        try (PreparedStatement statement = data.createStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                menus.add(getMenuItem(resultSet));
            }
            return menus;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<MenuItem> findOne(String s) {
        try (PreparedStatement statement = data.createStatement(GET_BY_ID_SQL)) {
            statement.setString(1, s);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(getMenuItem(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<MenuItem> findAll() {
        return getList(GET_ALL_SQL);
    }

    @Override
    public Optional<MenuItem> save(MenuItem entity) {
        try (PreparedStatement statement = data.createStatement(INSERT_SQL)) {
            statement.setString(1, entity.getCategory());
            statement.setString(2, entity.getItem());
            statement.setDouble(3, entity.getPrice());
            statement.setString(4, entity.getCurrency());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String generatedId = resultSet.getString("id");
                    entity.setId(generatedId);
                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<MenuItem> delete(String s) {
        try (PreparedStatement statement = data.createStatement(DELETE_SQL)) {
            statement.setString(1, s);
            MenuItem existingEntity = findOne(s).orElse(null);
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
    public Optional<MenuItem> update(MenuItem entity) {
        try (PreparedStatement statement = data.createStatement(UPDATE_SQL)) {
            statement.setString(1, entity.getCategory());
            statement.setString(2, entity.getItem());
            statement.setDouble(3, entity.getPrice());
            statement.setString(4, entity.getCurrency());
            statement.setString(5, entity.getId());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.empty() : Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable<MenuItem> getAllByCategory() {
        return getList(GET_BY_CATEGORY_SQL);
    }
}

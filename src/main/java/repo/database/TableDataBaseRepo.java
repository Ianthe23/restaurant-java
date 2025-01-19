package repo.database;

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
import java.util.UUID;

public class TableDataBaseRepo extends AbstractDataBaseRepo<String, Table> {
    private static final String GET_BY_ID_SQL = "select * from restaurant_table where id = ?";
    private static final String GET_ALL_SQL = "select * from restaurant_table";
    private static final String INSERT_SQL = "insert into restaurant_table returning id";
    private static final String DELETE_SQL = "delete from restaurant_table where id = ?";
    //private static final String UPDATE_SQL = "update restaurant_table set id = ? where id = ?";


    public TableDataBaseRepo(IValidator validator, DataBaseAcces data, String table) {
        super(validator, data, table);
    }

    private static Table getTable(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        return new Table(id);
    }

    private List<Table> getList(String sql) {
        List<Table> tables = new ArrayList<>();
        try (PreparedStatement statement = data.createStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tables.add(getTable(resultSet));
            }
            return tables;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Table> findOne(String s) {
        try {
            PreparedStatement statement = data.createStatement(GET_BY_ID_SQL);
            statement.setObject(1, UUID.fromString(s));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getTable(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Table> findAll() {
        return getList(GET_ALL_SQL);
    }

    @Override
    public Optional<Table> save(Table entity) {
        try {
            PreparedStatement statement = data.createStatement(INSERT_SQL);
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
    public Optional<Table> delete(String s) {
        try {
            PreparedStatement statement = data.createStatement(DELETE_SQL);
            statement.setString(1, s);
            Optional<Table> existingEntity = findOne(s);
            int response = 0;
            if (existingEntity.isPresent()) {
                response = statement.executeUpdate();
            }
            return response == 0 ? Optional.empty() : existingEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Table> update(Table entity) {
        return Optional.empty();
    }
}

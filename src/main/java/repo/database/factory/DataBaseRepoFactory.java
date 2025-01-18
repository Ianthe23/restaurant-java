package repo.database.factory;

import exceptions.RepoException;
import repo.database.MenuItemDataBaseRepo;
import repo.database.OrderDataBaseRepo;
import repo.database.TableDataBaseRepo;
import repo.database.utils.AbstractDataBaseRepo;
import repo.database.utils.DataBaseAcces;
import domain.validator.IValidator;

public class DataBaseRepoFactory implements IDataBaseFactory {
    private final DataBaseAcces data;

    /**
     * Constructor
     * @param data - the database access
     */
    public DataBaseRepoFactory(DataBaseAcces data ) {
        this.data = data;
    }

    /**
     * Method to create a repository
     * @param strategy - the strategy
     * @param validator - the validator
     * @return AbstractDataBaseRepo
     */
    @Override
    public AbstractDataBaseRepo createRepo(EDataBaseStrategy strategy, IValidator validator) {
        switch (strategy) {
            case restaurant_table -> {
                return new TableDataBaseRepo(validator, data, strategy.toString());
            }
            case restaurant_order -> {
                return new OrderDataBaseRepo(validator, data, strategy.toString());
            }
            case menu_item -> {
                return new MenuItemDataBaseRepo(validator, data, strategy.toString());
            }
            default -> {
                throw new RepoException("Invalid strategy");
            }
        }
    }
}

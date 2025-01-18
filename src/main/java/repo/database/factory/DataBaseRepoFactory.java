package repo.database.factory;

import exceptions.RepoException;
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
            case TABLE -> {
                return new TableDataBaseRepo(validator, data, strategy.toString());
            }
            case ORDER -> {
                return new OrderDataBaseRepo(validator, data, strategy.toString());
            }
            case ORDER_ITEM -> {
                return new OrderItemDataBaseRepo(validator, data, strategy.toString());
            }
            case MENU_ITEM -> {
                return new MenuItemDataBaseRepo(validator, data, strategy.toString());
            }
            default -> {
                throw new RepoException("Invalid strategy");
            }
        }
    }
}

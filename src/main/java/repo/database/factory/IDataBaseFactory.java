package repo.database.factory;

import domain.validator.IValidator;
import repo.database.utils.AbstractDataBaseRepo;

public interface IDataBaseFactory {
    /**
     * Method to create a repository
     * @param strategy - the strategy
     * @param validator - the validator
     * @return AbstractDataBaseRepo
     */
    AbstractDataBaseRepo createRepo(EDataBaseStrategy strategy, IValidator validator);
}

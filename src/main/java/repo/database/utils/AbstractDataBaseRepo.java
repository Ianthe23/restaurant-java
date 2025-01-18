package repo.database.utils;

import domain.Entity;
import domain.validator.IValidator;
import repo.IRepository;

public abstract class AbstractDataBaseRepo <ID, E extends Entity<ID>> implements IRepository<ID, E> {
    protected IValidator validator;
    protected DataBaseAcces data;
    protected String table;

    /**
     * Constructor
     * @param validator - the validator
     * @param data - the database access
     * @param table - the table name
     */
    public AbstractDataBaseRepo(IValidator validator, DataBaseAcces data, String table) {
        this.validator = validator;
        this.data = data;
        this.table = table;
    }

    /**
     * Constructor
     */
    public AbstractDataBaseRepo() {
        super();
    }
}

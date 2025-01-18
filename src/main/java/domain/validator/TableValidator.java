package domain.validator;

import domain.Table;
import exceptions.ValidationException;

public class TableValidator implements IValidator<Table> {
    @Override
    public void validate(Table entity) throws ValidationException {
        if (entity.getId() == null || entity.getId().equals("")) {
            throw new ValidationException("The id cannot be empty!");
        }
    }
}

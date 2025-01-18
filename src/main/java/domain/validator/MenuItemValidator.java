package domain.validator;

import domain.MenuItem;
import exceptions.ValidationException;

public class MenuItemValidator implements IValidator<MenuItem> {
    @Override
    public void validate(MenuItem entity) throws ValidationException {
        String errors = "";
        if (entity.getCategory().equals("")) {
            errors += "The category cannot be empty!\n";
        }
        if (entity.getItem().equals("")) {
            errors += "The item cannot be empty!\n";
        }
        if (entity.getPrice() < 0) {
            errors += "The price cannot be negative!\n";
        }
        if (entity.getCurrency().equals("")) {
            errors += "The currency cannot be empty!\n";
        }
        if (!errors.equals("")) {
            throw new ValidationException(errors);
        }
    }
}

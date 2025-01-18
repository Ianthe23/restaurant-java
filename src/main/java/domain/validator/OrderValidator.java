package domain.validator;

import domain.Order;
import exceptions.ValidationException;

public class OrderValidator implements IValidator<Order> {
    @Override
    public void validate(Order entity) throws ValidationException {
        String errors = "";
        if (entity.getTable() == null) {
            errors += "The table cannot be null!\n";
        }
        if (entity.getId().getLeft() == null || entity.getId().getRight() == null) {
            errors += "The id cannot be null!\n";
        }
        if (entity.getDate().equals("")) {
            errors += "The date cannot be empty!\n";
        }
        if (entity.getStatus() == null) {
            errors += "The status cannot be null!\n";
        }
        if (!errors.equals("")) {
            throw new ValidationException(errors);
        }
    }
}

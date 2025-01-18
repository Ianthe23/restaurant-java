package domain.validator;

import domain.OrderItem;
import exceptions.ValidationException;

public class OrderItemValidator implements IValidator<OrderItem> {
    /**
     * Method for validating an order item
     * @param orderItem - the order item to be validated
     * @throws ValidationException if the order item is not valid
     */
    @Override
    public void validate(OrderItem orderItem) throws ValidationException {
        String errors = "";
        if (orderItem.getId().getLeft() == null || orderItem.getId().getRight() == null) {
            errors += "Invalid order id or menuItem id!\n";
        }
        if (!errors.equals("")) {
            throw new ValidationException(errors);
        }
    }
}

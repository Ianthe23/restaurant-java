package domain.validator;

public class ValidatorFactory implements IValidatorFactory{
    private static ValidatorFactory instance = null;

    private ValidatorFactory() {
    }

    public static ValidatorFactory getInstance() {
        if (instance == null) {
            instance = new ValidatorFactory();
        }
        return instance;
    }

    @Override
    public IValidator createValidator(EValidator strategy) {
        return switch (strategy) {
            case TABLE -> new TableValidator();
            case ORDER -> new OrderValidator();
            case ORDER_ITEM -> new OrderItemValidator();
            case MENU_ITEM -> new MenuItemValidator();
            default -> null;
        };
    }
}

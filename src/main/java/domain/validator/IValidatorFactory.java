package domain.validator;

public interface IValidatorFactory {
    /**
     * Method for creating a validator
     * @param strategy - the strategy for the validator
     * @return a new validator
     */
    IValidator createValidator(EValidator strategy);
}

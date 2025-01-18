package domain.validator;

import exceptions.ValidationException;

public interface IValidator<T> {
    /**
     * Method for validating an entity
     * @param entity - the entity to be validated
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;
}

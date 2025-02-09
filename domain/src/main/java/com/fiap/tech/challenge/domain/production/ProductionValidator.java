package com.fiap.tech.challenge.domain.production;

import com.fiap.tech.challenge.domain.validation.Error;
import com.fiap.tech.challenge.domain.validation.ValidationHandler;
import com.fiap.tech.challenge.domain.validation.Validator;

public class ProductionValidator extends Validator {

    private static final String ORDER_ID_SHOULD_NOT_BE_NULL = "'orderId' should not be null";
    private static final String ORDER_ID_SHOULD_NOT_BE_EMPTY = "'orderId' should not be empty";
    private static final String ITEMS_SHOULD_NOT_BE_EMPTY = "'items' should not be empty";

    private final Production production;

    protected ProductionValidator(final Production aProduction, final ValidationHandler aHandler) {
        super(aHandler);
        this.production = aProduction;
    }

    @Override
    public void validate() {
        validateOrderId();
        validateItems();
    }

    private void validateOrderId() {
        final var orderId = production.getOrderId();
        if (orderId == null) {
            getHandler().append(new Error(ORDER_ID_SHOULD_NOT_BE_NULL));
            return;
        }
        if (orderId.isEmpty()) {
            getHandler().append(new Error(ORDER_ID_SHOULD_NOT_BE_EMPTY));
        }
    }

    private void validateItems() {
        final var items = production.getItems();
        if (items.isEmpty()) {
            getHandler().append(new Error(ITEMS_SHOULD_NOT_BE_EMPTY));
        }
    }

}

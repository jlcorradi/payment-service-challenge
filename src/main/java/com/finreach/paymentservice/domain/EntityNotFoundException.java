package com.finreach.paymentservice.domain;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String entity, Object id) {
        super(String.format("Entity %s identified by %s not found", entity, id));
    }
}

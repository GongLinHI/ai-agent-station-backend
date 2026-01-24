package com.gonglin.ai4knowledge.infrastructure.mysql.validator;

import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;

public interface DatabaseEntityValidator<T> {
    Boolean validateBeforeInsert(T entity);

    Boolean validateBeforeDisable(T entity);

    Boolean validateBeforeEnable(T entity);

    Boolean validateBeforeDelete(T entity);

    Boolean validate(T entity, DatabaseActionType actionType);
}

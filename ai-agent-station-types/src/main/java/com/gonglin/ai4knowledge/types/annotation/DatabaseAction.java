package com.gonglin.ai4knowledge.types.annotation;

import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface DatabaseAction {
    DatabaseActionType value() default DatabaseActionType.SELECT;
}

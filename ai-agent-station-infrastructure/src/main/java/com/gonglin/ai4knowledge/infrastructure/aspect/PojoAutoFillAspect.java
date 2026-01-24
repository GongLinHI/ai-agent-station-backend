package com.gonglin.ai4knowledge.infrastructure.aspect;

import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collection;

@Aspect
@Component
@Order(2)
@Slf4j
public class PojoAutoFillAspect {
    @Pointcut("@annotation(com.gonglin.ai4knowledge.types.annotation.DatabaseAction)")
    private void pointcut() {
    }

    private Boolean isValid(Class<?> aClass) {
        String name = aClass.getName();
        return name.startsWith("com.gonglin.ai4knowledge.infrastructure.mysql.pojo");
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        log.debug("PojoAutoFillAspect before method execution");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DatabaseAction annotation = signature.getMethod().getAnnotation(DatabaseAction.class);
        DatabaseActionType actionType = annotation.value();

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null) {
                continue;
            }
            if (arg instanceof Collection<?> collection) {
                for (Object item : collection) {
                    if (item != null && isValid(item.getClass())) {
                        fillFields(item, actionType);
                    }
                }
            } else if (isValid(arg.getClass())) {
                fillFields(arg, actionType);
            }
        }
    }

    private void fillFields(Object pojo, DatabaseActionType actionType) {
        LocalDateTime now = LocalDateTime.now();
        try {
            switch (actionType) {
                case INSERT -> {
                    setFieldValue(pojo, "createTime", now);
                    setFieldValue(pojo, "updateTime", now);
                }
                case UPDATE -> {
                    setFieldValue(pojo, "updateTime", now);
                }
                case SOFT_DELETE -> {
                    setFieldValue(pojo, "deleteTime", now);
                }
                // 其他情况不用处理，直接忽略
                default -> {
                }
            }
        } catch (Exception e) {
            log.error("Failed to fill fields for pojo: {}", pojo.getClass().getName(), e);
        }
    }

    private void setFieldValue(Object obj, String fieldName, Object value) {
        Class<?> objClass = obj.getClass();
        try {
            Field field = objClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("字段 [{}] not found or inaccessible in class [{}]", fieldName, objClass.getName());
        }
    }
}

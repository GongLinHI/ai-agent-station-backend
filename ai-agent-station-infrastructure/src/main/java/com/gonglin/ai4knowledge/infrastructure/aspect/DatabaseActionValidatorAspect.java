package com.gonglin.ai4knowledge.infrastructure.aspect;

import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiAdvisor;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiApi;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiClient;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiMcpTool;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiModel;
import com.gonglin.ai4knowledge.infrastructure.mysql.pojo.AiSystemPrompt;
import com.gonglin.ai4knowledge.infrastructure.mysql.validator.DatabaseEntityValidator;
import com.gonglin.ai4knowledge.types.annotation.DatabaseAction;
import com.gonglin.ai4knowledge.types.enums.DatabaseActionType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Order(1)
@Slf4j
public class DatabaseActionValidatorAspect {

    @SuppressWarnings("rawtypes")
    private final Map<String, DatabaseEntityValidator> map;


    @Autowired
    public DatabaseActionValidatorAspect(Map<String, DatabaseEntityValidator> map) {
        this.map = map;
    }

    @Pointcut("@annotation(com.gonglin.ai4knowledge.types.annotation.DatabaseAction)")
    private void pointcut() {
    }


    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DatabaseAction annotation = signature.getMethod().getAnnotation(DatabaseAction.class);
        DatabaseActionType actionType = annotation.value();

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof AiApi) {
                map.get("AiApi").validate((AiApi) arg, actionType);
            } else if (arg instanceof AiAdvisor) {
                map.get("AiAdvisor").validate((AiAdvisor) arg, actionType);
            } else if (arg instanceof AiSystemPrompt) {
                map.get("AiSystemPrompt").validate((AiSystemPrompt) arg, actionType);
            } else if (arg instanceof AiMcpTool) {
                map.get("AiMcpTool").validate((AiMcpTool) arg, actionType);
            } else if (arg instanceof AiModel) {
                map.get("AiModel").validate((AiModel) arg, actionType);
            } else if (arg instanceof AiClient) {
                map.get("AiClient").validate((AiClient) arg, actionType);
            }
        }
    }
}

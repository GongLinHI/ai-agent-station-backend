package com.gonglin.ai4knowledge.domain.agent.service.assemble.node;


import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryCommand;
import com.gonglin.ai4knowledge.domain.agent.model.valobj.ArmoryDynamicContext;
import com.gonglin.ai4knowledge.domain.agent.repository.IAiAgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public abstract class AbstractArmorySupport extends AbstractMultiThreadStrategyRouter<ArmoryCommand, ArmoryDynamicContext, String> {
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    protected IAiAgentRepository iAiAgentRepository;
    @Autowired
    @Qualifier("jsonMapper")
    protected ObjectMapper jsonMapper;

    protected synchronized <T> void registerBean(String beanName, T beanInstance) {
        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

        // 如果已存在同名 BeanDefinition，先移除
        if (beanFactory.containsBeanDefinition(beanName)) {
            beanFactory.removeBeanDefinition(beanName);
            log.info("已存在同名BeanDefinition，执行替换 - beanName: {}", beanName);
        }

        // 构建 BeanDefinition，指定实例的类型和提供实例的 Supplier
        @SuppressWarnings("unchecked")
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(
                        (Class<T>) beanInstance.getClass(), () -> beanInstance)
                .setScope(BeanDefinition.SCOPE_SINGLETON)
                .getBeanDefinition();
        // 注册新的 BeanDefinition
        beanFactory.registerBeanDefinition(beanName, beanDefinition);

        log.info("成功注册Bean到IOC容器 - beanName: {}, beanType: {}",
                 beanName, beanInstance.getClass().getSimpleName());
    }

    protected <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }


}


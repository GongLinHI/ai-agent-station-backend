package com.gonglin.ai4knowledge.domain.agent.model.valobj;

import cn.bugstack.wrench.design.framework.tree.DynamicContext;
import com.google.common.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class ArmoryDynamicContext extends DynamicContext {
    private final Map<String, Object> dataObjects = new HashMap<>();

    public <T> void setValue(String key, T value) {
        this.dataObjects.put(key, value);
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) this.dataObjects.get(key);
    }

    // 快捷方法，用于常见类型
    public <T> T getValue(String key, Class<T> type) {
        return getValue(key, TypeToken.of(type));
    }

    public <T> T getValue(String key, TypeToken<T> typeToken) {
        Object value = this.dataObjects.get(key);
        if (value != null && !typeToken.getRawType().isInstance(value)) {
            throw new ClassCastException("Type mismatch for key: " + key);
        }
        @SuppressWarnings("unchecked")
        T result = (T) value;
        return result;
    }


    public boolean containsKey(String key) {
        return this.dataObjects.containsKey(key);
    }

}

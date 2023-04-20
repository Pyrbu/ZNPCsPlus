package io.github.znetworkw.znpcservers.reflection;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

public class ReflectionBuilder {
    private final String reflectionPackage;
    private String fieldName;
    private String additionalData;
    private final ArrayList<String> className = new ArrayList<>();
    private final ArrayList<String> methods = new ArrayList<>();
    private final ArrayList<Class<?>[]> parameterTypes = new ArrayList<>();
    private Class<?> expectType;

    public ReflectionBuilder(String reflectionPackage) {
        this(reflectionPackage, "", "", null);
    }

    protected ReflectionBuilder(String reflectionPackage, String fieldName, String additionalData, Class<?> expectType) {
        this.reflectionPackage = reflectionPackage;
        this.fieldName = fieldName;
        this.additionalData = additionalData;
        this.expectType = expectType;
    }

    public ReflectionBuilder withClassName(String className) {
        this.className.add(ReflectionPackage.join(reflectionPackage, additionalData, className));
        return this;
    }

    public ReflectionBuilder withClassName(Class<?> clazz) {
        className.add(clazz.getName());
        return this;
    }

    public ReflectionBuilder withMethodName(String methodName) {
        this.methods.add(methodName);
        return this;
    }

    public ReflectionBuilder withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public ReflectionBuilder withSubClass(String additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    public ReflectionBuilder withParameterTypes(Class<?>... types) {
        this.parameterTypes.add(types);
        return this;
    }

    public ReflectionBuilder withExpectResult(Class<?> expectType) {
        this.expectType = expectType;
        return this;
    }

    public Class<?> getExpectType() {
        return expectType;
    }

    public ImmutableList<Class<?>[]> getParameterTypes() {
        return ImmutableList.copyOf(this.parameterTypes);
    }

    public ImmutableList<String> getClassName() {
        return ImmutableList.copyOf(this.className);
    }

    public ImmutableList<String> getMethods() {
        return ImmutableList.copyOf(this.methods);
    }

    public String getPackage() {
        return reflectionPackage;
    }

    public String getFieldName() {
        return fieldName;
    }
}
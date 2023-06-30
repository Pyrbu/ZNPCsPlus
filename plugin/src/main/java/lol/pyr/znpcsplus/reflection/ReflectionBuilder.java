package lol.pyr.znpcsplus.reflection;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.google.common.collect.ImmutableList;
import lol.pyr.znpcsplus.reflection.types.ClassReflection;
import lol.pyr.znpcsplus.reflection.types.FieldReflection;
import lol.pyr.znpcsplus.reflection.types.MethodReflection;

import java.util.ArrayList;

public class ReflectionBuilder {
    private final String reflectionPackage;
    private String fieldName;
    private String additionalData;
    private final ArrayList<String> className = new ArrayList<>();
    private final ArrayList<String> methods = new ArrayList<>();
    private final ArrayList<Class<?>[]> parameterTypes = new ArrayList<>();
    private Class<?> expectType;
    private boolean strict = true;

    public ReflectionBuilder() {
        this("");
    }

    public ReflectionBuilder(Class<?> clazz) {
        this("");
        withClassName(clazz);
    }

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
        this.className.add(ReflectionPackage.joinWithDot(reflectionPackage, PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_17) ? additionalData : "", className));
        return this;
    }

    public ReflectionBuilder withRawClassName(String className) {
        this.className.add(className);
        return this;
    }

    public ReflectionBuilder withClassName(Class<?> clazz) {
        if (clazz != null) className.add(clazz.getName());
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

    public ReflectionBuilder setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    public boolean isStrict() {
        return strict;
    }

    public Class<?> getExpectType() {
        return expectType;
    }

    public ImmutableList<Class<?>[]> getParameterTypes() {
        return ImmutableList.copyOf(this.parameterTypes);
    }

    public ImmutableList<String> getClassNames() {
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

    public MethodReflection toMethodReflection() {
        return new MethodReflection(this);
    }

    public ClassReflection toClassReflection() {
        return new ClassReflection(this);
    }

    public FieldReflection toFieldReflection() {
        return new FieldReflection(this);
    }
}
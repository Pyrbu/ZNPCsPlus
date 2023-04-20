package io.github.znetworkw.znpcservers.reflection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.znetworkw.znpcservers.utility.Utils;
import lol.pyr.znpcsplus.ZNPCsPlus;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface TypeCache {
    class ClassCache {
        protected static final ConcurrentMap<CacheKey, Object> CACHE = new ConcurrentHashMap<>();

        public static Object find(String name, Class<?> objectClass) {
            return CACHE.get(new CacheKey(name, objectClass));
        }

        public static void register(String name, Object object, Class<?> objectClass) {
            CACHE.putIfAbsent(new CacheKey(name, objectClass), object);
        }

        private static class CacheKey {
            private final Class<?> type;

            private final String value;

            public CacheKey(String value, Class<?> type) {
                this.type = type;
                this.value = value;
            }

            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                CacheKey classKey = (CacheKey) o;
                return (Objects.equals(this.type, classKey.type) && Objects.equals(this.value, classKey.value));
            }

            public int hashCode() {
                return Objects.hash(this.type, this.value);
            }
        }
    }

    class ReflectionBuilder {
        private final ReflectionBasePackage reflectionBasePackage;

        private final ReflectionTopPackage reflectionTopPackage;

        private final String fieldName;

        private final List<String> className;

        private final List<String> methods;

        private final String additionalData;

        private final ImmutableList<Class<?>[]> parameterTypes;

        private final Class<?> expectType;

        public ReflectionBuilder(ReflectionBasePackage reflectionBasePackage) {
            this(reflectionBasePackage, ReflectionTopPackage.DEFAULT, new ArrayList<>(), "", new ArrayList<>(), "", ImmutableList.of(), null);
        }

        protected ReflectionBuilder(ReflectionBasePackage reflectionBasePackage, ReflectionTopPackage reflectionTopPackage, List<String> className, String fieldName, List<String> methods, String additionalData, ImmutableList<Class<?>[]> parameterTypes, Class<?> expectType) {
            this.reflectionBasePackage = reflectionBasePackage;
            this.reflectionTopPackage = reflectionTopPackage;
            this.className = className;
            this.methods = methods;
            this.fieldName = fieldName;
            this.additionalData = additionalData;
            this.parameterTypes = parameterTypes;
            this.expectType = expectType;
        }

        public ReflectionBuilder withCategory(ReflectionTopPackage reflectionTopPackage) {
            return new ReflectionBuilder(this.reflectionBasePackage, reflectionTopPackage, this.className, this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public ReflectionBuilder withClassName(String className) {
            return new ReflectionBuilder(this.reflectionBasePackage, this.reflectionTopPackage, new ImmutableList.Builder<String>().addAll(this.className).add(formatClass(className)).build(), this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public ReflectionBuilder withClassName(Class<?> clazz) {
            return new ReflectionBuilder(this.reflectionBasePackage, this.reflectionTopPackage, new ImmutableList.Builder<String>().addAll(this.className).add((clazz == null) ? "" : clazz.getName()).build(), this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public ReflectionBuilder withMethodName(String methodName) {
            return new ReflectionBuilder(this.reflectionBasePackage, this.reflectionTopPackage, this.className, this.fieldName, new ImmutableList.Builder<String>().addAll(this.methods).add(methodName).build(), this.additionalData, this.parameterTypes, this.expectType);
        }

        public ReflectionBuilder withFieldName(String fieldName) {
            return new ReflectionBuilder(this.reflectionBasePackage, this.reflectionTopPackage, this.className, fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public ReflectionBuilder withAdditionalData(String additionalData) {
            return new ReflectionBuilder(this.reflectionBasePackage, this.reflectionTopPackage, this.className, this.fieldName, this.methods, additionalData, this.parameterTypes, this.expectType);
        }

        public ReflectionBuilder withParameterTypes(Class<?>... types) {
            return new ReflectionBuilder(this.reflectionBasePackage, this.reflectionTopPackage, this.className, this.fieldName, this.methods, this.additionalData, ImmutableList.copyOf(Iterables.concat(this.parameterTypes, ImmutableList.of(types))), this.expectType);
        }

        public ReflectionBuilder withExpectResult(Class<?> expectType) {
            return new ReflectionBuilder(this.reflectionBasePackage, this.reflectionTopPackage, this.className, this.fieldName, this.methods, this.additionalData, this.parameterTypes, expectType);
        }

        protected String formatClass(String className) {
            return switch (this.reflectionBasePackage) {
                case MINECRAFT_SERVER, CRAFT_BUKKIT -> String.format(((this.reflectionBasePackage == ReflectionBasePackage.CRAFT_BUKKIT) ? this.reflectionBasePackage.getFixedPackageName() : this.reflectionBasePackage.getForCategory(this.reflectionTopPackage, this.additionalData)) + ".%s", className);
                case DEFAULT -> className;
            };
        }
    }

    abstract class BaseReflection<T> {
        protected final ReflectionBuilder reflectionBuilder;

        protected Class<?> BUILDER_CLASS;

        private T cached;

        private boolean loaded = false;

        protected BaseReflection(ReflectionBuilder reflectionBuilder) {
            this.reflectionBuilder = reflectionBuilder;
            for (String classes : reflectionBuilder.className) {
                try {
                    this.BUILDER_CLASS = Class.forName(classes);
                } catch (ClassNotFoundException ignored) {
                }
            }
        }

        public T load() {
            return load(false);
        }

        public T load(boolean missAllowed) {
            if (this.loaded) return this.cached;
            try {
                if (this.BUILDER_CLASS == null) throw new ClassNotFoundException("No class found: " + this.reflectionBuilder.className.toString());
                T eval = (this.cached != null) ? this.cached : (this.cached = onLoad());
                if (eval == null) throw new NullPointerException();
            } catch (Throwable throwable) {
                if (!missAllowed) {
                    log("Cache load failed!");
                    log("Class Names: " + this.reflectionBuilder.className.toString());
                    log("Loader Type: " + getClass().getCanonicalName());
                    log("Bukkit Version: " + Utils.BUKKIT_VERSION + " (" + Utils.getBukkitPackage() + ")");
                    log("Exception:");
                    throwable.printStackTrace();
                }
            }
            this.loaded = true;
            return this.cached;
        }

        private void log(String message) {
            ZNPCsPlus.LOGGER.warning("[ReflectionCache] " + message);
        }

        protected abstract T onLoad() throws Exception;

        public static class ClassReflection extends BaseReflection<Class<?>> {
            public ClassReflection(ReflectionBuilder reflectionBuilder) {
                super(reflectionBuilder);
            }

            protected Class<?> onLoad() {
                return this.BUILDER_CLASS;
            }
        }

        public static class MethodReflection extends BaseReflection<Method> {
            public MethodReflection(ReflectionBuilder builder) {
                super(builder);
            }

            protected Method onLoad() {
                Method methodThis = null;
                List<String> methods = this.reflectionBuilder.methods;
                boolean hasExpectedType = (this.reflectionBuilder.expectType != null);
                if (methods.isEmpty() && hasExpectedType) for (Method method : this.BUILDER_CLASS.getDeclaredMethods()) {
                    if (method.getReturnType() == this.reflectionBuilder.expectType) return method;
                }
                for (String methodName : this.reflectionBuilder.methods) try {
                    Method maybeGet;
                    if (!Iterables.isEmpty(this.reflectionBuilder.parameterTypes))
                        maybeGet = this.BUILDER_CLASS.getDeclaredMethod(methodName, Iterables.get(this.reflectionBuilder.parameterTypes, 0));
                    else maybeGet = this.BUILDER_CLASS.getDeclaredMethod(methodName);
                    if (this.reflectionBuilder.expectType != null && this.reflectionBuilder.expectType != maybeGet.getReturnType()) continue;
                    maybeGet.setAccessible(true);
                    methodThis = maybeGet;
                } catch (NoSuchMethodException ignored) {}
                return methodThis;
            }
        }

        public static class FieldReflection extends BaseReflection<Field> {
            public FieldReflection(ReflectionBuilder reflectionBuilder) {
                super(reflectionBuilder);
            }

            protected Field onLoad() throws NoSuchFieldException {
                if (this.reflectionBuilder.expectType != null)
                    for (Field field1 : this.BUILDER_CLASS.getDeclaredFields()) {
                        if (field1.getType() == this.reflectionBuilder.expectType) {
                            field1.setAccessible(true);
                            return field1;
                        }
                    }
                Field field = this.BUILDER_CLASS.getDeclaredField(this.reflectionBuilder.fieldName);
                field.setAccessible(true);
                return field;
            }

            public AsValueField asValueField() {
                return new AsValueField(this);
            }

            private static class AsValueField extends BaseReflection<Object> {
                private final FieldReflection fieldReflection;

                public AsValueField(FieldReflection fieldReflection) {
                    super(fieldReflection.reflectionBuilder);
                    this.fieldReflection = fieldReflection;
                }

                protected Object onLoad() throws IllegalAccessException, NoSuchFieldException {
                    Field field = this.fieldReflection.onLoad();
                    return field.get(null);
                }
            }
        }

        public static class ConstructorReflection extends BaseReflection<Constructor<?>> {
            public ConstructorReflection(ReflectionBuilder reflectionBuilder) {
                super(reflectionBuilder);
            }

            protected Constructor<?> onLoad() throws NoSuchMethodException {
                Constructor<?> constructor = null;
                if (Iterables.size(this.reflectionBuilder.parameterTypes) > 1) {
                    for (Class<?>[] keyParameters : this.reflectionBuilder.parameterTypes) try {
                        constructor = this.BUILDER_CLASS.getDeclaredConstructor(keyParameters);
                    } catch (NoSuchMethodException ignored) {}
                } else constructor = (Iterables.size(this.reflectionBuilder.parameterTypes) > 0) ? this.BUILDER_CLASS.getDeclaredConstructor(Iterables.get(this.reflectionBuilder.parameterTypes, 0)) : this.BUILDER_CLASS.getDeclaredConstructor();
                if (constructor != null) constructor.setAccessible(true);
                return constructor;
            }
        }

        public static class EnumReflection extends BaseReflection<Enum<?>[]> {
            public EnumReflection(ReflectionBuilder reflectionBuilder) {
                super(reflectionBuilder);
            }

            protected Enum<?>[] onLoad() {
                Enum<?>[] arrayOfEnum = (Enum<?>[]) this.BUILDER_CLASS.getEnumConstants();
                for (Enum<?> enumConstant : arrayOfEnum) TypeCache.ClassCache.register(enumConstant.name(), enumConstant, this.BUILDER_CLASS);
                return arrayOfEnum;
            }
        }
    }
}

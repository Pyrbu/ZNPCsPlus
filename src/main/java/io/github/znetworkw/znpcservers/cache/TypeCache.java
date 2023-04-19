package io.github.znetworkw.znpcservers.cache;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
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

    class CacheBuilder {
        private final CachePackage cachePackage;

        private final CacheCategory cacheCategory;

        private final String fieldName;

        private final List<String> className;

        private final List<String> methods;

        private final String additionalData;

        private final ImmutableList<Class<?>[]> parameterTypes;

        private final Class<?> expectType;

        public CacheBuilder(CachePackage cachePackage) {
            this(cachePackage, CacheCategory.DEFAULT, new ArrayList<>(), "", new ArrayList<>(), "", ImmutableList.of(), null);
        }

        protected CacheBuilder(CachePackage cachePackage, CacheCategory cacheCategory, List<String> className, String fieldName, List<String> methods, String additionalData, ImmutableList<Class<?>[]> parameterTypes, Class<?> expectType) {
            this.cachePackage = cachePackage;
            this.cacheCategory = cacheCategory;
            this.className = className;
            this.methods = methods;
            this.fieldName = fieldName;
            this.additionalData = additionalData;
            this.parameterTypes = parameterTypes;
            this.expectType = expectType;
        }

        public CacheBuilder withCategory(CacheCategory cacheCategory) {
            return new CacheBuilder(this.cachePackage, cacheCategory, this.className, this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public CacheBuilder withClassName(String className) {
            return new CacheBuilder(this.cachePackage, this.cacheCategory, new ImmutableList.Builder<String>().addAll(this.className).add(formatClass(className)).build(), this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public CacheBuilder withClassName(Class<?> clazz) {
            return new CacheBuilder(this.cachePackage, this.cacheCategory, new ImmutableList.Builder<String>().addAll(this.className).add((clazz == null) ? "" : clazz.getName()).build(), this.fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public CacheBuilder withMethodName(String methodName) {
            return new CacheBuilder(this.cachePackage, this.cacheCategory, this.className, this.fieldName, new ImmutableList.Builder<String>().addAll(this.methods).add(methodName).build(), this.additionalData, this.parameterTypes, this.expectType);
        }

        public CacheBuilder withFieldName(String fieldName) {
            return new CacheBuilder(this.cachePackage, this.cacheCategory, this.className, fieldName, this.methods, this.additionalData, this.parameterTypes, this.expectType);
        }

        public CacheBuilder withAdditionalData(String additionalData) {
            return new CacheBuilder(this.cachePackage, this.cacheCategory, this.className, this.fieldName, this.methods, additionalData, this.parameterTypes, this.expectType);
        }

        public CacheBuilder withParameterTypes(Class<?>... types) {
            return new CacheBuilder(this.cachePackage, this.cacheCategory, this.className, this.fieldName, this.methods, this.additionalData, ImmutableList.copyOf(Iterables.concat(this.parameterTypes, ImmutableList.of(types))), this.expectType);
        }

        public CacheBuilder withExpectResult(Class<?> expectType) {
            return new CacheBuilder(this.cachePackage, this.cacheCategory, this.className, this.fieldName, this.methods, this.additionalData, this.parameterTypes, expectType);
        }

        protected String formatClass(String className) {
            return switch (this.cachePackage) {
                case MINECRAFT_SERVER, CRAFT_BUKKIT -> String.format(((this.cachePackage == CachePackage.CRAFT_BUKKIT) ? this.cachePackage.getFixedPackageName() : this.cachePackage.getForCategory(this.cacheCategory, this.additionalData)) + ".%s", className);
                case DEFAULT -> className;
            };
        }
    }

    abstract class BaseCache<T> {
        protected final TypeCache.CacheBuilder cacheBuilder;

        protected Class<?> BUILDER_CLASS;

        private T cached;

        private boolean loaded = false;

        protected BaseCache(TypeCache.CacheBuilder cacheBuilder) {
            this.cacheBuilder = cacheBuilder;
            for (String classes : cacheBuilder.className) {
                try {
                    this.BUILDER_CLASS = Class.forName(classes);
                } catch (ClassNotFoundException ignored) {
                }
            }
        }

        public T load() {
            if (this.loaded) return this.cached;
            try {
                if (this.BUILDER_CLASS == null) throw new IllegalStateException("can't find class for: " + this.cacheBuilder.className);
                T eval = (this.cached != null) ? this.cached : (this.cached = onLoad());
                if (eval == null) throw new NullPointerException();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                if (throwable instanceof IllegalStateException) log("No cache found for: " + this.cacheBuilder.className);
                log("No cache found for: " + this.cacheBuilder.className + " : " + this.cacheBuilder.methods.toString());
                log("Skipping cache for " + this.cacheBuilder.className);
            }
            this.loaded = true;
            return this.cached;
        }

        private void log(String message) {
            ZNPCsPlus.LOGGER.warning("[BaseCache] " + message);
        }

        protected abstract T onLoad() throws Exception;

        public static class ClazzLoader extends BaseCache<Class<?>> {
            public ClazzLoader(TypeCache.CacheBuilder cacheBuilder) {
                super(cacheBuilder);
            }

            protected Class<?> onLoad() {
                return this.BUILDER_CLASS;
            }
        }

        public static class MethodLoader extends BaseCache<Method> {
            public MethodLoader(TypeCache.CacheBuilder builder) {
                super(builder);
            }

            protected Method onLoad() {
                Method methodThis = null;
                List<String> methods = this.cacheBuilder.methods;
                boolean hasExpectedType = (this.cacheBuilder.expectType != null);
                if (methods.isEmpty() && hasExpectedType) for (Method method : this.BUILDER_CLASS.getDeclaredMethods()) {
                    if (method.getReturnType() == this.cacheBuilder.expectType) return method;
                }
                for (String methodName : this.cacheBuilder.methods) try {
                    Method maybeGet;
                    if (!Iterables.isEmpty(this.cacheBuilder.parameterTypes))
                        maybeGet = this.BUILDER_CLASS.getDeclaredMethod(methodName, Iterables.get(this.cacheBuilder.parameterTypes, 0));
                    else maybeGet = this.BUILDER_CLASS.getDeclaredMethod(methodName);
                    if (this.cacheBuilder.expectType != null && this.cacheBuilder.expectType != maybeGet.getReturnType()) continue;
                    maybeGet.setAccessible(true);
                    methodThis = maybeGet;
                } catch (NoSuchMethodException ignored) {}
                return methodThis;
            }
        }

        public static class FieldLoader extends BaseCache<Field> {
            public FieldLoader(TypeCache.CacheBuilder cacheBuilder) {
                super(cacheBuilder);
            }

            protected Field onLoad() throws NoSuchFieldException {
                if (this.cacheBuilder.expectType != null)
                    for (Field field1 : this.BUILDER_CLASS.getDeclaredFields()) {
                        if (field1.getType() == this.cacheBuilder.expectType) {
                            field1.setAccessible(true);
                            return field1;
                        }
                    }
                Field field = this.BUILDER_CLASS.getDeclaredField(this.cacheBuilder.fieldName);
                field.setAccessible(true);
                return field;
            }

            public AsValueField asValueField() {
                return new AsValueField(this);
            }

            private static class AsValueField extends TypeCache.BaseCache<Object> {
                private final TypeCache.BaseCache.FieldLoader fieldLoader;

                public AsValueField(TypeCache.BaseCache.FieldLoader fieldLoader) {
                    super(fieldLoader.cacheBuilder);
                    this.fieldLoader = fieldLoader;
                }

                protected Object onLoad() throws IllegalAccessException, NoSuchFieldException {
                    Field field = this.fieldLoader.onLoad();
                    return field.get(null);
                }
            }
        }

        public static class ConstructorLoader extends BaseCache<Constructor<?>> {
            public ConstructorLoader(TypeCache.CacheBuilder cacheBuilder) {
                super(cacheBuilder);
            }

            protected Constructor<?> onLoad() throws NoSuchMethodException {
                Constructor<?> constructor = null;
                if (Iterables.size(this.cacheBuilder.parameterTypes) > 1) {
                    for (Class<?>[] keyParameters : this.cacheBuilder.parameterTypes) try {
                        constructor = this.BUILDER_CLASS.getDeclaredConstructor(keyParameters);
                    } catch (NoSuchMethodException ignored) {}
                } else constructor = (Iterables.size(this.cacheBuilder.parameterTypes) > 0) ? this.BUILDER_CLASS.getDeclaredConstructor(Iterables.get(this.cacheBuilder.parameterTypes, 0)) : this.BUILDER_CLASS.getDeclaredConstructor();
                if (constructor != null) constructor.setAccessible(true);
                return constructor;
            }
        }

        public static class EnumLoader extends BaseCache<Enum<?>[]> {
            public EnumLoader(TypeCache.CacheBuilder cacheBuilder) {
                super(cacheBuilder);
            }

            protected Enum<?>[] onLoad() {
                Enum<?>[] arrayOfEnum = (Enum<?>[]) this.BUILDER_CLASS.getEnumConstants();
                for (Enum<?> enumConstant : arrayOfEnum) TypeCache.ClassCache.register(enumConstant.name(), enumConstant, this.BUILDER_CLASS);
                return arrayOfEnum;
            }
        }
    }
}

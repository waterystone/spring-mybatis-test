package com.adu.spring_test.mybatis.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adu.spring_test.mybatis.enums.EnumTrait;
import com.google.common.base.Converter;
import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public abstract class EnumTraitUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnumTraitUtil.class);

    private EnumTraitUtil() {
    }

    private static final LoadingCache<Class, Converter<Integer, ? extends EnumTrait>> enumCodeConverterMap = CacheBuilder
            .newBuilder().build(new CacheLoader<Class, Converter<Integer, ? extends EnumTrait>>() {
                @Override
                public Converter<Integer, ? extends EnumTrait> load(final Class clazz) throws Exception {
                    return new EnumTraitCodeConverter(clazz);
                }
            });

    public static <T extends EnumTrait> T codeOf(final Class<T> clazz, int code) {
        try {
            return codeConverter(clazz).convert(code);
        } catch (ExecutionException e) {
            LOGGER.error("enum to code fail", e);
        }
        return null;
    }

    private static <T extends EnumTrait> Converter<Integer, T> codeConverter(final Class<T> clazz)
            throws ExecutionException {
        Converter<Integer, T> integerConverter = (Converter<Integer, T>) enumCodeConverterMap.get(clazz);
        return integerConverter;
    }

    private static final class EnumTraitCodeConverter<T extends EnumTrait> extends Converter<Integer, T>
            implements Serializable {

        private final Class<T> enumClass;

        ImmutableMap<Integer, T> codeToEnumMap;

        EnumTraitCodeConverter(Class<T> enumClass) {
            this.enumClass = checkNotNull(enumClass);
            codeToEnumMap = Maps.uniqueIndex(ImmutableList.copyOf(enumClass.getEnumConstants()),
                    new Function<T, Integer>() {
                        @Nullable
                        @Override
                        public Integer apply(T input) {
                            return input.getCode();
                        }
                    });
        }

        @Override
        protected T doForward(Integer value) {
            return codeToEnumMap.get(value);
        }

        @Override
        protected Integer doBackward(T enumValue) {
            return enumValue.getCode();
        }

        @Override
        public boolean equals(@Nullable Object object) {
            if (object instanceof EnumTraitCodeConverter) {
                EnumTraitCodeConverter that = (EnumTraitCodeConverter) object;
                return this.enumClass.equals(that.enumClass);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return enumClass.hashCode();
        }

        @Override
        public String toString() {
            return "EnumTraitUtil.EnumTraitCodeConverter(" + enumClass.getName() + ".class)";
        }

        private static final long serialVersionUID = 0L;
    }
}

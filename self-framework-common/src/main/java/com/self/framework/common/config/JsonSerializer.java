package com.self.framework.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * json序列化
 *
 * @author wenbo.zhuang
 * @date 2022/06/16 17:52
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonSerializer {

    // 懒加载
    private static class Lazy {
        private static final ObjectMapper MAPPER = new ObjectMapper();
        private static final ObjectReader READER;
        private static final ObjectWriter WRITER;
        static {
            // 缓存
            MAPPER.registerModule(new AfterburnerModule());
//            // 序列化时不转成时间戳
//            MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            // 日期格式
            MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            // 支持fastjson的JSONField注解
            MAPPER.setAnnotationIntrospector(new EnhanceAnnotationIntrospector());
            // 未知字段不报错
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // null值不输出
            MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            //如果是空对象的时候,不抛异常
            MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            READER = MAPPER.reader();
            WRITER = MAPPER.writer();
        }
    }

    /**
     * 泛型转JavaType
     * @param valueType 值的泛型类型
     * @return JavaType
     */
    public static JavaType toJavaType(Type valueType) {
        return Lazy.MAPPER.constructType(valueType);
    }

    @SneakyThrows
    public static <T> T deserialize(File src, Class<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(File src, TypeReference<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(File src, Type valueType) {
        return Lazy.READER.forType(toJavaType(valueType)).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(File src, JavaType valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(URL src, Class<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(URL src, TypeReference<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(URL src, JavaType valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(URL src, Type valueType) {
        return Lazy.READER.forType(toJavaType(valueType)).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(String src, Class<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(String src, TypeReference<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(String src, JavaType valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(String src, Type valueType) {
        return Lazy.READER.forType(toJavaType(valueType)).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(Reader src, Class<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(Reader src, TypeReference<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(Reader src, JavaType valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(Reader src, Type valueType) {
        return Lazy.READER.forType(toJavaType(valueType)).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(InputStream src, Class<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(InputStream src, TypeReference<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(InputStream src, JavaType valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(InputStream src, Type valueType) {
        return Lazy.READER.forType(toJavaType(valueType)).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] src, Class<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] src, int offset, int len, Class<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src, offset, len);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] src, TypeReference<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] src, int offset, int len, TypeReference<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src, offset, len);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] src, JavaType valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] src, Type valueType) {
        return Lazy.READER.forType(toJavaType(valueType)).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] src, int offset, int len, JavaType valueType) {
        return Lazy.READER.forType(valueType).readValue(src, offset, len);
    }

    @SneakyThrows
    public static <T> T deserialize(byte[] src, int offset, int len, Type valueType) {
        return Lazy.READER.forType(toJavaType(valueType)).readValue(src, offset, len);
    }

    @SneakyThrows
    public static <T> T deserialize(DataInput src, Class<T> valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(DataInput src, JavaType valueType) {
        return Lazy.READER.forType(valueType).readValue(src);
    }

    @SneakyThrows
    public static <T> T deserialize(DataInput src, Type valueType) {
        return Lazy.READER.forType(toJavaType(valueType)).readValue(src);
    }

    @SneakyThrows
    public static void serialize(File resultFile, Object value) {
        Lazy.WRITER.writeValue(resultFile, value);
    }

    @SneakyThrows
    public static void serialize(OutputStream out, Object value) {
        Lazy.WRITER.writeValue(out, value);
    }

    @SneakyThrows
    public static void serialize(DataOutput out, Object value) {
        Lazy.WRITER.writeValue(out, value);
    }

    @SneakyThrows
    public static void serialize(Writer w, Object value) {
        Lazy.WRITER.writeValue(w, value);
    }

    @SneakyThrows
    public static String serializeAsString(Object value) {
        return Lazy.WRITER.writeValueAsString(value);
    }

    @SneakyThrows
    public static byte[] serializeAsBytes(Object value) {
        return Lazy.WRITER.writeValueAsBytes(value);
    }

    @SneakyThrows
    public static <T> T convertValue(Object fromValue, Class<T> type) {
        return Lazy.MAPPER.convertValue(fromValue, type);
    }
    @SneakyThrows
    public static <T> T convertValue(Object fromValue, TypeReference<T> type) {
        return Lazy.MAPPER.convertValue(fromValue, type);
    }
    @SneakyThrows
    public static <T> T convertValue(Object fromValue, JavaType type) {
        return Lazy.MAPPER.convertValue(fromValue, type);
    }
}

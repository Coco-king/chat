package top.codecrab.chat.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * @author codecrab
 * @date 2021年03月30日 14:54
 */
@Slf4j
@SuppressWarnings({"unchecked"})
public class JSONUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss:SSS";

    /*

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("GMT+8");

    static class NativeLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

        public NativeLocalDateTimeSerializer() {
            super(LocalDateTime.class);
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
            if (value != null) {
                final long mills = value.atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
                g.writeNumber(mills);
            } else {
                g.writeNull();
            }
        }
    }

    static class NativeLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

        public NativeLocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }

        @Override
        public LocalDateTime deserialize(final JsonParser parser,
                                         final DeserializationContext context) throws IOException {
            final long value = parser.getValueAsLong();
            return Instant.ofEpochMilli(value).atZone(DEFAULT_ZONE_ID).toLocalDateTime();
        }
    }

    static class NativeLocalDateSerializer extends StdSerializer<LocalDate> {

        public NativeLocalDateSerializer() {
            super(LocalDate.class);
        }

        @Override
        public void serialize(LocalDate value, JsonGenerator g, SerializerProvider provider) throws IOException {
            if (value != null) {
                final long mills = value.atStartOfDay(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
                g.writeNumber(mills);
            } else {
                g.writeNull();
            }
        }
    }

    static class NativeLocalDateDeserializer extends StdDeserializer<LocalDate> {

        public NativeLocalDateDeserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalDate deserialize(final JsonParser parser,
                                     final DeserializationContext context) throws IOException {
            final long value = parser.getValueAsLong();
            return Instant.ofEpochMilli(value).atZone(DEFAULT_ZONE_ID).toLocalDate();
        }
    }

    static class NativeLocalTimeSerializer extends StdSerializer<LocalTime> {

        public NativeLocalTimeSerializer() {
            super(LocalTime.class);
        }

        @Override
        public void serialize(LocalTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
            if (value != null) {
                final long mills = value.atDate(LocalDate.now()).atZone(DEFAULT_ZONE_ID).toInstant().toEpochMilli();
                g.writeNumber(mills);
            } else {
                g.writeNull();
            }
        }
    }

    static class NativeLocalTimeDeserializer extends StdDeserializer<LocalTime> {

        public NativeLocalTimeDeserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalTime deserialize(final JsonParser parser,
                                     final DeserializationContext context) throws IOException {
            final long value = parser.getValueAsLong();
            return Instant.ofEpochMilli(value).atZone(DEFAULT_ZONE_ID).toLocalTime();
        }
    }

    */

    static {
        //设置java.util.Date时间类的序列化以及反序列化的格式
        objectMapper.setDateFormat(new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_SSS));

        // 初始化JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        //处理LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS_SSS);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        //处理LocalDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        //处理LocalTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        /*

        //处理LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS_SSS);
        javaTimeModule.addSerializer(LocalDateTime.class, new NativeLocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new NativeLocalDateTimeDeserializer());

        //处理LocalDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        javaTimeModule.addSerializer(LocalDate.class, new NativeLocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new NativeLocalDateDeserializer());

        //处理LocalTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        javaTimeModule.addSerializer(LocalTime.class, new NativeLocalTimeSerializer());
        javaTimeModule.addDeserializer(LocalTime.class, new NativeLocalTimeDeserializer());

        */

        //注册时间模块, 支持支持jsr310, 即新的时间类(java.time包下的时间类)
        objectMapper.registerModule(javaTimeModule);

        // 包含所有字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 在序列化一个空对象时时不抛出异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // 忽略反序列化时在json字符串中存在, 但在java对象中不存在的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 转换为JSON字符串
     *
     * @param object 被转为JSON的对象
     * @param <T>    Bean对象
     * @return JSON字符串
     */
    public static <T> String toJsonStr(T object) {
        if (object == null) {
            return null;
        }
        try {
            return object instanceof String ? (String) object : objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            log.warn("Parse object to string error", e);
            return null;
        }
    }

    /**
     * 转换为格式化后的JSON字符串
     *
     * @param object 被转为JSON的对象
     * @param <T>    Bean对象
     * @return 格式化后的JSON字符串
     */
    public static <T> String toJsonPrettyStr(T object) {
        if (object == null) {
            return null;
        }
        try {
            return object instanceof String ? (String) object : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            log.warn("Parse object to string error", e);
            return null;
        }
    }

    /**
     * JSON字符串转为实体类对象，转换异常将被抛出
     *
     * @param <T>     Bean类型
     * @param jsonStr JSON字符串
     * @param clazz   实体类对象
     * @return 实体类对象
     */
    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonStr) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) jsonStr : objectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            log.warn("Parse String to object error!", e);
            return null;
        }
    }

    /**
     * JSON字符串转为实体类对象，转换异常将被抛出
     *
     * @param <T>           Bean类型
     * @param jsonStr       JSON字符串
     * @param typeReference {@link TypeReference}类型参考子类，可以获取其泛型参数中的Type类型
     * @return 实体类对象
     */
    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(jsonStr) || typeReference == null) {
            return null;
        }
        try {
            return typeReference.getType().equals(String.class) ? (T) jsonStr : objectMapper.readValue(jsonStr, typeReference);
        } catch (Exception e) {
            log.warn("Parse String to object error!", e);
            return null;
        }
    }

    /**
     * 将JSONArray字符串转换为Bean的List，默认为ArrayList
     *
     * @param <T>          Bean类型
     * @param jsonArray    JSONArray字符串
     * @param elementClass List中元素类型
     * @return List
     */
    public static <T> T toList(String jsonArray, Class<?> elementClass) {
        return toList(jsonArray, ArrayList.class, elementClass);
    }

    /**
     * 将JSONArray字符串转换为Bean的List，默认为ArrayList
     *
     * @param <T>             Bean类型
     * @param jsonArray       JSONArray字符串
     * @param collectionClass List类型
     * @param elementClass    List中元素类型
     * @return List
     */
    public static <T> T toList(String jsonArray, Class<?> collectionClass, Class<?> elementClass) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        try {
            return objectMapper.readValue(jsonArray, javaType);
        } catch (Exception e) {
            log.warn("Parse String to object error!", e);
            return null;
        }
    }


    /**
     * 将JSONArray字符串转换为Bean的List，默认为ArrayList
     *
     * @param <T>             Bean类型
     * @param jsonArray       JSONArray字符串
     * @param collectionClass List类型
     * @param elementClass    List中元素类型
     * @return List
     */
    public static <T> T toList(String jsonArray, Class<?> collectionClass, Class<?>... elementClass) {
        if (collectionClass == null) collectionClass = ArrayList.class;

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        try {
            return objectMapper.readValue(jsonArray, javaType);
        } catch (Exception e) {
            log.warn("Parse String to object error!", e);
            return null;
        }
    }
}

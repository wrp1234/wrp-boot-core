package com.wrp.boot.core.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期序列化配置
 * @author wrp
 * @since 2024年12月17日 14:18
 **/
public class DateConfig {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        //日期转字符串
        return builder -> builder.serializerByType(LocalDate.class,
                        new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
                .serializerByType(LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .serializerByType(LocalTime.class,
                        new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME))
                //字符串转日期
                .deserializerByType(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                .deserializerByType(LocalDate.class,
                        new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE))
                .deserializerByType(LocalTime.class,
                        new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
    }
}

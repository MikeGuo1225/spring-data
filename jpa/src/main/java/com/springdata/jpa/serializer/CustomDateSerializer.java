package com.springdata.jpa.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author : Donald
 * @date : 2018/10/18 11:37.
 * @description : Custom Jackson serializer for displaying Util Date objects.
 */
@Slf4j
public class CustomDateSerializer implements JsonSerializer<Date> {
    @Override
    public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context) {
        try {
            if (Date.class.getName().equals(typeOfT.getTypeName())) {
                return new JsonPrimitive(date.getTime());
            } else {
                log.warn("[CustomDateSerializer.serialize] Error : don't support this class :{}", typeOfT.getTypeName());
                return null;
            }
        } catch (final Exception e) {
            log.error("[CustomDateSerializer.serialize] Error : {}", e);
            return null;
        }
    }
    
}

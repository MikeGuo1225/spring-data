package com.springdata.jpa.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author : Donald
 * @date : 2018/10/18 11:37.
 * @description : Custom Jackson deserializer for displaying Util Date objects.
 */
@Slf4j
public class CustomDateDeserializer implements JsonDeserializer<Date> {
    
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (Date.class.getName().equals(typeOfT.getTypeName())) {
                return new Date(Long.valueOf(json.getAsString()));
            } else {
                log.warn("[CustomDateDeserializer.deserialize] Error : don't support this class :{}", typeOfT.getTypeName());
                return null;
            }
        } catch (final Exception e) {
            log.error("[CustomDateDeserializer.deserialize] Error : {}", e);
            return null;
        }
    }
}

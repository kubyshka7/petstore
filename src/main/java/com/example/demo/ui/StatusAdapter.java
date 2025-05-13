package com.example.demo.ui;

import com.google.gson.*;
import com.example.demo.orders.Status;

import java.lang.reflect.Type;

public class StatusAdapter implements JsonSerializer<Status>, JsonDeserializer<Status> {
    @Override
    public JsonElement serialize(Status status, Type typeOfSrc, JsonSerializationContext context){
        return new JsonPrimitive(status.name());
    }

    @Override
    public Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Status.valueOf(json.getAsString().toUpperCase());
        } catch(IllegalArgumentException e) {
            throw new JsonParseException("Uknown status: " + json.getAsString());
        }
    }
}

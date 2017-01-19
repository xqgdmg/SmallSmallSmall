package com.kollway.update.api.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by liangyaotian on 1/11/16.
 */
public class BooleanAdapter extends TypeAdapter<Boolean> {

    @Override
    public Boolean read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return false;
        }
        String value = in.nextString();
        return "1".equals(value);
    }

    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null){
            value = false;
        }
        out.value(value ? "1" : "0");
    }
}

package com.dlz.framework.ssme.util.xml.mapper;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class OrderIdSerializer extends JsonSerializer<Long> {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
	
    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        jgen.writeString(String.valueOf(value));
    }
    
}
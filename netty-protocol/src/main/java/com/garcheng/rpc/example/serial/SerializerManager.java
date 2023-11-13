package com.garcheng.rpc.example.serial;

import com.garcheng.rpc.example.constant.SerialType;

import java.util.concurrent.ConcurrentHashMap;

public class SerializerManager {

    private final static ConcurrentHashMap<Byte, Serializer> serializer = new ConcurrentHashMap<>();

    static {
        Serializer javaSerializer = new JavaSerializer();
        Serializer jsonSerializer = new JsonSerializer();
        serializer.put(javaSerializer.getType(), javaSerializer);
        serializer.put(jsonSerializer.getType(), jsonSerializer);
    }

    public static Serializer getSerializer(byte key) {
        Serializer iserializer = SerializerManager.serializer.get(key);
        if (iserializer == null) {
            return serializer.get(SerialType.JAVA_SERIAL.code());
        }
        return iserializer;

    }


}

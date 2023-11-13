package com.garcheng.rpc.example.serial;

import com.alibaba.fastjson.JSON;
import com.garcheng.rpc.example.constant.SerialType;

public class JsonSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }

    @Override
    public byte getType() {
        return SerialType.JSON_SERIAL.code();
    }
}

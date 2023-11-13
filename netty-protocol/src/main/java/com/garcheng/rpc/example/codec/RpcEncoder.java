package com.garcheng.rpc.example.codec;

import com.garcheng.rpc.example.core.Header;
import com.garcheng.rpc.example.core.RpcProtocol;
import com.garcheng.rpc.example.serial.Serializer;
import com.garcheng.rpc.example.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf out) throws Exception {
        log.info("--------begin rpc encoder--------");
        Header header = msg.getHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getSerialType());
        out.writeByte(header.getReqType());
        out.writeLong(header.getRequestId());
        Object content = msg.getContent();
        Serializer serializer = SerializerManager.getSerializer(header.getSerialType());
        byte[] data = serializer.serialize(content);
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}

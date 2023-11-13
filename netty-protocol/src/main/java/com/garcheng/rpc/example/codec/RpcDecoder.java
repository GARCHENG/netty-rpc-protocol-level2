package com.garcheng.rpc.example.codec;

import com.garcheng.rpc.example.constant.ReqType;
import com.garcheng.rpc.example.core.Header;
import com.garcheng.rpc.example.core.RpcProtocol;
import com.garcheng.rpc.example.core.RpcRequest;
import com.garcheng.rpc.example.core.RpcResponse;
import com.garcheng.rpc.example.serial.Serializer;
import com.garcheng.rpc.example.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("--------begin rpc decoder--------");
        short magic = in.readShort();
        byte serialType = in.readByte();
        byte reqType = in.readByte();
        long requestId = in.readLong();
        int length = in.readInt();
        Header header = new Header(magic, serialType, reqType, requestId, length);
        if (in.readableBytes() < length){
            in.resetReaderIndex();
            return;
        }
        byte [] data = new byte[length];
        in.readBytes(data);
        Serializer serializer = SerializerManager.getSerializer(serialType);
        ReqType rt = ReqType.findByCode(reqType);
        switch (rt){
            case REQUEST:
                RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
                requestRpcProtocol.setHeader(header);
                RpcRequest rpcRequest = serializer.deserialize(data, RpcRequest.class);
                requestRpcProtocol.setContent(rpcRequest);
                out.add(requestRpcProtocol);
                break;
            case RESPONSE:
                RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
                responseRpcProtocol.setHeader(header);
                RpcResponse rpcResponse = serializer.deserialize(data, RpcResponse.class);
                responseRpcProtocol.setContent(rpcResponse);
                out.add(responseRpcProtocol);
                break;
            case HEARTBEAT:
                break;
            default:
                break;
        }
    }
}

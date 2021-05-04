package org.grpc.server;

import io.grpc.stub.StreamObserver;
import org.grpc.guru.proto.models.TransferRequest;
import org.grpc.guru.proto.models.TransferResponse;
import org.grpc.guru.proto.models.TransferServiceGrpc;

public class TransferService  extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}

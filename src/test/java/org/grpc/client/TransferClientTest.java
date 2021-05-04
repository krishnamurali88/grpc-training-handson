package org.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.grpc.guru.proto.models.BankServiceGrpc;
import org.grpc.guru.proto.models.TransferRequest;
import org.grpc.guru.proto.models.TransferServiceGrpc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {

    private TransferServiceGrpc.TransferServiceStub stub;

    @BeforeAll
    public void setUp() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        this.stub = TransferServiceGrpc.newStub(channel);
    }

    @Test
    public void transfer() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        TransferStreamingResponse response = new TransferStreamingResponse(latch);

        StreamObserver<TransferRequest> transaferRequestStreamObserver = this.stub.transfer(response);


        for (int i = 0; i < 100; i++) {
            TransferRequest transferRequest = TransferRequest.newBuilder()
                    .setFromAccount(ThreadLocalRandom.current().nextInt(1, 11))
                    .setToAccount(ThreadLocalRandom.current().nextInt(1, 11))
                    .setAmount(ThreadLocalRandom.current().nextInt(1, 21))
                    .build();

            transaferRequestStreamObserver.onNext(transferRequest);
        }
        transaferRequestStreamObserver.onCompleted();
        latch.await();
    }
}

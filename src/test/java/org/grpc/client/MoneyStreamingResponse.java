package org.grpc.client;

import io.grpc.stub.StreamObserver;
import org.grpc.guru.proto.models.Money;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {

    private CountDownLatch latch;

    public MoneyStreamingResponse() {
    }

    public MoneyStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println("Received " + money.getValue());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Server is done !!");
        latch.countDown();
    }
}

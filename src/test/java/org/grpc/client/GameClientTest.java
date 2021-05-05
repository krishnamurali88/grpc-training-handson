package org.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.grpc.snake.ladder.proto.models.Die;
import org.grpc.snake.ladder.proto.models.GameServiceGrpc;
import org.grpc.snake.ladder.proto.models.GameState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameClientTest {

    private GameServiceGrpc.GameServiceStub stub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.stub = GameServiceGrpc.newStub(channel);
    }

    @Test
    public void clientGame() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        GameStateStreamingResponse gameStateStreamingResponse = new GameStateStreamingResponse(latch);
        StreamObserver<Die> dieStateStreamObserver = this.stub.roll(gameStateStreamingResponse);
        gameStateStreamingResponse.setDieStreamObserver(dieStateStreamObserver);
        gameStateStreamingResponse.roll();
        latch.await();

    }
}


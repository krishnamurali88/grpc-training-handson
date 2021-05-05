package org.grpc.snakeladder;

import io.grpc.stub.StreamObserver;
import org.grpc.snake.ladder.proto.models.Die;
import org.grpc.snake.ladder.proto.models.GameServiceGrpc;
import org.grpc.snake.ladder.proto.models.GameState;
import org.grpc.snake.ladder.proto.models.Player;

public class GameService extends GameServiceGrpc.GameServiceImplBase {

    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
        Player client = Player.newBuilder()
                .setName("Client")
                .setPosition(0)
                .build();

        Player server = Player.newBuilder()
                .setName("Server")
                .setPosition(0)
                .build();

        return new DieStreamingRequest(responseObserver, client, server);
    }
}

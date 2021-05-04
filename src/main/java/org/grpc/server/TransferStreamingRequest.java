package org.grpc.server;

import io.grpc.stub.StreamObserver;
import org.grpc.guru.proto.models.Account;
import org.grpc.guru.proto.models.TransferRequest;
import org.grpc.guru.proto.models.TransferResponse;
import org.grpc.guru.proto.models.TransferStatus;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> transferResponseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transaferRequest) {
        int fromAccount = transaferRequest.getFromAccount();
        int toAccount = transaferRequest.getToAccount();
        int transferAmount = transaferRequest.getAmount();
        int balance = AccountDatabase.getBalance(fromAccount);

        TransferStatus status = TransferStatus.FAILED;

        if (balance >= transferAmount && fromAccount != toAccount) {
            AccountDatabase.deductBalance(fromAccount, transferAmount);
            AccountDatabase.addBalance(toAccount, transferAmount);
            status = TransferStatus.SUCCESS;
        }
        Account fromAccountInfo = Account.newBuilder().setAccountNumber(fromAccount).setAmount(AccountDatabase.getBalance(fromAccount)).build();
        Account toAccountInfo = Account.newBuilder().setAccountNumber(toAccount).setAmount(AccountDatabase.getBalance(toAccount)).build();
        TransferResponse transferResponse = TransferResponse.newBuilder()
                .setStatus(status)
                .addAccounts(fromAccountInfo)
                .addAccounts(toAccountInfo)
                .build();
        this.transferResponseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        AccountDatabase.printAccountDetails();
        this.transferResponseStreamObserver.onCompleted();
    }
}

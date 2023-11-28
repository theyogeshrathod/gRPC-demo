package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.StoreLaptopRequest;
import proto.StoreLaptopResponse;
import proto.Laptop;
import proto.LaptopServiceGrpc;
import util.Generator;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaptopClient {
    private static final Logger logger = Logger.getLogger(LaptopClient.class.getName());

    private final ManagedChannel channel;
    private final LaptopServiceGrpc.LaptopServiceBlockingStub blockingStub;

    public LaptopClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        blockingStub = LaptopServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void storeLaptop(Laptop laptop) {
        StoreLaptopRequest request = StoreLaptopRequest.newBuilder().setLaptop(laptop).build();
        StoreLaptopResponse response;

        try {
            response = blockingStub.storeLaptop(request);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Create laptop request failed: " + e.getMessage());
            return;
        }

        logger.info("Laptop created with id: " + response.getId());
    }

    public static void main(String[] args) throws InterruptedException {
        LaptopClient client = new LaptopClient("127.0.0.1", 1234);
        Laptop laptop = new Generator().newLaptop();

        try {
            client.storeLaptop(laptop);
        } finally {
            client.shutdown();
        }
    }
}

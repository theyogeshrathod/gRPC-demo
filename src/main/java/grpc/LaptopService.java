package grpc;

import io.grpc.stub.StreamObserver;
import proto.StoreLaptopRequest;
import proto.StoreLaptopResponse;
import proto.Laptop;
import proto.LaptopServiceGrpc;

import java.util.UUID;
import java.util.logging.Logger;

public class LaptopService extends LaptopServiceGrpc.LaptopServiceImplBase {

    private static final Logger logger = Logger.getLogger(LaptopService.class.getName());

    private final LaptopStore store;

    public LaptopService(LaptopStore store) {
        this.store = store;
    }

    @Override
    public void storeLaptop(StoreLaptopRequest request, StreamObserver<StoreLaptopResponse> responseObserver) {
        Laptop laptop = request.getLaptop();

        String id = laptop.getId();
        logger.info("Create laptop request with id: " + id);

        UUID uuid;
        if (id.isEmpty()) {
            uuid = UUID.randomUUID();
        } else {
            try {
                uuid = UUID.fromString(id);
            } catch (Exception e) {
                responseObserver.onError(e);
                return;
            }
        }

        Laptop other = laptop.toBuilder().setId(uuid.toString()).build();
        try {
            store.save(other);
        } catch (Exception e) {
            responseObserver.onError(e);
            return;
        }

        StoreLaptopResponse response = StoreLaptopResponse.newBuilder().setId(other.getId()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        logger.info("Laptop successfully created with Id: " + other.getId());
    }
}

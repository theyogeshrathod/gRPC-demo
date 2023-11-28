package grpc;

import proto.Laptop;

public interface LaptopStore {
    void save(Laptop laptop) throws Exception;
    Laptop find(String id);
}

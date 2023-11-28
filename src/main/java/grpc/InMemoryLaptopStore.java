package grpc;

import proto.Laptop;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryLaptopStore implements LaptopStore {

    private final ConcurrentMap<String, Laptop> data;

    public InMemoryLaptopStore() {
        data = new ConcurrentHashMap<>();
    }
    @Override
    public void save(Laptop laptop) throws Exception {
        if (data.containsKey(laptop.getId())) {
            throw new Exception("Laptop already present");
        }

        Laptop other = laptop.toBuilder().build();
        data.put(other.getId(), other);
    }

    @Override
    public Laptop find(String id) {
        if (!data.containsKey(id)) {
            return null;
        }

         return data.get(id).toBuilder().build();
    }
}

package util;

import com.google.protobuf.Timestamp;
import proto.*;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class Generator {

    private Random rand;

    public Generator() {
        rand = new Random();
    }

    public Keyboard newKeyboard() {
        return Keyboard.newBuilder().setLayout(randomKeyboardLayout()).setBacklit(rand.nextBoolean()).build();
    }

    public CPU newCPU() {
        String brand = randomCPUBrand();
        String name = randomCPUName(brand);
        int numberOfCores = rand.nextInt(2, 8);
        int numberOfThreads = rand.nextInt(numberOfCores, 12);
        double minGhz = randomDouble(2.0, 3.5);
        double maxGhz = randomDouble(minGhz, 5.0);

        return CPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setNumberOfCores(numberOfCores)
                .setNumberOfThreads(numberOfThreads)
                .setMinGhz(minGhz)
                .setMaxGhz(maxGhz)
                .build();
    }

    public GPU newGPU() {
        String brand = randomCPUBrand();
        String name = randomGPUName(brand);
        double minGhz = randomDouble(1.0, 1.5);
        double maxGhz = randomDouble(minGhz, 2.0);

        Memory memory = Memory.newBuilder()
                .setValue(randomInt(2, 6))
                .setUnit(Memory.Unit.GIGABYTE).build();

        return GPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setMinGhz(minGhz)
                .setMaxGhz(maxGhz)
                .setMemory(memory)
                .build();
    }

    public Memory newRAM() {
        return Memory.newBuilder()
                .setValue(randomInt(4, 64))
                .setUnit(Memory.Unit.GIGABYTE).build();
    }

    public Storage newHDD() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(1, 6))
                .setUnit(Memory.Unit.TERABYTE).build();

        return Storage.newBuilder()
                .setMemory(memory)
                .setDriver(Storage.Driver.HDD)
                .build();
    }

    public Storage newSSD() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(128, 1024))
                .setUnit(Memory.Unit.GIGABYTE).build();

        return Storage.newBuilder()
                .setMemory(memory)
                .setDriver(Storage.Driver.SSD)
                .build();
    }

    public Screen newScreen() {
        int height = randomInt(1024, 4320);
        int width = height * 16 / 9;

        Screen.Resolution resolution = Screen.Resolution.newBuilder()
                .setHeight(height)
                .setWidth(width)
                .build();

        return Screen.newBuilder()
                .setResolution(resolution)
                .setSizeInch(randomFloat(13F, 17F))
                .setPanel(randomScreenPanel())
                .setMultitouch(rand.nextBoolean())
                .build();
    }

    public Laptop newLaptop() {
        String brand = randomLaptopBrand();
        String name = randomLaptopName(brand);

        double weight = randomDouble(1.0, 3.0);
        double priceUsd = randomDouble(1500, 3500);

        int releaseYear = randomInt(2015, 2023);

        return Laptop.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setBrand(brand)
                .setName(name)
                .setWeightKg(weight)
                .setPriceUsd(priceUsd)
                .setReleasedYear(releaseYear)
                .addStorages(newSSD())
                .addStorages(newHDD())
                .setScreen(newScreen())
                .setKeyboard(newKeyboard())
                .setUpdatedAt(nowTimestamp())
                .build();
    }

    private Timestamp nowTimestamp() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();

    }

    private String randomLaptopName(String brand) {
        return switch (brand) {
            case "Apple" -> randomStringFromSet("MacBook Air", "MacBook Pro", "MacBook Pro M2", "MacBook Pro M3");
            case "Dell" -> randomStringFromSet("Latitude", "Inspiron", "Vostro");
            default -> randomStringFromSet("Thinkpad", "Vivobook", "ChromeBook");
        };

    }

    private String randomLaptopBrand() {
        return randomStringFromSet("Apple", "Dell", "HP", "Lenovo");
    }

        private int randomInt(int min, int max) {
        return min + rand.nextInt() * (max - min);
    }

    private double randomDouble(double min, double max) {
        return min + rand.nextDouble() * (max - min);
    }

    private float randomFloat(float min, float max) {
        return min + rand.nextFloat() * (max - min);
    }

    private Screen.Panel randomScreenPanel() {
        if (rand.nextBoolean()) {
            return Screen.Panel.IPS;
        }
        return Screen.Panel.OLED;
    }

    private String randomCPUName(String brand) {
        return randomStringFromSet("Core i9", "Core i5", "Core i7", "Core i3");
    }

    private String randomGPUName(String brand) {
        return randomStringFromSet("RTX 2060", "RTX 2070", "GTX 1060", "GTX 1070");
    }

    private String randomCPUBrand() {
        return randomStringFromSet("Intel", "AMD");
    }

    private String randomStringFromSet(String... arr) {
        int n = arr.length;
        if (n == 0) {
            return "";
        }

        return arr[rand.nextInt(n)];
    }

    private Keyboard.Layout randomKeyboardLayout() {
        return switch (rand.nextInt(3)) {
            case 1 -> Keyboard.Layout.QWERTY;
            case 2 -> Keyboard.Layout.QWERTZ;
            default -> Keyboard.Layout.AZERTY;
        };
    }
}

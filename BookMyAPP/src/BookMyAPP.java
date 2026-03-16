import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;
    protected int available;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight, int available) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
        System.out.println("Available: " + available);
        System.out.println();
    }

    public int getAvailable() {
        return available;
    }

    public String getRoomType() {
        return this.getClass().getSimpleName();
    }
}

// Subclasses
class SingleRoom extends Room {
    public SingleRoom(int available) { super(1, 250, 1500.0, available); }
}

class DoubleRoom extends Room {
    public DoubleRoom(int available) { super(2, 400, 2500.0, available); }
}

class SuiteRoom extends Room {
    public SuiteRoom(int available) { super(3, 750, 5000.0, available); }
}

// Inventory class
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        // Default availability counts
        roomAvailability.put("SingleRoom", 5);
        roomAvailability.put("DoubleRoom", 3);
        roomAvailability.put("SuiteRoom", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}

// Main class
public class BookMyAPP {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();

        Room singleRoom = new SingleRoom(inventory.getRoomAvailability().get("SingleRoom"));
        Room doubleRoom = new DoubleRoom(inventory.getRoomAvailability().get("DoubleRoom"));
        Room suiteRoom = new SuiteRoom(inventory.getRoomAvailability().get("SuiteRoom"));

        System.out.println("Hotel Room Initialization with Inventory\n");

        System.out.println("Single Room:");
        singleRoom.displayRoomDetails();

        System.out.println("Double Room:");
        doubleRoom.displayRoomDetails();

        System.out.println("Suite Room:");
        suiteRoom.displayRoomDetails();

        // Update inventory example
        inventory.updateAvailability("SingleRoom", 4);
        System.out.println("Updated SingleRoom availability: " + inventory.getRoomAvailability().get("SingleRoom"));
    }
}
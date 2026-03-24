import java.util.*;

// Abstract Room class
abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails(int available) {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: ₹" + pricePerNight);
        System.out.println("Available: " + available);
    }

    public String getRoomType() {
        return this.getClass().getSimpleName();
    }
}

// Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 250, 1500.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 400, 2500.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 750, 5000.0);
    }
}

// Inventory (State Holder)
class RoomInventory {
    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        roomAvailability.put("SingleRoom", 5);
        roomAvailability.put("DoubleRoom", 3);
        roomAvailability.put("SuiteRoom", 0); // Example: unavailable
    }

    // Read-only access
    public Map<String, Integer> getRoomAvailability() {
        return Collections.unmodifiableMap(roomAvailability);
    }
}

// Search Service (Read-only logic)
class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory) {
        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("Available Rooms:\n");

        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            String roomType = entry.getKey();
            int count = entry.getValue();

            // Filter unavailable rooms
            if (count <= 0) continue;

            Room room = createRoom(roomType);
            if (room != null) {
                room.displayRoomDetails(count);
            }
        }
    }

    // Factory method to create room objects
    private Room createRoom(String roomType) {
        switch (roomType) {
            case "SingleRoom":
                return new SingleRoom();
            case "DoubleRoom":
                return new DoubleRoom();
            case "SuiteRoom":
                return new SuiteRoom();
            default:
                return null;
        }
    }
}

// Main Class
public class BookMyAPP {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        RoomSearchService searchService = new RoomSearchService();

        System.out.println("=== Hotel Room Search ===\n");

        // Perform search (read-only operation)
        searchService.searchAvailableRooms(inventory);

        System.out.println("\nSearch completed. Inventory remains unchanged.");
    }
}
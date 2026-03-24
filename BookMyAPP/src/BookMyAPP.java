import java.util.*;

// Reservation (from UC5)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// Booking Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // removes from queue (FIFO)
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("SingleRoom", 2);
        availability.put("DoubleRoom", 1);
        availability.put("SuiteRoom", 1);
    }

    public int getAvailable(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        availability.put(roomType, availability.get(roomType) - 1);
    }

    public void display() {
        System.out.println("\nCurrent Inventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> " + availability.get(type));
        }
    }
}

// Booking Service (CORE LOGIC)
class BookingService {

    // Map: RoomType -> Set of Room IDs
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public BookingService() {
        allocatedRooms.put("SingleRoom", new HashSet<>());
        allocatedRooms.put("DoubleRoom", new HashSet<>());
        allocatedRooms.put("SuiteRoom", new HashSet<>());
    }

    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        System.out.println("\n=== Processing Bookings ===");

        while (!queue.isEmpty()) {
            Reservation request = queue.getNextRequest();

            String roomType = request.getRoomType();
            String guest = request.getGuestName();

            System.out.println("\nProcessing request for " + guest);

            // Step 1: Check availability
            if (inventory.getAvailable(roomType) <= 0) {
                System.out.println("❌ No rooms available for " + roomType);
                continue;
            }

            // Step 2: Generate unique Room ID
            String roomId = generateRoomId(roomType);

            // Step 3: Ensure uniqueness using Set
            Set<String> assignedSet = allocatedRooms.get(roomType);

            if (assignedSet.contains(roomId)) {
                System.out.println("❌ Duplicate Room ID detected!");
                continue;
            }

            // Step 4: Atomic Allocation
            assignedSet.add(roomId);        // assign room
            inventory.decrement(roomType);  // update inventory

            // Step 5: Confirm booking
            System.out.println("✅ Booking Confirmed!");
            System.out.println("Guest: " + guest);
            System.out.println("Room Type: " + roomType);
            System.out.println("Room ID: " + roomId);
        }
    }

    // Room ID generator
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
    }
}

// Main Class
public class BookMyAPP {
    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "SingleRoom"));
        queue.addRequest(new Reservation("Bob", "SingleRoom"));
        queue.addRequest(new Reservation("Charlie", "SingleRoom")); // should fail
        queue.addRequest(new Reservation("David", "DoubleRoom"));

        // Process bookings
        bookingService.processBookings(queue, inventory);

        // Final inventory state
        inventory.display();
    }
}
import java.util.*;

// Reservation
class Reservation {
    private String bookingId;
    private String roomType;
    private String roomId;

    public Reservation(String bookingId, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getBookingId() { return bookingId; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
}

// Inventory
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("SingleRoom", 1);
        availability.put("DoubleRoom", 1);
    }

    public void increment(String roomType) {
        availability.put(roomType, availability.get(roomType) + 1);
    }

    public void display() {
        System.out.println("\nInventory:");
        for (String type : availability.keySet()) {
            System.out.println(type + " -> " + availability.get(type));
        }
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void add(Reservation r) {
        bookings.put(r.getBookingId(), r);
    }

    public Reservation get(String bookingId) {
        return bookings.get(bookingId);
    }

    public void remove(String bookingId) {
        bookings.remove(bookingId);
    }

    public boolean exists(String bookingId) {
        return bookings.containsKey(bookingId);
    }
}

// Cancellation Service
class CancellationService {

    // Stack for rollback (LIFO)
    private Stack<String> releasedRoomIds = new Stack<>();

    public void cancelBooking(String bookingId,
                              BookingHistory history,
                              RoomInventory inventory) {

        System.out.println("\nProcessing cancellation for " + bookingId);

        // ✅ Step 1: Validate existence
        if (!history.exists(bookingId)) {
            System.out.println("❌ Cancellation failed: Booking not found");
            return;
        }

        Reservation r = history.get(bookingId);

        // ✅ Step 2: Record room ID in rollback stack
        releasedRoomIds.push(r.getRoomId());

        // ✅ Step 3: Restore inventory
        inventory.increment(r.getRoomType());

        // ✅ Step 4: Remove from booking history
        history.remove(bookingId);

        // ✅ Step 5: Confirm cancellation
        System.out.println("✅ Booking cancelled successfully");
        System.out.println("Released Room ID: " + r.getRoomId());
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent first): " + releasedRoomIds);
    }
}

// Main Class
public class BookMyAPP {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        System.out.println("=== Booking Cancellation System ===\n");

        // Simulate confirmed bookings
        Reservation r1 = new Reservation("BKG-1", "SingleRoom", "SR-111");
        Reservation r2 = new Reservation("BKG-2", "DoubleRoom", "DR-222");

        history.add(r1);
        history.add(r2);

        // Cancel valid booking
        cancelService.cancelBooking("BKG-1", history, inventory);

        // Try cancelling again (should fail)
        cancelService.cancelBooking("BKG-1", history, inventory);

        // Cancel another booking
        cancelService.cancelBooking("BKG-2", history, inventory);

        // Show rollback stack
        cancelService.showRollbackStack();

        // Final inventory
        inventory.display();
    }
}
import java.io.*;
import java.util.*;

// ✅ Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private String guestName;
    private String roomType;

    public Reservation(String bookingId, String guestName, String roomType) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getBookingId() { return bookingId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println(bookingId + " | " + guestName + " | " + roomType);
    }
}

// ✅ Inventory (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("SingleRoom", 2);
        availability.put("DoubleRoom", 1);
    }

    public Map<String, Integer> getAvailability() {
        return availability;
    }

    public void display() {
        System.out.println("Inventory: " + availability);
    }
}

// ✅ Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> bookings = new ArrayList<>();

    public void add(Reservation r) {
        bookings.add(r);
    }

    public List<Reservation> getAll() {
        return bookings;
    }

    public void display() {
        for (Reservation r : bookings) {
            r.display();
        }
    }
}

// ✅ Wrapper class (Entire System State)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    RoomInventory inventory;
    BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }
}

// ✅ Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("✅ System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("✅ System state restored from file.");
            return (SystemState) in.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("⚠ No saved state found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Corrupted data. Starting with clean state.");
        }

        // Safe fallback
        return new SystemState(new RoomInventory(), new BookingHistory());
    }
}

// 🚀 Main Class
public class BookMyAPP {
    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        // 🔄 Load previous state
        SystemState state = persistence.load();

        RoomInventory inventory = state.inventory;
        BookingHistory history = state.history;

        System.out.println("\n=== Current System State ===");
        inventory.display();
        history.display();

        // Simulate new booking
        System.out.println("\nAdding new booking...");
        Reservation r = new Reservation("BKG-" + System.currentTimeMillis(),
                "Alice", "SingleRoom");

        history.add(r);

        // Save updated state
        persistence.save(new SystemState(inventory, history));

        System.out.println("\nSystem ready for next restart.");
    }
}
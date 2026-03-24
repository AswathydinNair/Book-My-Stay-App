import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation
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

// Inventory
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("SingleRoom", 1);
        availability.put("DoubleRoom", 1);
    }

    public int getAvailable(String roomType) {
        return availability.getOrDefault(roomType, -1);
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int count = getAvailable(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for " + roomType);
        }

        availability.put(roomType, count - 1);
    }
}


class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        // Null / empty check
        if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        // Room type validation
        if (inventory.getAvailable(r.getRoomType()) == -1) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Availability check
        if (inventory.getAvailable(r.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + r.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    public void bookRoom(Reservation r, RoomInventory inventory) {
        try {

            BookingValidator.validate(r, inventory);


            inventory.decrement(r.getRoomType());

            System.out.println(" Booking successful for " + r.getGuestName()
                    + " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {

            System.out.println(" Booking failed: " + e.getMessage());
        }
    }
}

// Main Class
public class BookMyAPP {
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService();

        System.out.println("=== Booking with Validation ===\n");

        service.bookRoom(new Reservation("Alice", "SingleRoom"), inventory);

        service.bookRoom(new Reservation("Bob", "LuxuryRoom"), inventory);

        service.bookRoom(new Reservation("", "DoubleRoom"), inventory);

        service.bookRoom(new Reservation("Charlie", "SingleRoom"), inventory);

        System.out.println("\nSystem continues running safely.");
    }
}
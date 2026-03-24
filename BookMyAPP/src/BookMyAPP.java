import java.util.*;

// Reservation (enhanced with bookingId)
class Reservation {
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
        System.out.println("BookingID: " + bookingId +
                ", Guest: " + guestName +
                ", Room: " + roomType);
    }
}

// 📦 Booking History (Storage)
class BookingHistory {

    // List preserves order (chronological)
    private List<Reservation> history = new ArrayList<>();

    // Store confirmed booking
    public void addBooking(Reservation reservation) {
        history.add(reservation);
    }

    // Read-only access
    public List<Reservation> getAllBookings() {
        return Collections.unmodifiableList(history);
    }
}

// 📊 Report Service (Read-only)
class BookingReportService {

    // Display all bookings
    public void showAllBookings(BookingHistory history) {
        System.out.println("\n=== Booking History ===");

        for (Reservation r : history.getAllBookings()) {
            r.display();
        }
    }

    // Summary report
    public void generateSummary(BookingHistory history) {
        System.out.println("\n=== Booking Summary Report ===");

        Map<String, Integer> roomCount = new HashMap<>();
        int totalBookings = 0;

        for (Reservation r : history.getAllBookings()) {
            totalBookings++;

            String type = r.getRoomType();
            roomCount.put(type, roomCount.getOrDefault(type, 0) + 1);
        }

        System.out.println("Total Bookings: " + totalBookings);

        for (String type : roomCount.keySet()) {
            System.out.println(type + ": " + roomCount.get(type));
        }
    }
}

// 🔧 Simulated Booking Service (integration point from UC6)
class BookingService {

    private int counter = 1;

    // Simulate confirmed booking
    public Reservation confirmBooking(String guest, String roomType) {
        String bookingId = "BKG-" + counter++;
        System.out.println("✅ Booking Confirmed for " + guest + " | ID: " + bookingId);

        return new Reservation(bookingId, guest, roomType);
    }
}

// 🚀 Main Class
public class BookMyAPP {
    public static void main(String[] args) {

        BookingService bookingService = new BookingService();
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        System.out.println("=== Booking System with History ===\n");

        // Simulate confirmed bookings (from UC6)
        Reservation r1 = bookingService.confirmBooking("Alice", "SingleRoom");
        Reservation r2 = bookingService.confirmBooking("Bob", "DoubleRoom");
        Reservation r3 = bookingService.confirmBooking("Charlie", "SingleRoom");

        // Store in history
        history.addBooking(r1);
        history.addBooking(r2);
        history.addBooking(r3);

        // Admin views history
        reportService.showAllBookings(history);

        reportService.generateSummary(history);

        System.out.println("\nNote: Reporting does NOT modify booking data.");
    }
}
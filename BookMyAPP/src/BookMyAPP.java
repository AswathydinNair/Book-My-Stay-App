import java.util.*;

// Reservation class (represents a booking request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + ", Requested Room: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (read-only)
    public void viewRequests() {
        if (queue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Requests in Queue (FIFO Order):");
        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (without removing)
    public Reservation peekNextRequest() {
        return queue.peek();
    }
}

// Main Class
public class BookMyAPP {
    public static void main(String[] args) {

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        System.out.println("=== Booking Request Intake System ===\n");

        // Simulating guest booking requests
        Reservation r1 = new Reservation("Alice", "SingleRoom");
        Reservation r2 = new Reservation("Bob", "DoubleRoom");
        Reservation r3 = new Reservation("Charlie", "SuiteRoom");

        // Add to queue (FIFO)
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // View queue
        requestQueue.viewRequests();

        // Peek next request
        System.out.println("\nNext request to process:");
        Reservation next = requestQueue.peekNextRequest();
        if (next != null) {
            next.display();
        }

        System.out.println("\nNote: No inventory updated. Requests only queued.");
    }
}
import java.util.*;

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

// Shared Booking Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Added: " + r.getGuestName());
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// Shared Inventory (CRITICAL RESOURCE)
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("SingleRoom", 2);
    }

    // 🔴 Critical Section
    public synchronized boolean allocateRoom(String roomType) {
        int available = availability.getOrDefault(roomType, 0);

        if (available <= 0) {
            return false;
        }

        // Simulate delay (to expose race condition if not synchronized)
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        availability.put(roomType, available - 1);
        return true;
    }

    public void display() {
        System.out.println("\nFinal Inventory: " + availability);
    }
}

// Concurrent Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;

            // Get request safely
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            System.out.println(Thread.currentThread().getName() +
                    " processing " + r.getGuestName());

            // Allocate safely
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println("✅ " + r.getGuestName() + " booked successfully");
            } else {
                System.out.println("❌ " + r.getGuestName() + " failed (No rooms)");
            }
        }
    }
}

// Main Class
public class BookMyAPP {
    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        System.out.println("=== Concurrent Booking Simulation ===\n");

        // Simulate multiple users
        queue.addRequest(new Reservation("Alice", "SingleRoom"));
        queue.addRequest(new Reservation("Bob", "SingleRoom"));
        queue.addRequest(new Reservation("Charlie", "SingleRoom")); // extra

        // Multiple threads (guests)
        Thread t1 = new BookingProcessor(queue, inventory);
        Thread t2 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {}

        inventory.display();

        System.out.println("\nNo double booking occurred ✅");
    }
}
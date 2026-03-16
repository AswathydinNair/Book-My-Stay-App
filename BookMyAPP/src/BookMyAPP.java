// Abstract Room class
abstract class Room {
    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;
    protected int available;

    // Parameterized constructor
    Room(int numberOfBeds, int squareFeet, double pricePerNight, int available) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    // Display room details
    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
        System.out.println("Available: " + available);
        System.out.println();
    }
}

// SingleRoom subclass
class SingleRoom extends Room {
    public SingleRoom(int available) {
        super(1, 250, 1500.0, available);
    }
}

// DoubleRoom subclass
class DoubleRoom extends Room {
    public DoubleRoom(int available) {
        super(2, 400, 2500.0, available);
    }
}

// SuiteRoom subclass
class SuiteRoom extends Room {
    public SuiteRoom(int available) {
        super(3, 750, 5000.0, available);
    }
}

// Main class to test
public class BookMyAPP {
    public static void main(String[] args) {
        System.out.println("Hotel Room Initialization\n");

        Room singleRoom = new SingleRoom(5);
        Room doubleRoom = new DoubleRoom(3);
        Room suiteRoom = new SuiteRoom(2);

        System.out.println("Single Room:");
        singleRoom.displayRoomDetails();

        System.out.println("Double Room:");
        doubleRoom.displayRoomDetails();

        System.out.println("Suite Room:");
        suiteRoom.displayRoomDetails();
    }
}
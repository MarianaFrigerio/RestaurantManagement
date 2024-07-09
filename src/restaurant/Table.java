package restaurant;

public class Table {
    public final int size; // number of chairs around this table

    /* I added a field for the empty seats to have control over how many seats
     * a table has as empty and the easiest data structure that I've found was int */
    private int emptySeats; // number of empty seats at this table

    public Table(int size) {
        // validation of Table size
        if (size < 2 || size > 6) {
            throw new IllegalArgumentException("Table size can only be of 2, 3, 4, 5 or 6 people");
        }
        this.size = size;
        this.emptySeats = size;
    }

    /* Getter of the field emptySeats */
    public int getEmptySeats() {
        return emptySeats;
    }

    /* Method that fills the specified seats entered as parameters.
     * Inside this method there's also a number of seats validation so the emptySeats
     * field will only be updated when these validations are passed */
    public void fillSeats(int seats) {
        if (seats <= 0 || seats > emptySeats) {
            throw new IllegalArgumentException("Invalid number of seats to fill");
        }
        this.emptySeats -= seats;
    }

    /* Method that releases the specified seats entered as parameters.
     * Inside this method there's also a number of seats validation so the emptySeats
     * field will only be updated when these validations are passed */
    public void releaseSeats(int seats) {
        if (seats <= 0 || seats > (size - emptySeats)) {
            throw new IllegalArgumentException("Invalid number of seats to release");
        }
        this.emptySeats += seats;
    }
}

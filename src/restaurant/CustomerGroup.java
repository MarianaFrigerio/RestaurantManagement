package restaurant;

public class CustomerGroup {
    public final int size; // number of people in the group

    public CustomerGroup(int size) {
        // validation of CustomerGroup size
        if (size <= 0 || size > 6) {
            throw new IllegalArgumentException(
                    "Customer group size must be of between 1 and 6 people");
        }
        this.size = size;
    }
}

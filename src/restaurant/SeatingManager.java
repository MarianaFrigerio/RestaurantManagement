package restaurant;

import java.util.*;

public class SeatingManager {

    private static final String GROUP_IS_NULL_EXCEPTION_MESSAGE = "CustomerGroup can not be null";

    /* I've let the type of the group of tables as List because it was the data structured that there was
     * on the initial exercise, but I would have tried to change (if I could) onto a HashSet or any other
     * similar data structure
     */
    private final List<Table> tables;

    /* I've used a HashMap to link the customer groups with the table where they are seated.
     * I choose this type of data structure because of its efficiency (ot has an average execution time
     * on all of its main functions of O(1) and because I didn't need the objects to be stored
     * on a certain way or ordering.
     * Besides this, I thought HashMap as a good choice because is searchable by the two linked objects
     * (on our case CustomerGroup and Table), so there aren't many differences between being
     * a "key" or a "value"
     */
    private final HashMap<CustomerGroup, Table> groupTableHashMap;

    /* For this data structure I was inspired by real restaurants that have waiting lists.
     * Having said that, using a list is one of the best options for this object
     * as we need this list to be ordered and to be flexible.
     */
    private final List<CustomerGroup> waitingList;

    /* Constructor */
    public SeatingManager(List<Table> tables, Map<CustomerGroup, Table> groupTableHashMap) {
        if (tables == null || tables.isEmpty()) {
            throw new IllegalArgumentException("Tables list must have some value");
        }

        this.tables = tables;
        // I was forced by SonarLint to put a cast from Map onto a HashMap.
        this.groupTableHashMap = (HashMap<CustomerGroup, Table>) groupTableHashMap;
        this.waitingList = new ArrayList<>();
    }

    /* For the method 'arrives' I wanted to use a functional programming algorithm
     * but on the implementation phase I saw that I could use a return inside the "if" to finish the
     * execution of the loop so this final version is the most efficient one.
     * Having on consideration that "tables" is a list of object of type Table I used a
     * type of for-each that, being the code simple and direct, is the perfect loop to use in here.
     * On this method we have two main flows - one when there are free seats on a table and the one
     * when all the tables are full and we can only add the group onto the waiting list.
     *
     * Regarding the complexity of this method in terms of running time the worst case scenario would be of
     *  O(n) with n being the number of tables.
     * This worst case scenario in terms of speed is the one where there are lots of tables
     * and only the last one has free seating space.
     *
     * In terms of memory consumption this method has a complexity of O(g + m)
     * where the g is the number of seated customer groups and the n is the number of groups on the waiting
     * list.
     */
    /* Group arrives and wants to be seated. */
    public void arrives(CustomerGroup group) {
        // validation to avoid Null Pointer Exceptions when accessing group.size
        if (group == null) {
            throw new IllegalArgumentException(GROUP_IS_NULL_EXCEPTION_MESSAGE);
        }

        int size = group.size;
        for (Table table : tables) {

            if (table.getEmptySeats() >= size) {
                groupTableHashMap.put(group, table);
                table.fillSeats(size);
                return;
            }
        }
        waitingList.add(group);
    }

    /* I am a big fan of using Optionals instead of null checks - the possibilities of having a
     * Null Pointer Exception decrease because all the scenarios are covered and checked.
     * On this case I implemented the method "leaves" using functional programming, checking if the table
     * has value or is null and depending on that I would execute some actions or others.
     *
     * Regarding the complexity of this method in terms of running time the worst case scenario would be of
     *  O(n) with n being the waiting groups.
     * This worst case scenario in terms of speed is the one where the table from the group
     * that's leaving is present and is tried to sit groups on the now free table.
     *
     * In terms of memory consumption this method has a complexity of O(g + m)
     * where the g is the number of seated customer groups and the m is the number of groups on the waiting
     * list.
     */
    /* Whether seated or not, the group leaves the restaurant. */
    public void leaves(CustomerGroup group) {
        if (group == null) {
            throw new IllegalArgumentException(GROUP_IS_NULL_EXCEPTION_MESSAGE);
        }

        Optional.ofNullable(groupTableHashMap.remove(group))
                .ifPresentOrElse(
                        table -> {
                            table.releaseSeats(group.size);
                            tryToSeatWaitingGroups(table);
                        },
                        () -> waitingList.remove(group));
    }

    /* Return the table at which the group is seated, or null if
    they are not seated (whether they're waiting or already left). */
    public Table locate(CustomerGroup group) {
        if (group == null) {
            throw new IllegalArgumentException(GROUP_IS_NULL_EXCEPTION_MESSAGE);
        }

        /* Thanks to the implementation of the HashMaps we can put the null and non-null
         *all in one line of code */
        return groupTableHashMap.get(group);
    }

    /* The development of this method was a little difficult for me as
     * I had to do some changes to it in the middle of the development stage.
     * In the beginning I was checking all the tables to there was one that
     * had free space for a group, then I realised I could directly
     * pass as an argument the freed table and compare the sizes of the
     * groups on the waiting list with the empty spaces that this table has
     *
     *
     * Regarding the complexity of this method in terms of running time the worst case scenario would be of
     * O(m * k) with m being the amount of groups on the waiting list and
     * the k being the number of groups to be seated.
     * This worst case scenario in terms of speed is the one where there is more than one
     * group that is seated so more than one group need to be removed from
     * the waiting list
     *
     * In terms of memory consumption this method has a complexity of O(n + m + k)
     * with n being the amount of groups on the waiting list,
     * the m being the number of groups to be seated list and the k is the amount of groups that are
     * registered on the restaurant (on the HashMap).
     */
    private void tryToSeatWaitingGroups(Table freedTable) {
        List<CustomerGroup> seatedGroups = new ArrayList<>();

        waitingList.forEach(
                customerGroup -> {
                    if (freedTable.getEmptySeats() >= customerGroup.size) {
                        freedTable.fillSeats(customerGroup.size);
                        groupTableHashMap.put(customerGroup, freedTable);
                        seatedGroups.add(customerGroup);
                    }
                });

        waitingList.removeAll(seatedGroups);
    }
}

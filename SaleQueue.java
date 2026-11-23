import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class SaleQueue {

    private class Node {
        Sale data;
        Node next;

        Node(Sale data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node front;
    private Node rear;
    
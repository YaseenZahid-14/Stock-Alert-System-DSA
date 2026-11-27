import java.util.ArrayList;
import java.util.List;

public class AlertQueue {

    private class Node {
        String message;
        Node next;

        Node(String message) {
            this.message = message;
            this.next = null;
        }
    }
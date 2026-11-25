import java.util.ArrayList;
import java.util.List;

public class ActivityStack {

    private class Node {
        String message;
        Node next;

        Node(String message, Node next) {
            this.message = message;
            this.next = next;
        }
    }

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

    private Node top;

    public void push(String message) {
        top = new Node(message, top);
    }

    public List<String> getAll() {
        List<String> activities = new ArrayList<>();

        Node current = top;

        while (current != null) {
            activities.add(current.message);
            current = current.next;
        }

        return activities;
    }
}

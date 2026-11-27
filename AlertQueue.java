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

    private Node front;
    private Node rear;

    public void enqueue(String message) {
        Node newNode = new Node(message);

        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
    }

    public List<String> getAll() {
        List<String> alerts = new ArrayList<>();

        Node current = front;

        while (current != null) {
            alerts.add(current.message);
            current = current.next;
        }

        return alerts;
    }
}
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
    public void enqueue(Sale sale) {
        Node newNode = new Node(sale);

        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
    }

    public List<Sale> getAll() {
        List<Sale> sales = new ArrayList<>();

        Node current = front;

        while (current != null) {
            sales.add(current.data);
            current = current.next;
        }

        return sales;
    }

public class ProductLinkedList {

    private class Node {
        Product data;
        Node next;

        Node(Product data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size = 0;

    public void add(Product product) {
        Node newNode = new Node(product);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;

            while (current.next != null) {
                current = current.next;
            }

            current.next = newNode;
        }

        size++;
    }

    public boolean removeById(int id) {
        if (head == null) {
            return false;
        }

        if (head.data.getId() == id) {
            head = head.next;
            size--;
            return true;
        }

        Node current = head;

        while (current.next != null) {
            if (current.next.data.getId() == id) {
                current.next = current.next.next;
                size--;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    public Product findById(int id) {
        Node current = head;

        while (current != null) {
            if (current.data.getId() == id) {
                return current.data;
            }

            current = current.next;
        }

        return null;
    }

    public Product searchByName(String name) {
        Node current = head;

        while (current != null) {
            if (current.data.getName()
                    .equalsIgnoreCase(name.trim())) {
                return current.data;
            }

            current = current.next;
        }

        return null;
    }

    public Product[] toArray() {
        Product[] products = new Product[size];

        Node current = head;
        int index = 0;

        while (current != null) {
            products[index++] = current.data;
            current = current.next;
        }

        return products;
    }

    public int size() {
        return size;
    }
}

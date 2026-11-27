public class Product {

    private static int nextId = 1;

    private int id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    public Product(String name, String category,
                   double price, int quantity) {

        this.id = nextId++;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Reduce stock when product is sold
    public void reduceQuantity(int amount) {
        quantity -= amount;
    }

    // Add stock when new quantity arrives
    public void addQuantity(int amount) {
        if (amount > 0) {
            quantity += amount;
        }
    }

    // Set updated stock quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {

        if (quantity == 0) {
            return "OUT OF STOCK";

        } else if (quantity <= 5) {
            return "LOW STOCK";

        } else {
            return "AVAILABLE";
        }
    }
}
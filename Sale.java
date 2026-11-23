import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sale {

    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private LocalDateTime dateTime;
public Sale(String productName, int quantity,
                double unitPrice) {

        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;

        this.totalAmount = quantity * unitPrice;
        this.dateTime = LocalDateTime.now();
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

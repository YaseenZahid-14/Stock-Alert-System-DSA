import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryStockAlertSystem extends JFrame {

    private static final int LOW_LIMIT = 5;

    private ProductLinkedList products = new ProductLinkedList();
    private AlertQueue alerts = new AlertQueue();
    private ActivityStack activities = new ActivityStack();
    private SaleQueue sales = new SaleQueue();

    private JTextField nameField = new JTextField(9);
    private JTextField categoryField = new JTextField(9);
    private JTextField priceField = new JTextField(6);
    private JTextField quantityField = new JTextField(4);

    private JTextField searchField = new JTextField(10);
    private JTextField sellField = new JTextField(4);

    private JLabel productLabel =
        new JLabel("Total Products: 0");

    private JLabel dailySalesLabel =
        new JLabel("Today's Sales: Rs. 0.00");

    private DefaultTableModel model =
        new DefaultTableModel(
            new String[]{
                "ID", "Product", "Category",
                "Price", "Quantity", "Status"
            }, 0
        ) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

    private JTable table = new JTable(model);

    public InventoryStockAlertSystem() {

        setTitle("Supermarket Inventory System");

        setSize(1050, 650);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        buildGUI();

        refreshTable(products.toArray());
    }

    private void buildGUI() {

        JPanel root = new JPanel(new BorderLayout(10, 10));

        root.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        root.setBackground(new Color(240, 244, 249));

        setContentPane(root);

        JLabel title = new JLabel(
            "SUPERMARKET INVENTORY & STOCK ALERT SYSTEM"
        );

        title.setFont(
            new Font("Arial", Font.BOLD, 20)
        );

        JPanel header = new JPanel(new GridLayout(2, 1));

        header.setOpaque(false);

        header.add(title);

        JPanel stats = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 20, 5)
        );

        stats.setOpaque(false);

        stats.add(productLabel);
        stats.add(dailySalesLabel);

        header.add(stats);

        root.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));

        center.setOpaque(false);

        center.add(createAddPanel(), BorderLayout.NORTH);

        table.setRowHeight(28);

        center.add(new JScrollPane(table), BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);

        root.add(createButtons(), BorderLayout.SOUTH);
        
    }

    private JPanel createAddPanel() {

        JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 8, 10)
        );

        panel.setBorder(
            BorderFactory.createTitledBorder("Add New Product")
        );

        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Category:"));
        panel.add(categoryField);

        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        JButton add = new JButton("Add Product");

        add.addActionListener(e -> addProduct());

        panel.add(add);

        return panel;
    }

   
    private JPanel createButtons() {

        JPanel panel = new JPanel(
            new GridLayout(3, 1, 5, 5)
        );

        panel.setOpaque(false);

        // Checkout section

        JPanel checkout = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        checkout.setBorder(
            BorderFactory.createTitledBorder("Cashier Checkout")
        );

        checkout.add(new JLabel("Purchased Quantity:"));
        checkout.add(sellField);

        JButton sell = new JButton("Sell Product");

        sell.addActionListener(e -> sellProduct());

        checkout.add(sell);

        panel.add(checkout);

        // Inventory buttons

        JPanel row1 = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        JButton remove = new JButton("Remove Product");
        JButton view = new JButton("View All");
        JButton low = new JButton("Low Stock");
        JButton out = new JButton("Out of Stock");

        // UPDATE STOCK BUTTON
        JButton updateStock = new JButton("Update Existing Stock");

        remove.addActionListener(e -> removeProduct());

        view.addActionListener(
            e -> refreshTable(products.toArray())
        );

        low.addActionListener(
            e -> filterProducts("LOW STOCK")
        );

        out.addActionListener(
            e -> filterProducts("OUT OF STOCK")
        );

        // Update stock action listener
        updateStock.addActionListener(
            e -> updateExistingStock()
        );

        row1.add(remove);
        row1.add(view);
        row1.add(low);
        row1.add(out);
        row1.add(updateStock);

        panel.add(row1);

        // Search and history buttons

        JPanel row2 = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        row2.add(new JLabel("Search:"));
        row2.add(searchField);

        JButton search = new JButton("Search");
        JButton sortName = new JButton("Sort by Name");
        JButton sortQty = new JButton("Sort by Quantity");
        JButton history = new JButton("Sales & Alert History");

        search.addActionListener(e -> searchProduct());

        sortName.addActionListener(
            e -> sortProducts(true)
        );

        sortQty.addActionListener(
            e -> sortProducts(false)
        );

        history.addActionListener(
            e -> showHistory()
        );

        row2.add(search);
        row2.add(sortName);
        row2.add(sortQty);
        row2.add(history);

        panel.add(row2);

        return panel;
    }

    // ADD PRODUCT

    private void addProduct() {

        try {

            String name = nameField.getText().trim();

            String category = categoryField.getText().trim();

            double price = Double.parseDouble(
                priceField.getText().trim()
            );

            int quantity = Integer.parseInt(
                quantityField.getText().trim()
            );

            if (name.isEmpty() || category.isEmpty()
                    || price < 0 || quantity < 0) {

                throw new IllegalArgumentException();
            }

            if (products.searchByName(name) != null) {

                JOptionPane.showMessageDialog(
                    this, "Product already exists!"
                );

                return;
            }

            Product p = new Product(
                name, category, price, quantity
            );

            products.add(p);

            activities.push("Added product: " + name);

            checkStock(name, quantity);

            nameField.setText("");
            categoryField.setText("");
            priceField.setText("");
            quantityField.setText("");

            refreshTable(products.toArray());

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Enter valid product details and values."
            );
        }
    }

    // UPDATE EXISTING STOCK METHOD (FIXED & INTEGRATED)

    private void updateExistingStock() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a product first!"
            );

            return;
        }

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        Product product = products.findById(id);

        if (product == null) {
            return;
        }

        String input = JOptionPane.showInputDialog(
            this,
            "Product: " + product.getName()
            + "\nCurrent Stock: " + product.getQuantity()
            + "\n\nEnter New Stock Quantity to Add:"
        );

        if (input == null) {
            return;
        }

        try {

            int newQuantity = Integer.parseInt(input.trim());

            if (newQuantity <= 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Enter a quantity greater than zero!"
                );

                return;
            }

            int oldStock = product.getQuantity();
            
            


          // Add new quantity to existing stock
product. addQuantity(newQuantity);

int updatedStock = product.getQuantity();

activities.push(
    "Stock updated: " + product.getName()
    + " | Added: " + newQuantity
    + " | New Stock: " + updatedStock
);

checkStock(product.getName(), updatedStock);

refreshTable(products.toArray());

JOptionPane.showMessageDialog(
    this,
    "STOCK UPDATED SUCCESSFULLY!"
    + "\n\nProduct: " + product.getName()
    + "\nPrevious Stock: " + oldStock
    + "\nAdded Quantity: " + newQuantity
    + "\nUpdated Stock: " + updatedStock
);
        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid whole number!"
            );
        }
    }

    // SELL PRODUCT / CHECKOUT

    private void sellProduct() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                this, "Please select a product first!"
            );

            return;
        }

        try {

            int id = Integer.parseInt(
                model.getValueAt(row, 0).toString()
            );

            int soldQuantity = Integer.parseInt(
                sellField.getText().trim()
            );

            if (soldQuantity <= 0) {
                throw new NumberFormatException();
            }

            Product p = products.findById(id);

            if (p == null) {
                return;
            }

            if (soldQuantity > p.getQuantity()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Not enough stock! Available: "
                    + p.getQuantity()
                );

                return;
            }

            double total =
                soldQuantity * p.getPrice();

            p.reduceQuantity(soldQuantity);

            Sale sale = new Sale(
                p.getName(),
                soldQuantity,
                p.getPrice()
            );

            sales.enqueue(sale);

            activities.push(
                "Sold " + soldQuantity
                + " x " + p.getName()
            );

            checkStock(
                p.getName(),
                p.getQuantity()
            );

            sellField.setText("");

            refreshTable(products.toArray());

            JOptionPane.showMessageDialog(
                this,
                "SALE COMPLETED\n\n"
                + "Product: " + p.getName()
                + "\nQuantity Sold: " + soldQuantity
                + "\nUnit Price: Rs. " + p.getPrice()
                + "\nTotal Bill: Rs. " + total
                + "\nRemaining Stock: " + p.getQuantity(),
                "Customer Receipt",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this, "Enter a valid sale quantity!"
            );
        }
    }

    // AUTOMATIC STOCK ALERTS

    private void checkStock(String name, int quantity) {

        if (quantity == 0) {

            String message =
                "OUT OF STOCK: " + name;

            alerts.enqueue(message);

            JOptionPane.showMessageDialog(
                this, message, "Stock Alert",
                JOptionPane.WARNING_MESSAGE
            );

        } else if (quantity <= LOW_LIMIT) {

            String message =
                "LOW STOCK: " + name
                + " - Remaining: " + quantity;

            alerts.enqueue(message);

            JOptionPane.showMessageDialog(
                this, message, "Stock Alert",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // REMOVE PRODUCT

    private void removeProduct() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this, "Select a product first!"
            );
            return;
        }

        int id = Integer.parseInt(
            model.getValueAt(row, 0).toString()
        );

        String name = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Remove " + name + "?",
            "Confirm",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            products.removeById(id);

            activities.push("Removed product: " + name);

            refreshTable(products.toArray());
        }
    }

    // SEARCH PRODUCT

    private void searchProduct() {

        String name = searchField.getText().trim();

        Product p = products.searchByName(name);

        if (p == null) {

            JOptionPane.showMessageDialog(
                this, "Product not found!"
            );

            return;
        }

        refreshTable(new Product[]{p});
    }

    // FILTER PRODUCTS

    private void filterProducts(String status) {

        Product[] all = products.toArray();

        java.util.ArrayList<Product> result =
            new java.util.ArrayList<>();

        for (Product p : all) {

            if (p.getStatus().equals(status)) {
                result.add(p);
            }
        }

        refreshTable(
            result.toArray(new Product[0])
        );
    }

    // BUBBLE SORT

    private void sortProducts(boolean byName) {

        Product[] arr = products.toArray();

        for (int i = 0; i < arr.length - 1; i++) {

            for (int j = 0; j < arr.length - i - 1; j++) {

                boolean swap;

                if (byName) {

                    swap = arr[j].getName()
                        .compareToIgnoreCase(
                            arr[j + 1].getName()
                        ) > 0;

                } else {

                    swap = arr[j].getQuantity()
                        > arr[j + 1].getQuantity();
                }

                if (swap) {

                    Product temp = arr[j];

                    arr[j] = arr[j + 1];

                    arr[j + 1] = temp;
                }
            }
        }

        refreshTable(arr);
    }

    // SALES AND ALERT HISTORY

    private void showHistory() {

        StringBuilder text = new StringBuilder();

        text.append("TODAY'S TOTAL SALES\n");

        text.append(
            "Rs. " + String.format(
                "%,.2f", sales.getTodaySales()
            )
        );

        text.append("\n\nSALES HISTORY\n");

        List<Sale> allSales = sales.getAll();

        if (allSales.isEmpty()) {
            text.append("No sales yet.\n");
        }

        for (Sale sale : allSales) {
            text.append(sale.toString()).append("\n");
        }

        text.append("\nSTOCK ALERT HISTORY\n");

        List<String> allAlerts = alerts.getAll();

        if (allAlerts.isEmpty()) {
            text.append("No alerts yet.\n");
        }

        for (String alert : allAlerts) {
            text.append(alert).append("\n");
        }

        text.append("\nRECENT ACTIVITY\n");

        List<String> allActivities =
            activities.getAll();

        for (String activity : allActivities) {
            text.append(activity).append("\n");
        }

        JTextArea area = new JTextArea(
            text.toString(), 20, 60
        );

        area.setEditable(false);

        JOptionPane.showMessageDialog(
            this,
            new JScrollPane(area),
            "Daily Sales & History",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // REFRESH TABLE AND DAILY TOTAL

    private void refreshTable(Product[] list) {

        model.setRowCount(0);

        for (Product p : list) {

            model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getCategory(),
                "Rs. " + p.getPrice(),
                p.getQuantity(),
                p.getStatus()
            });
        }

        productLabel.setText(
            "Total Products: " + products.size()
        );

        dailySalesLabel.setText(
            "Today's Sales: Rs. "
            + String.format(
                "%,.2f", sales.getTodaySales()
            )
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            InventoryStockAlertSystem app =
                new InventoryStockAlertSystem();

            app.setVisible(true);
        });
    }
}
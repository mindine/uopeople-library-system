public class Book {
    private static int idCounter = 1;
    private String id;
    private String title;
    private String author;
    private int quantity;
    private int totalStock;

    public Book(String title, String author, int quantity) {
        this.id = String.valueOf(idCounter++);
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.totalStock = quantity;
    }

    public Book(String title, String author, int quantity, int totalStock) {
        this.id = String.valueOf(idCounter++);
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.totalStock = totalStock;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getQuantity() { return quantity; }
    public int getTotalStock() { return totalStock; }

    public void addQuantity(int quantityToAdd) {
        this.quantity += quantityToAdd;
        this.totalStock += quantityToAdd;
    }

    public boolean borrowBook(int quantityToBorrow) {
        if (this.quantity >= quantityToBorrow) {
            this.quantity -= quantityToBorrow;
            return true;
        }
        return false;
    }

    public void returnBook(int quantityToReturn) {
        this.quantity += quantityToReturn;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Quantity: " + quantity;
    }

    public String toCSV() {
        return title + "," + author + "," + quantity + "," + totalStock;
    }
}

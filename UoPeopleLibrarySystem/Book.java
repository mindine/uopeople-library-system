// Create the Book class
public class Book {
    // Static ID counter to assign unique IDs
    private static int idCounter = 1;
    private String id;
    private String title;
    private String author;
    private int quantity;
    private int totalStock; // Total stock available for the book

    // Constructor to initialize a new book
    public Book(String title, String author, int quantity) {
        this.id = String.valueOf(idCounter++); // Auto-incremented ID
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.totalStock = quantity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getQuantity() {
        return quantity;
    }

    //Add more copies of the book
    public void addQuantity(int quantityToAdd) {
        this.quantity += quantityToAdd;
        this.totalStock += quantityToAdd;
    }

    // Borrow copies if available
    public boolean borrowBook(int quantityToBorrow) {
        if (this.quantity >= quantityToBorrow) {
            this.quantity -= quantityToBorrow;
            return true; // Successfully borrowed
        }
        return false; // Not enough copies available
    }

    // Return borrowed books
    public void returnBook(int quantityToReturn) {
        this.quantity += quantityToReturn;
    }

    // Get total stock available for the book
    public int getTotalStock() {
        return totalStock;
    }

    // String representation of a book
    @Override public String toString() {
        return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Quantity: " + quantity;
    }

    // Constructor for loading from file
    public Book(String title, String author, int quantity, int totalStock) {
        this.id = String.valueOf(idCounter++); // Assign ID
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.totalStock = totalStock;
    }

    // Format for saving to CSV
    public String toCSV() {
        return title + "," + author + "," + quantity + "," + totalStock;
    }
}
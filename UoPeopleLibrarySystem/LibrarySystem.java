import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LibrarySystem {
    private static Map<String, Book> library = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1; loadLibraryFromFile();

        // Load existing books at startup
        displayMenu();

        while (option != 0) {
            try { System.out.print("Your option:=> ");
                option = Integer.parseInt(scanner.nextLine().trim());
                switch (option) {
                    case 1: addBooks(scanner);
                    break;
                    case 2: borrowBooks(scanner);
                    break;
                    case 3: returnBooks(scanner);
                    break;
                    case 4: listAllBooks(); // Added functionality
                    break;
                    case 0: System.out.println("You chose to exit. Bye for now!");
                    break;
                    default: System.out.println("Invalid option. Please try again.");
                    System.out.print("Your option:=> ");
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        } scanner.close();
    }

    // Display the main menu
    public static void displayMenu() {
        System.out.println("===============================================");
        System.out.println("Welcome to UoPeople Library System's Main Menu");
        System.out.println("Please enter any of the corresponding options 1, 2, 3, 4, or 0");
        System.out.println("based on the operation you wish to carry out.");
        System.out.println("===============================================");
        System.out.println("1. Add Books");
        System.out.println("2. Borrow Books");
        System.out.println("3. Return Books");
        System.out.println("4. List All Books"); // Added functionality
        System.out.println("0. Exit");
        System.out.println("===============================================");
    }

    // Add Books Feature
    private static void addBooks(Scanner scanner) {
        try {
            // Prompt the user for book title
            System.out.print("Enter book title: ");
            String title = scanner.nextLine().trim();

            // Prompt the user for book author
            System.out.print("Enter book author: ");
            String author = scanner.nextLine().trim();

            // Prompt user for book quantity and validate it
            int quantity = -1;
            while (quantity <= 0) {
                System.out.print("Enter quantity: ");
                String quantityStr = scanner.nextLine().trim();
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) {
                        System.out.println("Quantity must be a positive number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            // Create unique key by combining title and author
            String key = title + "-" + author;

            // Check if book exists; if yes, update quantity, else add new book
            if (library.containsKey(key)) {
                Book existingBook = library.get(key);
                existingBook.addQuantity(quantity);
                System.out.println("Book already exists. Updated quantity:");
                System.out.println(existingBook);
            } else {
                Book newBook = new Book(title, author, quantity);
                library.put(key, newBook);
                System.out.println("New book added successfully:");
                System.out.println(newBook);
            }

            System.out.println(); // Empty line for better output readability
            displayMenu(); // Redisplay the menu after operation
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while adding a book. Please try again.");
        }
        saveLibraryToFile(); // Save updated library
    }

    public static void borrowBooks(Scanner scanner) {
        try {
            // Prompt user for book title and author
            String title = readNonEmptyLine(scanner, "Enter book title:=> ");
            String author = readNonEmptyLine(scanner, "Enter book author:=> ");
            String key = title + "-" + author;

            // Check if the book exists in the library
            if (!library.containsKey(key)) {
                System.out.println(" Book not found in the library.");
                displayMenu(); // Show menu and exit method
                return;
            }

            Book book = library.get(key);
            int available = book.getQuantity();
            int quantity = -1; // First prompt without option to go back

            System.out.print("Enter quantity to borrow:=> ");
            while (quantity <= 0) {
                String input = scanner.nextLine().trim();
                // Option to go back (offered after invalid input)
                if (input.equals("0")) {
                    System.out.println("Returning to main menu...");
                    displayMenu();
                    return;
                }

                try {
                    quantity = Integer.parseInt(input);
                    if (quantity <= 0) {
                        System.out.println("Invalid quantity. Please enter a positive number.");
                        System.out.print("Enter quantity to borrow (or 0 to return to menu):=> ");
                        continue;
                    }

                    if (quantity > available) {
                        System.out.println(" Not enough copies available. Available quantity: " + available);
                        System.out.print("Enter quantity to borrow (or 0 to return to menu):=> ");
                        quantity = -1;
                        continue;
                    }

                    // If valid and available, borrow
                    if (book.borrowBook(quantity)) {
                        System.out.println(" Book borrowed successfully.");
                        System.out.println(book.toString());
                        saveLibraryToFile(); // Save change
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                    System.out.print("Enter quantity to borrow (or 0 to return to menu):=> ");
                }
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while borrowing a book.");
        }

        displayMenu(); // Show main menu after operation
    }

    // Return Books Feature
    public static void returnBooks(Scanner scanner) {
        try {
            // Step 1: Get book title and author from user
            String title = readNonEmptyLine(scanner, "Enter book title:=> ");
            String author = readNonEmptyLine(scanner, "Enter book author:=> ");

            // Step 2: Create key to identify the book in the library
            String key = title + "-" + author;

            // Step 3: Check if the book exists in the library
            if (!library.containsKey(key)) {
                System.out.println(" This book does not belong to our library.");
                displayMenu();
                return;
            }

            // Step 4: Fetch the book and determine how many copies can be returned
            Book book = library.get(key);
            int maxReturnable = book.getTotalStock() - book.getQuantity();

            // Step 5: If no copies were borrowed, inform user and return to menu
            if (maxReturnable == 0) {
                System.out.println(" You haven’t borrowed any copies of this book.");
                System.out.println("Returning to main menu...");
                displayMenu();
                return;
            }

            // Step 6: Prompt user for quantity to return
            while (true) {
                System.out.print("Enter quantity to return (or 0 to return to menu):=> ");
                String input = scanner.nextLine().trim();

                // Allow user to cancel return and go back to menu
                if (input.equals("0")) {
                    System.out.println("Returning to main menu...");
                displayMenu();
                return;
                }

                try {
                int quantity = Integer.parseInt(input);

                // Check if input is a valid positive number
                if (quantity <= 0) {
                    System.out.println("Invalid quantity. Please enter a positive number.");
                    continue;
                }

                // Check if return quantity does not exceed borrowed amount
                if (quantity <= maxReturnable) {
                    // Update the book quantity
                    book.returnBook(quantity);
                    System.out.println(" Book returned successfully.");
                    System.out.println(book.toString());
                    saveLibraryToFile(); // Save changes to file
                    break; // Exit loop after successful return
                } else {
                    System.out.println(" You can only return up to " + maxReturnable + " copies.");
                }
            } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        } catch (Exception e) {
            System.out.println(" An error occurred while returning the book.");
        }

        // Step 7: Show menu again after successful return
        displayMenu();
    }

    // ------------------------------------------------------------------------------------------------------

    // Additional functionalities to make the program more practical and user-friendly

    // List All Books Feature
    public static void listAllBooks() {
        if (library.isEmpty()) {
            System.out.println("No books available in the library.");
        } else {
            System.out.println("Books available in the library:");
            for (Book book : library.values()) {
                System.out.println(book.toString());
            }
        }

        System.out.println(); // Blank line for spacing
        displayMenu(); // Show menu again
    }

    // Save Library Data to File
    public static void saveLibraryToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("library_data.csv"))) {
            for (Book book : library.values()) {
                writer.println(book.toCSV()); // Save title,author,quantity,totalStock
            }
        } catch (IOException e) {
            System.out.println(" Error saving library to file: " + e.getMessage());
        }
    }


    // Load Library Data from File (if needed)
    public static void loadLibraryFromFile() {
        File file = new File("library_data.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String title = parts[0];
                    String author = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    int totalStock = Integer.parseInt(parts[3]);
                    String key = title + "-" + author;
                    library.put(key, new Book(title, author, quantity, totalStock));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(" Error loading library from file: " + e.getMessage());
        }
    }

    // helper method to read a non-empty line from the user
    public static String readNonEmptyLine(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } System.out.println("You pressed the Enter button without entering any input. Please enter a valid input.");
        }
    }
}
import java.io.*;
import java.util.*;

class Book {
    int id;
    String title;
    String author;
    boolean issued;

    Book(int id, String title, String author, boolean issued) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issued = issued;
    }

    @Override
    public String toString() {
        return id + "," + title + "," + author + "," + issued;
    }
}

public class LibraryManagementSystem {

    static final String FILE_NAME = "books.txt";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Search Book");
            System.out.println("4. Issue Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Enter Choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addBook();
                    break;

                case 2:
                    viewBooks();
                    break;

                case 3:
                    searchBook();
                    break;

                case 4:
                    issueBook();
                    break;

                case 5:
                    returnBook();
                    break;

                case 6:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 6);
    }

    static void addBook() {
        try {
            System.out.print("Enter Book ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Book Title: ");
            String title = sc.nextLine();

            System.out.print("Enter Author Name: ");
            String author = sc.nextLine();

            FileWriter fw = new FileWriter(FILE_NAME, true);
            fw.write(id + "," + title + "," + author + ",false\n");
            fw.close();

            System.out.println("Book Added Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();

        try {
            File file = new File(FILE_NAME);

            if (!file.exists())
                return books;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                books.add(new Book(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        Boolean.parseBoolean(data[3])));
            }

            br.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return books;
    }

    static void saveBooks(List<Book> books) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));

            for (Book b : books) {
                bw.write(b.toString());
                bw.newLine();
            }

            bw.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void viewBooks() {

        List<Book> books = loadBooks();

        if (books.isEmpty()) {
            System.out.println("No Books Available.");
            return;
        }

        System.out.println("\n--- Book Records ---");

        for (Book b : books) {
            System.out.println(
                    "ID: " + b.id +
                    " | Title: " + b.title +
                    " | Author: " + b.author +
                    " | Status: " + (b.issued ? "Issued" : "Available"));
        }
    }

    static void searchBook() {

        List<Book> books = loadBooks();

        System.out.print("Enter Book Title: ");
        String search = sc.nextLine();

        boolean found = false;

        for (Book b : books) {

            if (b.title.equalsIgnoreCase(search)) {

                System.out.println(
                        "ID: " + b.id +
                        " | Author: " + b.author +
                        " | Status: " + (b.issued ? "Issued" : "Available"));

                found = true;
            }
        }

        if (!found)
            System.out.println("Book Not Found.");
    }

    static void issueBook() {

        List<Book> books = loadBooks();

        System.out.print("Enter Book ID to Issue: ");
        int id = sc.nextInt();

        boolean found = false;

        for (Book b : books) {

            if (b.id == id) {

                if (!b.issued) {
                    b.issued = true;
                    saveBooks(books);
                    System.out.println("Book Issued Successfully!");
                } else {
                    System.out.println("Book Already Issued.");
                }

                found = true;
                break;
            }
        }

        if (!found)
            System.out.println("Book Not Found.");
    }

    static void returnBook() {

        List<Book> books = loadBooks();

        System.out.print("Enter Book ID to Return: ");
        int id = sc.nextInt();

        boolean found = false;

        for (Book b : books) {

            if (b.id == id) {

                if (b.issued) {
                    b.issued = false;
                    saveBooks(books);
                    System.out.println("Book Returned Successfully!");
                } else {
                    System.out.println("Book is Already Available.");
                }

                found = true;
                break;
            }
        }

        if (!found)
            System.out.println("Book Not Found.");
    }
}
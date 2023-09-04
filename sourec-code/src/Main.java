import Book.Book;
import Book.BookImp;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to your Book management application");
        BookImp book = new BookImp();
        Scanner scan = new Scanner(System.in);
        boolean check = false;
        do{
            System.out.println("1. Add a Book\n" +
                    "2. Show All Books\n" +
                    "3. Show Book based on ISBN number \n" +
                    "4. Update a Book\n" +
                    "5. Delete a Book");
            System.out.print("Enter your choice: ");

            int choice = scan.nextInt();
            switch (choice){
                case 1:
                    Book newBook = new Book();
                    System.out.print("Enter the title of the book: ");
                    String title = scan.next().trim();
                    System.out.print("\nEnter the author of the book: ");
                    String author = scan.next().trim();
                    System.out.print("\nEnter the ISBN of the book: ");
                    int isbn_number = 0;
                    do {
                        isbn_number = scan.nextInt();
                        check = book.validateIsbn(isbn_number);
                        if(check != true){
                            System.out.println("ISBN is already used. Please enter a different one.");
                            System.out.print("Enter the ISBN of the book: ");
                        }
                    }while (check != true);
                    newBook.setTitle(title);
                    newBook.setAuthor(author);
                    newBook.setIsbn_number(isbn_number);
                    book.createBook(newBook);

                    break;
                case 2:
                    book.showAllBooks();
                    break;
                case 0:
                    System.out.println("Exiting the program.");
                    System.exit(0); // This line exits the program with status code 0
                    break;
                default:
                    System.out.println("Enter a valid number");
            }
        }while (true);
    }
}
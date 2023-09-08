import Book.Book;
import Book.BookImp;
import lost_log.LostLogImp;

import java.util.List;
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
                    "3. Search \n" +
                    "4. Borrow a  Book\n" +
                    "5. Return a Book\n" +
                    "6. Show Borrowed Books\n" +
                    "7. delete a book\n" +
                    "8. update a book\n" +
                    "9. Check for lost books\n" +
                    "10. Show Stats\n" +
                    "0. Exit"
            );
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
                case 3:
                    System.out.print("Type the title or the author: ");
                    String searchInput = scan.next().trim();
                    book.searchBooksByTitleOrAuthor(searchInput);
                    break;
                case 4:
                    //borrow a book
                    System.out.print("Enter the ISBN of the book you want to borrow: ");
                    int isbn = scan.nextInt();
                    //book.checkBookExists(isbn);
                    book.borrowBook(isbn);
                    break;
                case 5:
                    // return a book
                    System.out.print("Enter the ISBN of the book you want to return: ");
                    int isbnReturn = scan.nextInt();
                    book.returnBook(isbnReturn);
                    break;
                case 6:
                    // show borrowed books
                    book.showBorrowedBooks();
                    break;
                case 7:
                    // delete a book
                    System.out.print("Enter the ISBN of the book you want to delete: ");
                    int isbnDelete = scan.nextInt();
                    book.deleteBook(isbnDelete);
                    break;
                case 8:
                    // update a book
                    System.out.print("Enter the ISBN of the book you want to update: ");
                    int isbnUpdate = scan.nextInt();
                    // check if the book exists
                    check = book.checkBookExists(isbnUpdate);
                    if(check != true){
                        System.out.println("Book does not exist.");
                    }else {
                        System.out.print("Enter the new title of the book: ");
                        String titleUpdate = scan.next().trim();
                        System.out.print("\nEnter the new author of the book: ");
                        String authorUpdate = scan.next().trim();
                        // create a new book object
                        Book updateBook = new Book();
                        updateBook.setTitle(titleUpdate);
                        updateBook.setAuthor(authorUpdate);
                        updateBook.setIsbn_number(isbnUpdate);
                        book.updateBook(updateBook);
                    }

                    break;
                case 9:
                    // declare a book as lost
                    // System.out.print("Enter the ISBN of the book you want to declare as lost: ");
                    //int isbnLost = scan.nextInt();
                    //book.lostBook(isbnLost);
                    List<Book> borrowedBooks = book.getLostBooks();
                    if(borrowedBooks.isEmpty()){
                        System.out.println("No books have been declared as lost.");
                    }else {
                        book.updateBooksStatusToLost(borrowedBooks);
                        System.out.println();
                    }

                    break;
                case 10:
                    // show stats
                    book.showStats();
                    LostLogImp lostLogImp = new LostLogImp();
                    lostLogImp.saveLostBooksToFile();
                    System.out.println();
                    System.out.println("Lost books have been saved to lost_books.txt");
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
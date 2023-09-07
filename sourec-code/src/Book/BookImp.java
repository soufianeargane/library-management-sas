package Book;
import database.DBConnection;
import Loan.Loan;
import Loan.LoanImp;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookImp {


    private int executeCountQuery(int parameterValue) {
        int count = 0;
        Connection con = DBConnection.createDBConnection();
        if (con != null) {
            try  {
                String query = "SELECT COUNT(*) FROM books WHERE isbn_number = ? AND deleted_at IS NULL";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, parameterValue);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }



    public void createBook(Book newBook) {
        Connection con = DBConnection.createDBConnection();
        if (con != null){
            String query = "INSERT INTO `books`(`title`, `author`, `isbn_number`) VALUES (?,?,?)";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, newBook.getTitle());
                preparedStatement.setString(2, newBook.getAuthor());
                preparedStatement.setInt(3, newBook.getIsbn_number());
                int count = preparedStatement.executeUpdate();
                if(count != 0){
                    System.out.println("Book Inserted Successfully");
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }

    public boolean validateIsbn(int isbn) {
        int count = executeCountQuery(isbn);
        if (count > 0){
            return false;
        }
        return true;
    }

    public void showAllBooks(){

        Connection con = DBConnection.createDBConnection();
        if(con != null){
            String query = "SELECT books.*, statuses.name AS status_name FROM books INNER JOIN statuses ON books.status_id = statuses.id" +
                    " WHERE books.deleted_at IS NULL AND statuses.id != 3";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                // if there are no data
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No books");
                }else{
                    System.out.println("Books: \n");
                    while (resultSet.next()) {
                        int ISBN = resultSet.getInt("isbn_number");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        String status = resultSet.getString("status_name");

                        // You can perform actions with the retrieved book data here
                        // For example, you can print the book details
                        System.out.println("Title: " + title);
                        System.out.println("Author: " + author);
                        System.out.println("ISBN Number: " + ISBN);
                        System.out.println("Status: " + status);
                        System.out.println();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }


    }

    public void searchBooksByTitleOrAuthor(String searchTerm) {
        Connection con = DBConnection.createDBConnection();

        if (con != null) {
            String query = "SELECT books.*, statuses.name AS status_name " +
                    "FROM books " +
                    "INNER JOIN statuses ON books.status_id = statuses.id " +
                    "WHERE books.title LIKE ? OR books.author LIKE ? " +"" +
                    "AND books.deleted_at IS NULL";

            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);

                preparedStatement.setString(1, "%" + searchTerm + "%");
                preparedStatement.setString(2, "%" + searchTerm + "%");

                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if there are no results
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No books found with the search term: " + searchTerm);
                } else {
                    // Iterate through the result set and process each matching book
                    while (resultSet.next()) {
                        int bookId = resultSet.getInt("id");
                        String title = resultSet.getString("title");
                        String author = resultSet.getString("author");
                        String status = resultSet.getString("status_name");

                        System.out.println("Book ID: " + bookId);
                        System.out.println("Title: " + title);
                        System.out.println("Author: " + author);
                        System.out.println("Status: " + status);
                        System.out.println();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // check if book exists
    public boolean checkBookExists(int isbn) {
        int count = executeCountQuery(isbn);
        if (count > 0){
            return true;
        }
        return false;
    }

    // borrow a book
    public void borrowBook(int isbn){
        // check if book exists
        boolean check = checkBookExists(isbn);
        if (check != true){
            System.out.println("Book does not exist");
        }else{
            // get status of the book
            Connection con = DBConnection.createDBConnection();
            if(con != null){
                String query = "SELECT status_id, id FROM books WHERE books.isbn_number = ?" + " AND books.deleted_at IS NULL";
                try {
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setInt(1, isbn);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        int status = resultSet.getInt("status_id");
                        if (status == 1){
                            Loan loan = new Loan();
                            LoanImp newLoan = new LoanImp();

                            System.out.println("Book is available");
                            Scanner scan = new Scanner(System.in);
                            System.out.print("Enter your name: ");
                            String name = scan.next().trim();
                            loan.setName(name);
                            System.out.print("Enter your phone number: ");
                            String phone = scan.next().trim();
                            loan.setPhone(phone);
                            // book id
                            loan.setBook_id(resultSet.getInt("id"));
                            // date is now
                            java.util.Date date = new java.util.Date();
                            loan.setDate(date);
                            newLoan.insertLoan(loan);

                            // update status of the book
                            String updateQuery = "UPDATE books SET status_id = 2 WHERE isbn_number = ?";
                            PreparedStatement updateStatement = con.prepareStatement(updateQuery);
                            updateStatement.setInt(1, isbn);
                            updateStatement.executeUpdate();
                            System.out.println("Book borrowed successfully");

                        }else{
                            System.out.println("Book is not available");
                        }
                    }
                    else {
                        System.out.println("Book does not exist");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    // return a book
    public void returnBook(int isbn){
        boolean check = checkBookExists(isbn);
        if (check != true){
            System.out.println("Book does not exist");
        }else{
            // get status of the book
            Connection con = DBConnection.createDBConnection();
            if(con != null){
                String query = "SELECT status_id, id FROM books WHERE books.isbn_number = ?";
                try {
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setInt(1, isbn);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        int status = resultSet.getInt("status_id");
                        if (status == 2){

                            Loan loan = new Loan();
                            LoanImp newLoan = new LoanImp();

                            // get id
                            int loanId = newLoan.getIdOfLoan(resultSet.getInt("id"));

                            if( loanId == 0){
                                System.out.printf("something is wrong");
                            }else {
                                // update status of the book
                                String updateQuery = "UPDATE books SET status_id = 1 WHERE isbn_number = ?";
                                PreparedStatement updateStatement = con.prepareStatement(updateQuery);
                                updateStatement.setInt(1, isbn);
                                updateStatement.executeUpdate();
                                // update loan
                                loan.setReturned(1);
                                loan.setId(loanId);
                                newLoan.updateLoan(loan);
                            }
                            System.out.println("Book returned successfully");

                        }else{
                            System.out.println("Book is not borrowed");
                        }
                    }
                    else {
                        System.out.println("Book does not exist");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    // show borrowed books
    public void showBorrowedBooks(){
        Connection con = DBConnection.createDBConnection();
        if(con != null){
            String query = "SELECT loans.*, books.title as book_title, books.author as b_author, books.isbn_number as b_isbn FROM loans INNER JOIN books on loans.book_id = books.id WHERE loans.returned = 0"
                    + " AND books.deleted_at IS NULL";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                // check if result set is empty
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No borrowed books");
                }else {
                    System.out.println("Borrowed books: \n");
                    while (resultSet.next()) {
                        System.out.println("Book Title: " + resultSet.getString("book_title"));
                        System.out.println("Book Author: " + resultSet.getString("b_author"));
                        System.out.println("Book ISBN: " + resultSet.getInt("b_isbn"));
                        System.out.println("Name of borrower: " + resultSet.getString("name"));
                        System.out.println("Phone of borrower : " + resultSet.getString("phone"));
                        System.out.printf("Date borrowed: %s\n", resultSet.getDate("date"));
                        System.out.printf("Time borrowed: %s\n", resultSet.getTime("date"));
                        System.out.println();
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    // delete a book
    public void deleteBook(int isbn){
        boolean check = checkBookExists(isbn);
        if (check != true){
            System.out.println("Book does not exist");
        }else{
            // get status of the book
            Connection con = DBConnection.createDBConnection();
            if(con != null){
                // set deleted_at to now
                java.util.Date date = new java.util.Date();
                String query = "UPDATE books SET deleted_at = ? WHERE isbn_number = ?";
                try {
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
                    preparedStatement.setInt(2, isbn);
                    preparedStatement.executeUpdate();
                    System.out.println("Book deleted successfully");
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateBook(Book updateBook){
        Connection con = DBConnection.createDBConnection();
        if (con != null){
            String query = "UPDATE `books` SET `title`=?,`author`=? WHERE `isbn_number`=?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, updateBook.getTitle());
                preparedStatement.setString(2, updateBook.getAuthor());
                preparedStatement.setInt(3, updateBook.getIsbn_number());
                int count = preparedStatement.executeUpdate();
                if(count != 0){
                    System.out.println("Book Updated Successfully");
                }else{
                    System.out.println("Something went wrong");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void lostBook (int isbn){

        boolean check = checkBookExists(isbn);
        if (check != true){
            System.out.println("Book does not exist");
        }else{
            // get status of the book
            Connection con = DBConnection.createDBConnection();
            if(con != null){
                // set deleted_at to now
                java.util.Date date = new java.util.Date();
                String query = "UPDATE books SET status_id = 3 WHERE isbn_number = ?";
                try {
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setInt(1, isbn);
                    preparedStatement.executeUpdate();
                    System.out.println("Book marked as lost successfully");
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

    public void showStats(){
        int available = countBooks(1);
        int borrowed = countBooks(2);
        int lost = countBooks(3);

        try {
            saveStatisticsToFile(available, borrowed, lost);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception here
        }

        System.out.println("");
        System.out.println("Available books: " + available);
        System.out.println("Borrowed books: " + borrowed);
        System.out.println("Lost books: " + lost);
        System.out.println("");

    }
    public int countBooks(int status){
        Connection con = DBConnection.createDBConnection();
        if(con != null){
            String query = "SELECT COUNT(*) FROM books WHERE status_id = ? AND deleted_at IS NULL";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, status);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void saveStatisticsToFile(int available, int borrowed, int lost) throws IOException {
        FileWriter writer = new FileWriter("book_statistics.txt");

        writer.write("Statistics:\n");
        writer.write("Available Books: " + available + "\n");
        writer.write("Borrowed Books: " + borrowed + "\n");
        writer.write("Lost Books: " + lost + "\n");

        writer.close();

        System.out.println("Statistics saved to 'book_statistics.txt'");
    }

    public List<Book> getLostBooks() {

        Connection connection = DBConnection.createDBConnection();
        List<Book> books = new ArrayList<>();

        if (connection != null) {
            try {
                // Construct the SQL query
                String query = "SELECT books.* FROM books INNER JOIN loans ON books.id = loans.book_id WHERE loans.date <= DATE_SUB(NOW(), INTERVAL 5 MINUTE) AND loans.returned = 0";

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Process the results and add them to the books list
                while (resultSet.next()) {
                    int bookId = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    int isbn = resultSet.getInt("isbn_number");
                    // Add more book attributes as needed

                    // Create a Book object and add it to the list
                    Book book = new Book();
                    book.setId(bookId);
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setIsbn_number(isbn);
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return books;
    }

    public void updateBooksStatusToLost(List<Book> books) {

        Connection connection = DBConnection.createDBConnection();

        if (connection != null) {
            try {
                // Construct the SQL query
                String query = "UPDATE books SET status_id = 3 WHERE id IN (";
                for (int i = 0; i < books.size(); i++) {
                    if (i > 0) {
                        query += ", ";
                    }
                    query += books.get(i).getId();
                }
                query += ")";

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Execute the query
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}




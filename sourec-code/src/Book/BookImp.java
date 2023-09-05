package Book;
import database.DBConnection;
import Loan.Loan;
import Loan.LoanImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BookImp {


    private int executeCountQuery(int parameterValue) {
        int count = 0;
        Connection con = DBConnection.createDBConnection();
        if (con != null) {
            try  {
                String query = "SELECT COUNT(*) FROM books WHERE isbn_number = ?";
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
            String query = "SELECT books.*, statuses.name AS status_name FROM books INNER JOIN statuses ON books.status_id = statuses.id;";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

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
                    "WHERE books.title LIKE ? OR books.author LIKE ?;";

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

                        // You can perform actions with the retrieved book data here
                        // For example, you can print the book details
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
                String query = "SELECT status_id, id FROM books WHERE books.isbn_number = ?";
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
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }


}

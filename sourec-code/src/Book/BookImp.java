package Book;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookImp {
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
            }finally {
                try {
                    if (con != null && !con.isClosed()) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean validateIsbn(int isbn) {
        Connection con = DBConnection.createDBConnection();
        if (con != null) {
            try {
                String query = "SELECT COUNT(*) FROM books WHERE isbn_number = ?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, isbn);
                // Execute the query and retrieve the result
                ResultSet resultSet = preparedStatement.executeQuery();
                // Check the count
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count > 0){
                        return false;
                    }
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (con != null && !con.isClosed()) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false; // Return false in case of any error or if connection is not established
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

}

package lost_log;

import Book.Book;
import database.DBConnection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LostLogImp {
    // get all lost book

    private List<LostLog> getLostBooks() {
        Connection connection = DBConnection.createDBConnection();
        List<LostLog> lost_books = new ArrayList<>();
        try {
            String query = "SELECT * FROM lost_log";
            java.sql.PreparedStatement statement = connection.prepareStatement(query);
            java.sql.ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LostLog lost_book = new LostLog();
                lost_book.setLog_id(resultSet.getInt("log_id"));
                lost_book.setBook_title(resultSet.getString("book_title"));
                lost_book.setBook_author(resultSet.getString("book_author"));
                lost_book.setBook_isbn(resultSet.getInt("book_isbn"));
                lost_book.setBorrower_name(resultSet.getString("borrower_name"));
                lost_book.setBorrower_phone(resultSet.getString("borrower_phone"));
                lost_book.setLog_timestamp(resultSet.getDate("log_timestamp"));
                lost_books.add(lost_book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lost_books;
    }

    // save lost books to file.txt

    public void saveLostBooksToFile() {
        List<LostLog> lost_books = getLostBooks();
        try {
            java.io.FileWriter fileWriter = new java.io.FileWriter("lost_books.txt");
            for (LostLog lost_book : lost_books) {
                fileWriter.write("Lost Books are:\n");
                fileWriter.write("\n");
                fileWriter.write("Book ISBN: " + lost_book.getBook_isbn() + "\n");
                fileWriter.write("Book Title: " + lost_book.getBook_title() + "\n");
                fileWriter.write("Book Author: " + lost_book.getBook_author() + "\n");
                fileWriter.write("Borrower Name: " + lost_book.getBorrower_name() + "\n");
                fileWriter.write("Borrower Phone: " + lost_book.getBorrower_phone() + "\n");
                fileWriter.write("Log Timestamp: " + lost_book.getLog_timestamp() + "\n");
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

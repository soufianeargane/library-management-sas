package Loan;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LoanImp {
    public void insertLoan(Loan loan) {

        Connection con = DBConnection.createDBConnection();
        if (con != null) {
            String query = "INSERT INTO `loans`(`name`, `phone`, `book_id`, `date`) VALUES (?,?,?,?)";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, loan.getName());
                preparedStatement.setString(2, loan.getPhone());
                preparedStatement.setInt(3, loan.getBook_id());
                preparedStatement.setDate(4, new java.sql.Date(loan.getDate().getTime()));
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package Loan;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public void updateLoan(Loan loan){
        Connection con = DBConnection.createDBConnection();
        if (con != null) {
            String query = "UPDATE `loans` SET `returned`=? WHERE `id`=?";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, loan.getReturned());
                preparedStatement.setInt(2, loan.getId());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // get id
    public int getIdOfLoan(int bookId){
        Connection con = DBConnection.createDBConnection();
        if (con != null) {
            String query = "SELECT id FROM loans WHERE book_id = ? ORDER BY id DESC LIMIT 1";
            try {
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, bookId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;

    }

}

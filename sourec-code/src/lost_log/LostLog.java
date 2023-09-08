package lost_log;

import java.sql.Date;

public class LostLog {

    private int log_id;
    private String book_title;
    private String book_author;
    private int book_isbn;
    private String borrower_name;
    private String borrower_phone;
    private Date log_timestamp;

    public int getLog_id() {
        return log_id;
    }

    public int getBook_isbn() {
        return book_isbn;
    }

    public void setBook_isbn(int book_isbn) {
        this.book_isbn = book_isbn;
    }

    public String getBorrower_name() {
        return borrower_name;
    }

    public void setBorrower_name(String borrower_name) {
        this.borrower_name = borrower_name;
    }

    public String getBorrower_phone() {
        return borrower_phone;
    }

    public void setBorrower_phone(String borrower_phone) {
        this.borrower_phone = borrower_phone;
    }

    public Date getLog_timestamp() {
        return log_timestamp;
    }

    public void setLog_timestamp(Date log_timestamp) {
        this.log_timestamp = log_timestamp;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }
}

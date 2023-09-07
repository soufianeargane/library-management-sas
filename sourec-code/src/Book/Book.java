package Book;

import java.sql.Date;

public class Book {
    private int id;
    private String title;
    private String author;
    private int isbn_number;
    private int status_id;
    private Date deleted_at;



    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsbn_number() {
        return isbn_number;
    }

    public void setIsbn_number(int isbn_number) {
        this.isbn_number = isbn_number;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}

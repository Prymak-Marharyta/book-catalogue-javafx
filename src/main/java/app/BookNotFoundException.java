package app;

public class BookNotFoundException extends Exception {
    public BookNotFoundException(String title) {
        super("Публiкацiю з назвою \"" + title + "\" не знайдено.");
    }
}
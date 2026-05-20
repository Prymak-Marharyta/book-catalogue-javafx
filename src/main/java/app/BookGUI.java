package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BookGUI extends Application {
    private Catalogue catalogue = new Catalogue();

    private TextField titleField;
    private TextField authorField;
    private TextField publisherField;
    private TextField genreField;
    private TextField yearField;
    private TextArea outputArea;

    private final String FILE_NAME = "catalogue.dat";

    @Override
    public void start(Stage stage) {
        titleField = new TextField();
        authorField = new TextField();
        publisherField = new TextField();
        genreField = new TextField();
        yearField = new TextField();

        Button addButton = new Button("Додати книгу");
        Button removeButton = new Button("Видалити книгу");
        Button updateButton = new Button("Оновити книгу");
        Button saveButton = new Button("Зберегти у файл");
        Button loadButton = new Button("Завантажити з файлу");
        Button searchButton = new Button("Пошук");
        Button resetButton = new Button("Скинути параметри пошуку");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(250);

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Назва:"), 0, 0);
        form.add(titleField, 1, 0);

        form.add(new Label("Автор:"), 0, 1);
        form.add(authorField, 1, 1);

        form.add(new Label("Видавництво:"), 0, 2);
        form.add(publisherField, 1, 2);

        form.add(new Label("Жанр:"), 0, 3);
        form.add(genreField, 1, 3);

        form.add(new Label("Рiк:"), 0, 4);
        form.add(yearField, 1, 4);

        GridPane buttons = new GridPane();
        buttons.setHgap(10);
        buttons.setVgap(10);

        buttons.add(addButton, 0, 0);
        buttons.add(removeButton, 1, 0);
        buttons.add(updateButton, 2, 0);
        buttons.add(saveButton, 0, 1);
        buttons.add(loadButton, 1, 1);
        buttons.add(searchButton, 2, 1);
        buttons.add(resetButton, 0, 2);

        VBox root = new VBox(10, form, buttons, outputArea);
        root.setPadding(new Insets(10));

        addButton.setOnAction(e -> addBook());
        removeButton.setOnAction(e -> removeBook());
        updateButton.setOnAction(e -> updateBook());
        saveButton.setOnAction(e -> saveCatalogue());
        loadButton.setOnAction(e -> loadCatalogue());
        searchButton.setOnAction(e -> searchBook());
        resetButton.setOnAction(e -> showAllBooks());

        Scene scene = new Scene(root, 750, 500);
        stage.setTitle("Каталог книг");
        stage.setScene(scene);
        stage.show();

        showAllBooks();
    }

    private void addBook() {
        try {
            Book book = readBookFromFields();
            catalogue.addPublication(book);
            showMessage("Книгу додано.");
            clearFields();
            showAllBooks();
        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage());
        }
    }

    private void removeBook() {
        try {
            String title = titleField.getText().trim();

            if (title.isEmpty()) {
                throw new IllegalArgumentException("Введiть назву книги для видалення.");
            }

            catalogue.removePublicationByTitle(title);
            showMessage("Книгу видалено.");
            clearFields();
            showAllBooks();
        } catch (BookNotFoundException | IllegalArgumentException ex) {
            showMessage(ex.getMessage());
        }
    }

    private void updateBook() {
        try {
            String oldTitle = titleField.getText().trim();

            if (oldTitle.isEmpty()) {
                throw new IllegalArgumentException("Введiть назву книги для оновлення.");
            }

            Book updatedBook = readBookFromFields();
            catalogue.updateBook(oldTitle, updatedBook);

            showMessage("Данi книги оновлено.");
            clearFields();
            showAllBooks();
        } catch (BookNotFoundException | IllegalArgumentException ex) {
            showMessage(ex.getMessage());
        }
    }

    private void saveCatalogue() {
        try {
            catalogue.saveToFile(FILE_NAME);
            showMessage("Каталог збережено у файл.");
        } catch (Exception ex) {
            showMessage("Помилка збереження файлу: " + ex.getMessage());
        }
    }

    private void loadCatalogue() {
        try {
            catalogue.loadFromFile(FILE_NAME);
            showMessage("Каталог завантажено з файлу.");
            showAllBooks();
        } catch (Exception ex) {
            showMessage("Помилка завантаження файлу: " + ex.getMessage());
        }
    }

    private void searchBook() {
        String title = titleField.getText().trim();

        if (title.isEmpty()) {
            showMessage("Введiть назву або частину назви для пошуку.");
            return;
        }

        ArrayList<Publication> result = catalogue.findPublicationsByPartialTitle(title);

        if (result.isEmpty()) {
            showMessage("За заданим запитом нiчого не знайдено.");
        } else {
            showPublications(result);
        }
    }

    private Book readBookFromFields() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String publisher = publisherField.getText().trim();
        String genre = genreField.getText().trim();
        String yearText = yearField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || genre.isEmpty() || yearText.isEmpty()) {
            throw new IllegalArgumentException("Усi поля мають бути заповненi.");
        }

        int year;

        try {
            year = Integer.parseInt(yearText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Рiк видання має бути числом.");
        }

        if (year <= 0 || year > 2026) {
            throw new IllegalArgumentException("Рiк видання введено некоректно.");
        }

        return new Book(title, year, author, publisher, genre);
    }

    private void showAllBooks() {
        showPublications(catalogue.getAllPublications());
    }

    private void showPublications(ArrayList<Publication> publications) {
        outputArea.clear();

        if (publications.isEmpty()) {
            outputArea.setText("Каталог порожнiй.");
            return;
        }

        for (Publication p : publications) {
            outputArea.appendText(p.toString() + "\n");
        }
    }

    private void showMessage(String message) {
        outputArea.setText(message + "\n\n");

        for (Publication p : catalogue.getAllPublications()) {
            outputArea.appendText(p.toString() + "\n");
        }
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        publisherField.clear();
        genreField.clear();
        yearField.clear();
    }
}
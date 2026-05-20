package app;

import java.io.*;
import java.util.ArrayList;

public class Catalogue {
    private ArrayList<Publication> publications;

    public Catalogue() {
        publications = new ArrayList<>();
    }

    public void addPublication(Publication p) {
        publications.add(p);
    }

    public void removePublicationByTitle(String title) throws BookNotFoundException {
        Publication publication = findPublicationByTitle(title);
        publications.remove(publication);
    }

    public Publication findPublicationByTitle(String title) throws BookNotFoundException {
        for (Publication p : publications) {
            if (p.getTitle().equalsIgnoreCase(title)) {
                return p;
            }
        }
        throw new BookNotFoundException(title);
    }

    public ArrayList<Publication> findPublicationsByPartialTitle(String title) {
        ArrayList<Publication> result = new ArrayList<>();

        for (Publication p : publications) {
            if (p.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(p);
            }
        }

        return result;
    }

    public ArrayList<Publication> getAllPublications() {
        return publications;
    }

    public void updateBook(String title, Book updatedBook) throws BookNotFoundException {
        for (int i = 0; i < publications.size(); i++) {
            if (publications.get(i).getTitle().equalsIgnoreCase(title)) {
                publications.set(i, updatedBook);
                return;
            }
        }

        throw new BookNotFoundException(title);
    }

    public void saveToFile(String filename) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename));
        outputStream.writeObject(publications);
        outputStream.close();
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
        publications = (ArrayList<Publication>) inputStream.readObject();
        inputStream.close();
    }
}
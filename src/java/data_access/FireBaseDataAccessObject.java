package data_access;

import entities.Account;
import entities.PDFFile;

import java.io.IOException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;
import com.google.auth.oauth2.GoogleCredentials;

public class FireBaseDataAccessObject implements UserDataAccessInterface, FileDataAccessInterface{

    public void initializeFireBase() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("https://JSIT.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
        System.out.println("Firebase App initialised.");
        System.out.println(FirebaseApp.getInstance());

    }

    @Override
    public void saveFile(PDFFile file) {

    }

    @Override
    public PDFFile getFile(String path) {
        return null;
    }

    @Override
    public void saveUser(Account account) {

    }

    @Override
    public Account getUserByEmail(String email) {
        return null;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return false;
    }

//  For testing initialization.
    public static void main(String[] args) {
        FireBaseDataAccessObject fireBaseDataAccessObject = new FireBaseDataAccessObject();
        try {
            fireBaseDataAccessObject.initializeFireBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
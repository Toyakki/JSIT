package entities;

import java.util.ArrayList;

public class AccountFactory {
    private AccountFactory() {

    }

    public static Account createAccount(String email, String password, String type){
        return new Account(email, password, type, new ArrayList<>());
    }
}

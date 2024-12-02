package data_access;

import entities.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserDataAccessObject implements UserDataAccessInterface {
    private final Map<String, Account> users = new HashMap<>();

    public boolean userExistsByEmail(String email) {
        return users.containsKey(email);
    }

    public Account getUserByEmail(String email) {
        return users.get(email);
    }

    public void saveUser(Account account) {
        users.put(account.getEmail(), account);
    }

    public List<Account> getAllUsers(){
        return List.copyOf(users.values());
    }
}

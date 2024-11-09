package data_access;

import entities.Account;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDataAccessObject {
    private final Map<String, Account> users = new HashMap<>();

    public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }

    public Account getByEmail(String email) {
        return users.get(email);
    }

    public void save(Account account) {
        users.put(account.getEmail(), account);
    }
}

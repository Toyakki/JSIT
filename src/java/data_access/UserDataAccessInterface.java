package data_access;

import entities.Account;

import java.util.List;

public interface UserDataAccessInterface {
    void saveUser(Account account);
    Account getUserByEmail(String email);
    boolean userExistsByEmail(String email);
    List<Account> getAllUsers();
}

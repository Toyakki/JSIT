package data_access;

import entities.Account;

public interface UserDataAccessInterface {
    public void saveUser(Account account);
    public Account getUserByEmail(String email);
    public boolean userExistsByEmail(String email);
}

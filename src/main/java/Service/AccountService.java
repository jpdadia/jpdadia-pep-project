package Service;
import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;


public class AccountService{


    public static Account createNewAccount(Account account){
        return AccountDAO.createNewAccount(account);
    }

    public static boolean duplicateUsername(String username){
        return AccountDAO.duplicateUsername(username);
    }

    public static Account logIn(String username, String password){
        return AccountDAO.userLogin(username, password);
    }

    public static boolean accountExist(int accountId){
        return AccountDAO.accountExist(accountId);
    }
}

package Service;

import Model.Account;
import DAO.AccountDAO;

import static org.mockito.ArgumentMatchers.isNotNull;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        Account duplicateAccount = this.accountDAO.checkAccount(account.getUsername(),account.getPassword());

        if(duplicateAccount != null){
            if (duplicateAccount.getUsername() == account.getPassword() && duplicateAccount.getPassword() == account.getPassword())
                return null;
        }

        if(account.getUsername() != null && !account.getUsername().trim().isEmpty() && account.getPassword().length() >= 4)
            return this.accountDAO.insertAccount(account);
        
        return null;
    }

    public Account verifyAccount(String username, String password) {
        return this.accountDAO.checkAccount(username, password);
    }
}

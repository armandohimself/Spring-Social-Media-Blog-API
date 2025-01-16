package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
@Transactional
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> findAccountByUsername(Account account) {
        String username = account.getUsername();
        Optional<Account> accountOptional = accountRepository.findAccountByUsername(username);
        return accountOptional;
    }

    public Account registerAccount(Account account) {
        // Registration Successful Only If:
        // - username is not blank && password is at least 4 characters long
        if(!account.getUsername().isBlank() && account.getPassword().length() >= 4) {
            // Already checking for duplicate usernames in SocialMediaController
            Account savedAccount = accountRepository.save(account);
            return savedAccount;
        }
        return null;
    }

    public Account loginAccount(Account account) {
        // Login Successful Only If: 
        // - username & password == real account in db (custom query)
        String username = account.getUsername();
        String password = account.getPassword();
        Optional<Account> accountOptional = accountRepository.findAccountByUsernameAndPassword(username, password);

        if(accountOptional.isPresent()) {
            Account verifiedAccount = accountOptional.get();

            if(username.equals(verifiedAccount.getUsername()) && password.equals(verifiedAccount.getPassword())) {
                return verifiedAccount;
            }
        }
        return null;
    }
}

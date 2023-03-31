package com.mangobyte.accountservice.form;

import com.mangobyte.accountservice.exception.CustomException;
import com.mangobyte.accountservice.model.Account;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Optional;

public record AccountUpdateForm(Optional<@Size(min = 3, max = 30) String> username,
                                Optional<@Size(min =3, max = 125) @Email String> email) {
    public Account getUpdatedAccount(Account account) {
        Account updatedAccount = account.toBuilder().build();
        if (!username.isPresent() && !email.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "username or email is required");
        }
        if (username.isPresent() && !account.getUsername().equals(username.get())) {
            updatedAccount.setUsername(username.get());
        }
        if (email.isPresent() && !account.getEmail().equals(email.get())) {
            updatedAccount.setEmail(email.get());
        }
        return updatedAccount;
    }
}

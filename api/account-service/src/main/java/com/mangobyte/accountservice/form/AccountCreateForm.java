package com.mangobyte.accountservice.form;

import com.mangobyte.accountservice.model.Account;
import com.mangobyte.accountservice.model.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record AccountCreateForm(@NotNull @Size(min = 3, max = 30) String username,
                                @NotNull @Size(min =3, max = 125) @Email String email,
                                @NotNull @Size(min = 3, max = 30) String password) {
    public Account toAccountUser() {
        Account account = Account.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(Role.USER)
                .build();
        return account;
    }
}

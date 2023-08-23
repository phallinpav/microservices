package com.mangobyte.accountservice.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record EmailVerifyForm(@NotNull @Size(min =3, max = 125) @Email String email) {

}

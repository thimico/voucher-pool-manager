package br.com.getnet.voucher_pool_manager.model;

/**
 * @author Thiago Oliveira</a>
 */

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DestinatarioRequest {

    @NotNull(message = "O campo nome não pode ser nulo.")
    @NotEmpty(message = "O campo nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O campo email não pode ser nulo.")
    @NotEmpty(message = "O campo email não pode ser vazio.")
    @Email(message = "O campo email deve conter um endereço de email válido.")
    private String email;
}

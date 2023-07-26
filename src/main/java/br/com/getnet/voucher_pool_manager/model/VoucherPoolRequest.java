package br.com.getnet.voucher_pool_manager.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
/**
 * @author Thiago Oliveira</a>
 */

@Getter
@Setter
public class VoucherPoolRequest {

    @NotNull(message = "O id da oferta especial não pode ser nulo")
    private String ofertaEspecialId;

    @NotEmpty(message = "Lista de destinatários não pode ser vazia")
    private List<@Email(message = "Email deve ser válido") String> destinatariosEmails;

    @NotNull(message = "Data de validade não pode ser nula")
//    @Future(message = "Data de validade deve ser uma data futura")
    private LocalDate validade;

}

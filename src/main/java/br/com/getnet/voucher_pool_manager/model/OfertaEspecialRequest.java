package br.com.getnet.voucher_pool_manager.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Thiago Oliveira</a>
 */

@Getter
@Setter
public class OfertaEspecialRequest {

    @NotNull(message = "O campo nome não pode ser nulo.")
    @NotEmpty(message = "O campo nome não pode ser vazio.")
    private String nome;

    @NotNull(message = "O campo desconto não pode ser nulo.")
    private Integer desconto;
}

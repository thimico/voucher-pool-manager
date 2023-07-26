package br.com.getnet.voucher_pool_manager.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @author Thiago Oliveira</a>
 */

@Getter
@Setter
@NoArgsConstructor
public class VoucherRequest {

    @Email(message = "O email deve ser válido.")
    @NotEmpty(message = "O email do destinatário não pode ser vazio.")
    @Length(min = 1, max = 100, message = "O email do destinatário deve ter entre 1 e 100 caracteres.")
    private String destinatarioEmail;

    @NotNull(message = "O id da oferta especial não pode ser nulo")
    private String ofertaEspecialId;

//    @NotNull(message = "O desconto da oferta especial não pode ser nulo.")
//    @Range(min = 0, max = 100, message = "O desconto da oferta especial deve estar entre 0 e 100.")
//    private Integer ofertaEspecialDesconto;

    @Pattern(regexp = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)(\\d{4})$",
            message = "A data de validade do voucher deve estar no formato DD/MM/YYYY")
    private String validade;

}

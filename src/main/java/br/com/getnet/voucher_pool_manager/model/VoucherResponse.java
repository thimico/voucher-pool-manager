package br.com.getnet.voucher_pool_manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * @author Thiago Oliveira</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {

    private String id;
    private DestinatarioResponse destinatario;
    private OfertaEspecialResponse ofertaEspecial;
    private String codigo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate validade;

    private VoucherStatus status;
}
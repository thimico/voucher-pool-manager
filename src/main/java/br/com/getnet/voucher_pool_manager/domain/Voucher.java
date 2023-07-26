package br.com.getnet.voucher_pool_manager.domain;

import br.com.getnet.voucher_pool_manager.model.VoucherStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Date;


@Document
@Getter
@Setter
public class Voucher extends BaseEntity {

    @DBRef
    private Destinatario destinatario;

    @DBRef
    private OfertaEspecial ofertaEspecial;

    @Indexed(unique = true)
    private String codigo;

    private LocalDate validade;

    private Date dataUso;

    @Field("status")
    private VoucherStatus status;

}

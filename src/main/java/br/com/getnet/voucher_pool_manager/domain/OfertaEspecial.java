package br.com.getnet.voucher_pool_manager.domain;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Thiago Oliveira</a>
 */

@Document
@Getter
@Setter
public class OfertaEspecial extends BaseEntity {


    @Size(max = 255)
    private String nome;

    private Integer desconto;
}

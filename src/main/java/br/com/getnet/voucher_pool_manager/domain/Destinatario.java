package br.com.getnet.voucher_pool_manager.domain;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Getter
@Setter
public class Destinatario extends BaseEntity {


    @Size(max = 255)
    private String nome;

    @Indexed(unique = true)
    @Size(max = 255)
    private String email;

}

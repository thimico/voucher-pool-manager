package br.com.getnet.voucher_pool_manager.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document
@Getter
@Setter
@NoArgsConstructor
public abstract class BaseEntity {

    @Id
    private String id = UUID.randomUUID().toString();

    @CreatedDate
    private Date criadoEm;

    @LastModifiedDate
    private Date atualizadoEm;
}

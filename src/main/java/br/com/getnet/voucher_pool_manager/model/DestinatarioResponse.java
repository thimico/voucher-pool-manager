package br.com.getnet.voucher_pool_manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Thiago Oliveira</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinatarioResponse {
    private String id;
    private String nome;
    private String email;
}
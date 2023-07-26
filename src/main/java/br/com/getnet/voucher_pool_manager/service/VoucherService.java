package br.com.getnet.voucher_pool_manager.service;

import br.com.getnet.voucher_pool_manager.domain.Voucher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Thiago Oliveira</a>
 */
public interface VoucherService {

    @Transactional
    Voucher gerarVoucherParaDestinatario(String email, String ofertaEspecialId, LocalDate validade);

    List<Voucher> gerarVouchersParaTodosDestinatarios(String ofertaEspecialId, List<String> destinatariosEmails, LocalDate validade);

    @Transactional
    Integer usarVoucher(String codigo, String email);

}

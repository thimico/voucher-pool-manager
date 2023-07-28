package br.com.getnet.voucher_pool_manager.repos;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.domain.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface VoucherRepository extends MongoRepository<Voucher, String> {

    List<Voucher> findByDestinatarioAndDataUsoIsNull(Destinatario destinatario);
    Optional<Voucher> findByCodigoAndDestinatarioAndDataUsoIsNull(String codigo, Destinatario destinatario);

    List<Voucher> findByDestinatarioAndValidadeAfter(Destinatario destinatario, LocalDate now);

}

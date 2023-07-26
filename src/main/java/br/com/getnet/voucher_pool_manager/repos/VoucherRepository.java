package br.com.getnet.voucher_pool_manager.repos;

import br.com.getnet.voucher_pool_manager.domain.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface VoucherRepository extends MongoRepository<Voucher, String> {

    List<Voucher> findByDestinatarioEmailAndDataUsoIsNull(String email);
    Optional<Voucher> findByCodigoAndDestinatarioEmailAndDataUsoIsNull(String codigo, String email);


}

package br.com.getnet.voucher_pool_manager.repos;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface DestinatarioRepository extends MongoRepository<Destinatario, String> {

    Optional<Destinatario> findByEmail(String email);

}

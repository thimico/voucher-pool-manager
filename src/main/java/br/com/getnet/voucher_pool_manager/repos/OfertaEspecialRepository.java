package br.com.getnet.voucher_pool_manager.repos;

import br.com.getnet.voucher_pool_manager.domain.OfertaEspecial;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface OfertaEspecialRepository extends MongoRepository<OfertaEspecial, String> {

    OfertaEspecial findByNome(String nome);

    OfertaEspecial save(OfertaEspecial ofertaEspecial);
}

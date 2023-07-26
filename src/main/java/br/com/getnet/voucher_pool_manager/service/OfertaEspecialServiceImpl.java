package br.com.getnet.voucher_pool_manager.service;

import br.com.getnet.voucher_pool_manager.domain.OfertaEspecial;
import br.com.getnet.voucher_pool_manager.repos.OfertaEspecialRepository;
import br.com.getnet.voucher_pool_manager.repos.OfertaEspecialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Thiago Oliveira</a>
 */
@Service
public class OfertaEspecialServiceImpl implements OfertaEspecialService {

    private final OfertaEspecialRepository ofertaEspecialRepository;

    public OfertaEspecialServiceImpl(OfertaEspecialRepository ofertaEspecialRepository) {
        this.ofertaEspecialRepository = ofertaEspecialRepository;
    }

    @Override
    @Transactional
    public OfertaEspecial criarOfertaEspecial(OfertaEspecial ofertaEspecial) {
        return this.ofertaEspecialRepository.save(ofertaEspecial);
    }
}

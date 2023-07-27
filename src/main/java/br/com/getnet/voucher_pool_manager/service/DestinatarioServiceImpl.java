package br.com.getnet.voucher_pool_manager.service;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.repos.DestinatarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Thiago Oliveira</a>
 */
@Service
public class DestinatarioServiceImpl implements DestinatarioService {

    private final DestinatarioRepository destinatarioRepository;

    public DestinatarioServiceImpl(DestinatarioRepository destinatarioRepository) {
        this.destinatarioRepository = destinatarioRepository;
    }

    public Destinatario criarDestinatario(Destinatario destinatario) throws ValidationException {
        Optional<Destinatario> existingDestinatario = destinatarioRepository.findByEmail(destinatario.getEmail());
        if(existingDestinatario.isPresent()) {
            throw new ValidationException("Destinatario com email " + destinatario.getEmail() + " ja existe");
        }
        return destinatarioRepository.save(destinatario);
    }


}

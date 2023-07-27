package br.com.getnet.voucher_pool_manager.service;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.domain.OfertaEspecial;
import br.com.getnet.voucher_pool_manager.domain.Voucher;
import br.com.getnet.voucher_pool_manager.model.VoucherStatus;
import br.com.getnet.voucher_pool_manager.repos.DestinatarioRepository;
import br.com.getnet.voucher_pool_manager.repos.OfertaEspecialRepository;
import br.com.getnet.voucher_pool_manager.repos.VoucherRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Thiago Oliveira</a>
 */
@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private DestinatarioRepository destinatarioRepository;
    @Autowired
    private OfertaEspecialRepository ofertaEspecialRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository, DestinatarioRepository destinatarioRepository, OfertaEspecialRepository ofertaEspecialRepository) {
        this.voucherRepository = voucherRepository;
        this.destinatarioRepository = destinatarioRepository;
        this.ofertaEspecialRepository = ofertaEspecialRepository;
    }
    // ...

    @Override
    @Transactional
    public Voucher gerarVoucherParaDestinatario(String email, String ofertaEspecialId, LocalDate validade) {
        Optional<Destinatario> destinatarioOptional = destinatarioRepository.findByEmail(email);
        Optional<OfertaEspecial> ofertaEspecialOpt = ofertaEspecialRepository.findById(ofertaEspecialId);

        if (!destinatarioOptional.isPresent()) {
            throw new EntityNotFoundException("Destinatário com email " + email + " não encontrado.");
        }

        if (!ofertaEspecialOpt.isPresent()) {
            throw new EntityNotFoundException("Oferta especial não encontrada com o id: " + ofertaEspecialId);
        }

        Destinatario destinatario = destinatarioOptional.get();
        // Continue com a criação do voucher como antes, agora sabendo que o destinatário existe.
        Voucher novoVoucher = new Voucher();
        novoVoucher.setDestinatario(destinatario);
        novoVoucher.setOfertaEspecial(ofertaEspecialOpt.get());
        novoVoucher.setCodigo(gerarCodigoVoucher());
        novoVoucher.setValidade(validade);
        novoVoucher.setStatus(VoucherStatus.ATV);

        return voucherRepository.save(novoVoucher);
    }


    @Override
    @Transactional
    public List<Voucher> gerarVouchersParaTodosDestinatarios(String ofertaEspecialId, List<String> destinatariosEmails, LocalDate validade) {
        Optional<OfertaEspecial> ofertaEspecial = ofertaEspecialRepository.findById(ofertaEspecialId);
        if (!ofertaEspecial.isPresent()) {
            throw new EntityNotFoundException("Oferta especial não encontrada com o id: " + ofertaEspecialId);
        }


        List<Optional<Destinatario>> destinatariosOptionals = destinatariosEmails.stream()
                .map(email -> destinatarioRepository.findByEmail(email))
                .collect(Collectors.toList());

        boolean anyNotFound = destinatariosOptionals.stream().anyMatch(Optional::isEmpty);

        if (anyNotFound) {
            throw new EntityNotFoundException("Um ou mais destinatários não foram encontrados com os e-mails fornecidos.");
        }

        List<Destinatario> destinatarios = destinatariosOptionals.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        try {
                List<Voucher> vouchers = destinatarios.stream()
                        .map(destinatario -> {
                            Voucher voucher = new Voucher();
                            voucher.setOfertaEspecial(ofertaEspecial.get());
                            voucher.setDestinatario(destinatario);
                            voucher.setCodigo(gerarCodigoVoucher());
                            voucher.setValidade(validade);
                            voucher.setStatus(VoucherStatus.ATV);
                            return voucherRepository.save(voucher);
                        })
                        .collect(Collectors.toList());

            return vouchers;
        } catch (RuntimeException e) {
            // roolback
            throw e;
        }

    }

    private String gerarCodigoVoucher() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }


    @Override
    @Transactional
    public Integer usarVoucher(String codigo, String email) {
        Optional<Destinatario> destinatarioOptional = destinatarioRepository.findByEmail(email);

        if (!destinatarioOptional.isPresent()) {
            throw new EntityNotFoundException("Destinatário com email " + email + " não encontrado.");
        }
        Optional<Voucher> optionalVoucher = voucherRepository.findByCodigoAndDestinatarioAndDataUsoIsNull(codigo, destinatarioOptional.get());

        if (!optionalVoucher.isPresent()) {
            throw new ValidationException("Voucher inválido");
        }

        Voucher voucher = optionalVoucher.get();
        if (VoucherStatus.ATV.equals(voucher.getStatus()) && voucher.getValidade().isAfter(LocalDate.now())) {
            voucher.setStatus(VoucherStatus.USD);
            voucher.setDataUso(new Date());
            voucherRepository.save(voucher);
            return voucher.getOfertaEspecial().getDesconto();
        } else {
            throw new RuntimeException("Voucher inválido");
        }
    }

}

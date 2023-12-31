package br.com.getnet.voucher_pool_manager.service;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.domain.OfertaEspecial;
import br.com.getnet.voucher_pool_manager.domain.Voucher;
import br.com.getnet.voucher_pool_manager.model.VoucherStatus;
import br.com.getnet.voucher_pool_manager.repos.DestinatarioRepository;
import br.com.getnet.voucher_pool_manager.repos.OfertaEspecialRepository;
import br.com.getnet.voucher_pool_manager.repos.VoucherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests {@link VoucherServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
public class VoucherServiceImplTest {

    @Mock
    private VoucherRepository voucherRepository;
    @Mock
    private DestinatarioRepository destinatarioRepository;
    @Mock
    private OfertaEspecialRepository ofertaEspecialRepository;

    private VoucherServiceImpl unitUnderTest;

    private static EasyRandom easyRandom;

    @BeforeAll
    private static void beforeTests() {
        easyRandom = new EasyRandom();
    }

    @BeforeEach
    public void init() {
        unitUnderTest = new VoucherServiceImpl(voucherRepository, destinatarioRepository, ofertaEspecialRepository);
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GerarVoucherParaDestinatario {

        @Test
        void exceptionDestinatarioNaoEncontrado() {
            String email = "test@test.com";
            String ofertaEspecialId = "off1";
            LocalDate validade = LocalDate.now().plusDays(1);

            when(destinatarioRepository.findByEmail(email)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> unitUnderTest.gerarVoucherParaDestinatario(email, ofertaEspecialId, validade))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format("Destinatário com email %s não encontrado.", email));
        }

        @Test
        void exceptionOfertaEspecialNaoEncontrada() {
            String email = "test@test.com";
            String ofertaEspecialId = "off1";
            LocalDate validade = LocalDate.now().plusDays(1);

            when(destinatarioRepository.findByEmail(email)).thenReturn(Optional.of(new Destinatario()));
            when(ofertaEspecialRepository.findById(ofertaEspecialId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> unitUnderTest.gerarVoucherParaDestinatario(email, ofertaEspecialId, validade))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format("Oferta especial não encontrada com o id: %s", ofertaEspecialId));
        }

        @Test
        void gerarVoucherParaDestinatarioSuccess() {
            String email = "test@test.com";
            String ofertaEspecialId = "off1";
            LocalDate validade = LocalDate.now().plusDays(1);

            when(destinatarioRepository.findByEmail(email)).thenReturn(Optional.of(new Destinatario()));
            when(ofertaEspecialRepository.findById(ofertaEspecialId)).thenReturn(Optional.of(new OfertaEspecial()));
            when(voucherRepository.save(any())).thenReturn(new Voucher());

            assertDoesNotThrow(() -> unitUnderTest.gerarVoucherParaDestinatario(email, ofertaEspecialId, validade));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GerarVouchersParaTodosDestinatarios {

        @Test
        void exceptionOfertaEspecialNaoEncontrada() {
            List<String> destinatariosEmails = Arrays.asList("test@test.com");
            String ofertaEspecialId = "off1";
            LocalDate validade = LocalDate.now().plusDays(1);

            when(ofertaEspecialRepository.findById(ofertaEspecialId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> unitUnderTest.gerarVouchersParaTodosDestinatarios(ofertaEspecialId, destinatariosEmails, validade))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(String.format("Oferta especial não encontrada com o id: %s", ofertaEspecialId));
        }

        @Test
        void exceptionDestinatarioNaoEncontrado() {
            List<String> destinatariosEmails = Arrays.asList("test@test.com");
            String ofertaEspecialId = "off1";
            LocalDate validade = LocalDate.now().plusDays(1);

            when(ofertaEspecialRepository.findById(ofertaEspecialId)).thenReturn(Optional.of(new OfertaEspecial()));
            when(destinatarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> unitUnderTest.gerarVouchersParaTodosDestinatarios(ofertaEspecialId, destinatariosEmails, validade))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Um ou mais destinatários não foram encontrados com os e-mails fornecidos.");
        }

        @Test
        void gerarVouchersParaTodosDestinatariosSuccess() {
            List<String> destinatariosEmails = Arrays.asList("test@test.com");
            String ofertaEspecialId = "off1";
            LocalDate validade = LocalDate.now().plusDays(1);

            when(ofertaEspecialRepository.findById(ofertaEspecialId)).thenReturn(Optional.of(new OfertaEspecial()));
            when(destinatarioRepository.findByEmail(anyString())).thenReturn(Optional.of(new Destinatario()));
            when(voucherRepository.save(any())).thenReturn(new Voucher());

            assertDoesNotThrow(() -> unitUnderTest.gerarVouchersParaTodosDestinatarios(ofertaEspecialId, destinatariosEmails, validade));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class UsarVoucher {

        @Test
        void exceptionVoucherInvalido() {
            String codigo = "testCode";
            String email = "test@test.com";

            Destinatario destinatario = new Destinatario();
            destinatario.setEmail(email);

            assertThatThrownBy(() -> unitUnderTest.usarVoucher(codigo, email))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Destinatário com email test@test.com não encontrado.");
        }


        @Test
        void usarVoucherSuccess() {
            String codigo = "testCode";
            String email = "test@test.com";

            // Create Destinatario
            Destinatario destinatario = new Destinatario();
            destinatario.setEmail(email);

            Voucher voucher = easyRandom.nextObject(Voucher.class);
            voucher.setCodigo(codigo);
            voucher.setStatus(VoucherStatus.ATV);
            voucher.setValidade(LocalDate.now().plusDays(1));
            OfertaEspecial ofertaEspecial = easyRandom.nextObject(OfertaEspecial.class);
            ofertaEspecial.setDesconto(10);
            voucher.setOfertaEspecial(ofertaEspecial);

            when(destinatarioRepository.findByEmail(email)).thenReturn(Optional.of(destinatario));
            when(voucherRepository.findByCodigoAndDestinatarioAndDataUsoIsNull(codigo, destinatario)).thenReturn(Optional.of(voucher));
            when(voucherRepository.save(any())).thenReturn(voucher);

            Integer result = unitUnderTest.usarVoucher(codigo, email);
            assertThat(result).isEqualTo(ofertaEspecial.getDesconto());

            assertThat(voucher.getStatus()).isEqualTo(VoucherStatus.USD);
            assertThat(voucher.getDataUso()).isNotNull();
        }

        @Test
        public void findValidVouchersByEmail_Success() {
            String codigo = "testCode";
            Destinatario destinatario = new Destinatario();
            destinatario.setEmail("test@test.com");
            Voucher voucher = easyRandom.nextObject(Voucher.class);
            voucher.setCodigo(codigo);
            voucher.setStatus(VoucherStatus.ATV);
            voucher.setValidade(LocalDate.now().plusDays(1));
            when(destinatarioRepository.findByEmail(destinatario.getEmail())).thenReturn(Optional.of(destinatario));
            when(voucherRepository.findByDestinatarioAndValidadeAfter(destinatario, LocalDate.now())).thenReturn(Collections.singletonList(voucher));

            List<Voucher> vouchers = unitUnderTest.findValidVouchersByEmail(destinatario.getEmail());

            org.hamcrest.MatcherAssert.assertThat(vouchers, hasSize(1));
            assertEquals(vouchers.get(0), voucher);
        }

        @Test
        public void findValidVouchersByEmail_Failure() {
            when(destinatarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> {
                unitUnderTest.findValidVouchersByEmail("unknown@test.com");
            });

        }


    }
}

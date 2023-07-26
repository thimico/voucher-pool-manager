package br.com.getnet.voucher_pool_manager.service;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.repos.DestinatarioRepository;
import com.mongodb.DuplicateKeyException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Tests {@link DestinatarioServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
public class DestinatarioServiceImplTest {

    @Mock
    private DestinatarioRepository destinatarioRepository;

    private DestinatarioServiceImpl unitUnderTest;

    private static EasyRandom easyRandom;

    @BeforeAll
    private static void beforeTests() {
        easyRandom = new EasyRandom();
    }

    @BeforeEach
    public void init() {
        unitUnderTest = new DestinatarioServiceImpl(destinatarioRepository);
    }

    /**
     * Tests {@link DestinatarioServiceImpl#criarDestinatario(Destinatario)}
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class criarDestinatario {


        @Test
        void exceptionDestinatarioJaExisteCriarDestinatario() {
            String email = "test@test.com";
            Destinatario entity = easyRandom.nextObject(Destinatario.class);
            entity.setEmail(email);
            Optional<Destinatario> destinatarioOptional = Optional.of(entity);
            when(destinatarioRepository.findByEmail(email)).thenReturn(destinatarioOptional);

            assertThatThrownBy(() -> unitUnderTest.criarDestinatario(entity))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage(String.format("Destinatario com %s %s ja existe", "email", email));

        }

        @Test
        void criarDestinatarioSuccess(){
            Destinatario expectedItem = easyRandom.nextObject(Destinatario.class);
            Optional<Destinatario> expectedItemOptional = Optional.of(easyRandom.nextObject(Destinatario.class));
            when(destinatarioRepository.save(any())).thenReturn(expectedItem);

            final Destinatario result = unitUnderTest.criarDestinatario(expectedItem);

            assertThat(result).isNotNull().isEqualTo(expectedItem);

            verify(destinatarioRepository).save(any());
        }

    }

}

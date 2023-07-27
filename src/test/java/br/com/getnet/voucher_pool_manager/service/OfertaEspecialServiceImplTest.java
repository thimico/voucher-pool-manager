package br.com.getnet.voucher_pool_manager.service;

import br.com.getnet.voucher_pool_manager.domain.OfertaEspecial;
import br.com.getnet.voucher_pool_manager.repos.OfertaEspecialRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests {@link OfertaEspecialServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
public class OfertaEspecialServiceImplTest {

    @Mock
    private OfertaEspecialRepository ofertaEspecialRepository;

    private OfertaEspecialServiceImpl unitUnderTest;

    private static EasyRandom easyRandom;

    @BeforeAll
    private static void beforeTests() {
        easyRandom = new EasyRandom();
    }

    @BeforeEach
    public void init() {
        unitUnderTest = new OfertaEspecialServiceImpl(ofertaEspecialRepository);
    }

    /**
     * Tests {@link OfertaEspecialServiceImpl#criarOfertaEspecial(OfertaEspecial)}
     */
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class criarOfertaEspecial {

        @Test
        void criarOfertaEspecialSuccess(){
            OfertaEspecial expectedItem = easyRandom.nextObject(OfertaEspecial.class);
            Optional<OfertaEspecial> expectedItemOptional = Optional.of(easyRandom.nextObject(OfertaEspecial.class));
            when(ofertaEspecialRepository.save(any())).thenReturn(expectedItem);

            final OfertaEspecial result = unitUnderTest.criarOfertaEspecial(expectedItem);

            assertThat(result).isNotNull().isEqualTo(expectedItem);

            verify(ofertaEspecialRepository).save(any());
        }

    }

}

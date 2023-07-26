package br.com.getnet.voucher_pool_manager.controller;

import br.com.getnet.voucher_pool_manager.domain.OfertaEspecial;
import br.com.getnet.voucher_pool_manager.model.OfertaEspecialRequest;
import br.com.getnet.voucher_pool_manager.model.OfertaEspecialResponse;
import br.com.getnet.voucher_pool_manager.service.OfertaEspecialService;
import br.com.getnet.voucher_pool_manager.utils.ControllerTestBase;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OfertaEspecialController.class)
public class OfertaEspecialControllerTest extends ControllerTestBase {

    private static String BASE_URL = OfertaEspecialController.OFERTA_URL;

    @MockBean
    private OfertaEspecialService ofertaEspecialService;

    @Test
    public void criarOfertaEspecialTestSuccess() throws Exception {
        // Arrange
        OfertaEspecialRequest ofertaEspecialRequest = easyRandom.nextObject(OfertaEspecialRequest.class);

        OfertaEspecial ofertaEspecial = modelMapper.map(ofertaEspecialRequest, OfertaEspecial.class);
        OfertaEspecial novaOfertaEspecial = ofertaEspecialService.criarOfertaEspecial(ofertaEspecial);
        OfertaEspecialResponse expectedResponse = modelMapper.map(novaOfertaEspecial, OfertaEspecialResponse.class);

        when(ofertaEspecialService.criarOfertaEspecial(any(OfertaEspecial.class))).thenReturn(novaOfertaEspecial);
        when(modelMapper.map(any(OfertaEspecial.class), eq(OfertaEspecialResponse.class))).thenReturn(expectedResponse);

        // Act
        doPost(BASE_URL, ofertaEspecialRequest, HttpStatus.CREATED);

        // Verify the results
        verify(modelMapper).map(ofertaEspecialRequest, OfertaEspecial.class);
    }


    @Test
    public void shouldReturnBadRequestWhenValidationExceptionIsThrown() throws Exception {
        // Arrange
        OfertaEspecialRequest ofertaEspecialRequest = easyRandom.nextObject(OfertaEspecialRequest.class);
        when(ofertaEspecialService.criarOfertaEspecial(any())).thenThrow(new ValidationException("Validation error"));

        // Act & Assert
        doPost(BASE_URL, ofertaEspecialRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws Exception {
        // Arrange
        OfertaEspecialRequest ofertaEspecialRequest = easyRandom.nextObject(OfertaEspecialRequest.class);
        when(ofertaEspecialService.criarOfertaEspecial(any())).thenThrow(new IllegalArgumentException("Illegal argument"));

        // Act & Assert
        doPost(BASE_URL, ofertaEspecialRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnInternalServerErrorWhenExceptionIsThrown() throws Exception {
        // Arrange
        OfertaEspecialRequest ofertaEspecialRequest = easyRandom.nextObject(OfertaEspecialRequest.class);
        when(ofertaEspecialService.criarOfertaEspecial(any())).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        doPost(BASE_URL, ofertaEspecialRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

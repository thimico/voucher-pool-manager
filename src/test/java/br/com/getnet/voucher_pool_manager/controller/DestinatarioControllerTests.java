package br.com.getnet.voucher_pool_manager.controller;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.model.DestinatarioRequest;
import br.com.getnet.voucher_pool_manager.model.DestinatarioResponse;
import br.com.getnet.voucher_pool_manager.service.DestinatarioService;
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
@WebMvcTest(DestinatarioController.class)
public class DestinatarioControllerTests extends ControllerTestBase {

    private static String BASE_URL = DestinatarioController.DESTINATARIO_URL;

    @MockBean
    private DestinatarioService destinatarioService;

    @Test
    public void criarDestinatarioTestSuccess() throws Exception {
        // Arrange
        DestinatarioRequest destinatarioRequest = easyRandom.nextObject(DestinatarioRequest.class);
        destinatarioRequest.setEmail("john.doe@example.com");

        Destinatario destinatario = modelMapper.map(destinatarioRequest, Destinatario.class);
        Destinatario novaDestinatario = destinatarioService.criarDestinatario(destinatario);
        DestinatarioResponse expectedResponse = modelMapper.map(novaDestinatario, DestinatarioResponse.class);

        when(destinatarioService.criarDestinatario(any(Destinatario.class))).thenReturn(novaDestinatario);
        when(modelMapper.map(any(Destinatario.class), eq(DestinatarioResponse.class))).thenReturn(expectedResponse);

        // Act
        doPost(BASE_URL, destinatarioRequest, HttpStatus.CREATED);

        // Verify the results
        verify(modelMapper).map(destinatarioRequest, Destinatario.class);
    }


    @Test
    public void shouldReturnBadRequestWhenValidationExceptionIsThrown() throws Exception {
        // Arrange
        DestinatarioRequest destinatarioRequest = easyRandom.nextObject(DestinatarioRequest.class);
        when(destinatarioService.criarDestinatario(any())).thenThrow(new ValidationException("Validation error"));

        // Act & Assert
        doPost(BASE_URL, destinatarioRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws Exception {
        // Arrange
        DestinatarioRequest destinatarioRequest = easyRandom.nextObject(DestinatarioRequest.class);
        when(destinatarioService.criarDestinatario(any())).thenThrow(new IllegalArgumentException("Illegal argument"));

        // Act & Assert
        doPost(BASE_URL, destinatarioRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnInternalServerErrorWhenExceptionIsThrown() throws Exception {
        // Arrange
        DestinatarioRequest destinatarioRequest = easyRandom.nextObject(DestinatarioRequest.class);
        when(destinatarioService.criarDestinatario(any())).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        doPost(BASE_URL, destinatarioRequest, HttpStatus.BAD_REQUEST);
    }



}

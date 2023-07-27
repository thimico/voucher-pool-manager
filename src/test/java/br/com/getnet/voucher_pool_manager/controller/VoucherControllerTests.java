package br.com.getnet.voucher_pool_manager.controller;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.domain.OfertaEspecial;
import br.com.getnet.voucher_pool_manager.domain.Voucher;
import br.com.getnet.voucher_pool_manager.model.DestinatarioRequest;
import br.com.getnet.voucher_pool_manager.model.VoucherPoolRequest;
import br.com.getnet.voucher_pool_manager.model.VoucherRequest;
import br.com.getnet.voucher_pool_manager.model.VoucherResponse;
import br.com.getnet.voucher_pool_manager.service.VoucherService;
import br.com.getnet.voucher_pool_manager.utils.ControllerTestBase;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VoucherController.class)
public class VoucherControllerTests extends ControllerTestBase {

    private static String BASE_URL = VoucherController.VOUCHER_URL;

    @MockBean
    private VoucherService voucherService;

    @Test
    public void criarVoucherTestSuccess() throws Exception {
        // Arrange
        VoucherRequest voucherRequest = easyRandom.nextObject(VoucherRequest.class);
        voucherRequest.setDestinatarioEmail("john.doe@example.com");
        voucherRequest.setValidade("31/12/2023");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        OfertaEspecial ofertaEspecial = easyRandom.nextObject(OfertaEspecial.class);
        Destinatario destinatario = easyRandom.nextObject(Destinatario.class);
        destinatario.setEmail("john.doe@example.com");
        Voucher voucher = new Voucher();
        voucher.setDestinatario(destinatario);
        voucher.setOfertaEspecial(ofertaEspecial);
        voucher.setValidade(LocalDate.parse(voucherRequest.getValidade(), formatter));
        when(modelMapper.map(any(VoucherRequest.class), eq(Voucher.class))).thenReturn(voucher);
        Voucher novaVoucher = voucherService.gerarVoucherParaDestinatario(voucherRequest.getDestinatarioEmail(), voucherRequest.getOfertaEspecialId(), voucher.getValidade());

        VoucherResponse expectedResponse = modelMapper.map(novaVoucher, VoucherResponse.class);

        when(voucherService.gerarVoucherParaDestinatario(any(String.class), any(String.class), any(LocalDate.class))).thenReturn(novaVoucher);
        when(modelMapper.map(any(Voucher.class), eq(VoucherResponse.class))).thenReturn(expectedResponse);

        // Act & Assert
        doPost(BASE_URL, voucherRequest, HttpStatus.CREATED);

        // Verify the results
        verify(voucherService, times(2)).gerarVoucherParaDestinatario(voucherRequest.getDestinatarioEmail(), voucherRequest.getOfertaEspecialId(), voucher.getValidade());
    }


    @Test
    public void shouldReturnBadRequestWhenValidationExceptionIsThrown() throws Exception {
        // Arrange
        VoucherRequest voucherRequest = easyRandom.nextObject(VoucherRequest.class);
        when(voucherService.gerarVoucherParaDestinatario(any(String.class), any(String.class), any())).thenThrow(new ValidationException("Validation error"));

        // Act & Assert
        doPost(BASE_URL, voucherRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() throws Exception {
        // Arrange
        VoucherRequest voucherRequest = easyRandom.nextObject(VoucherRequest.class);
        when(voucherService.gerarVoucherParaDestinatario(any(String.class), any(String.class), any())).thenThrow(new IllegalArgumentException("Illegal argument"));

        // Act & Assert
        doPost(BASE_URL, voucherRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldReturnInternalServerErrorWhenExceptionIsThrown() throws Exception {
        // Arrange
        VoucherRequest voucherRequest = easyRandom.nextObject(VoucherRequest.class);
        when(voucherService.gerarVoucherParaDestinatario(any(String.class), any(String.class), any())).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        doPost(BASE_URL, voucherRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void gerarVouchersParaTodosDestinatariosTestSuccess() throws Exception {
        // Arrange
        VoucherPoolRequest voucherPoolRequest = new VoucherPoolRequest();
        voucherPoolRequest.setOfertaEspecialId("1");
        voucherPoolRequest.setValidade(LocalDate.of(2023, 12, 31));
        voucherPoolRequest.setDestinatariosEmails(Arrays.asList("john.doe@example.com", "jane.doe@example.com"));

        List<Voucher> vouchers = new ArrayList<>();
        for (String email : voucherPoolRequest.getDestinatariosEmails()) {
            OfertaEspecial ofertaEspecial = easyRandom.nextObject(OfertaEspecial.class);
            Destinatario destinatario = easyRandom.nextObject(Destinatario.class);
            destinatario.setEmail(email);

            Voucher voucher = new Voucher();
            voucher.setDestinatario(destinatario);
            voucher.setOfertaEspecial(ofertaEspecial);
            voucher.setValidade(voucherPoolRequest.getValidade());
            vouchers.add(voucher);
        }

        when(voucherService.gerarVouchersParaTodosDestinatarios(any(String.class), anyList(), any(LocalDate.class))).thenReturn(vouchers);

        // Act & Assert
        doPost(BASE_URL+"/pool", voucherPoolRequest, HttpStatus.OK);

        // Verify the results
        verify(voucherService).gerarVouchersParaTodosDestinatarios(voucherPoolRequest.getOfertaEspecialId(), voucherPoolRequest.getDestinatariosEmails(), voucherPoolRequest.getValidade());
    }

    @Test
    public void usarVoucherTestSuccess() throws Exception {
        // Arrange
        String voucherCode = "testCode";
        DestinatarioRequest destinatarioRequest = new DestinatarioRequest();
        destinatarioRequest.setEmail("john.doe@example.com");
        destinatarioRequest.setNome("John Doe");

        Destinatario destinatario = new Destinatario();
        destinatario.setEmail(destinatarioRequest.getEmail());

        Voucher usedVoucher = new Voucher(); // assuming usarVoucher() returns a Voucher object
        when(modelMapper.map(any(DestinatarioRequest.class), eq(Destinatario.class))).thenReturn(destinatario);
        when(voucherService.usarVoucher(anyString(), anyString())).thenReturn(1);

        // Act & Assert
        doPut(BASE_URL + "/" + voucherCode, destinatarioRequest, HttpStatus.NO_CONTENT);

        // Verify the results
        verify(voucherService).usarVoucher(anyString(), anyString());
    }




}

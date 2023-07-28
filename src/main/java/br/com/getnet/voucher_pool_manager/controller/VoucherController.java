package br.com.getnet.voucher_pool_manager.controller;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.domain.Voucher;
import br.com.getnet.voucher_pool_manager.model.DestinatarioRequest;
import br.com.getnet.voucher_pool_manager.model.VoucherPoolRequest;
import br.com.getnet.voucher_pool_manager.model.VoucherRequest;
import br.com.getnet.voucher_pool_manager.model.VoucherResponse;
import br.com.getnet.voucher_pool_manager.service.VoucherService;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.getnet.voucher_pool_manager.controller.VoucherController.VOUCHER_URL;
@RestController
@RequestMapping(path = VOUCHER_URL, produces = MediaType.APPLICATION_JSON_VALUE)

@Api(tags = "Vouchers")
public class VoucherController {

    public static final String VOUCHER_URL = "/api/v1/vouchers";
    private final VoucherService voucherService;
    private final ModelMapper modelMapper;

    public VoucherController(VoucherService voucherService, ModelMapper modelMapper) {
        this.voucherService = voucherService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ApiOperation(value = "Gerar um novo voucher para um destinatário específico")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Voucher gerado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição mal formada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<VoucherResponse> gerarVoucherParaDestinatario(@ApiParam(value = "Dados necessários para gerar um novo voucher") @Valid @RequestBody VoucherRequest voucherRequest) {
        Voucher voucher = modelMapper.map(voucherRequest, Voucher.class);
        Voucher novoVoucher = voucherService.gerarVoucherParaDestinatario(voucherRequest.getDestinatarioEmail(), voucherRequest.getOfertaEspecialId(), voucher.getValidade());
        return new ResponseEntity<>(modelMapper.map(novoVoucher, VoucherResponse.class), HttpStatus.CREATED);
    }

    @PostMapping("/pool")
    @ApiOperation(value = "Gera vouchers para todos os destinatários")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Vouchers gerados com sucesso"),
            @ApiResponse(code = 400, message = "Dados inválidos fornecidos"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<List<VoucherResponse>> gerarVouchersParaTodosDestinatarios(@Valid @RequestBody VoucherPoolRequest voucherPoolRequest) {
        List<Voucher> vouchers = voucherService.gerarVouchersParaTodosDestinatarios(
                voucherPoolRequest.getOfertaEspecialId(),
                voucherPoolRequest.getDestinatariosEmails(),
                voucherPoolRequest.getValidade());

        List<VoucherResponse> response = vouchers.stream()
                .map(voucher -> modelMapper.map(voucher, VoucherResponse.class))
                .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{codigo}")
    @ApiOperation(value = "Usar um voucher específico")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Voucher usado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição mal formada"),
            @ApiResponse(code = 404, message = "Voucher não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<?> usarVoucher(@ApiParam(value = "Código do voucher a ser usado") @PathVariable String codigo, @ApiParam(value = "Dados do destinatário para o uso do voucher") @Valid @RequestBody DestinatarioRequest destinatarioRequest) {
        Destinatario destinatario = modelMapper.map(destinatarioRequest, Destinatario.class);
        voucherService.usarVoucher(codigo, destinatario.getEmail());
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Busca todos os vouchers válidos para um determinado e-mail")
    @GetMapping("/valid")
    public List<VoucherResponse> getValidVouchers(
            @ApiParam(value = "E-mail do destinatário para o qual os vouchers válidos serão retornados", required = true)
            @RequestParam("email") String email) {

        List<Voucher> vouchers = voucherService.findValidVouchersByEmail(email);

        return vouchers.stream()
                .map(this::convertToVoucherResponse)
                .collect(Collectors.toList());
    }

    private VoucherResponse convertToVoucherResponse(Voucher voucher) {
        return modelMapper.map(voucher, VoucherResponse.class);
    }

}

package br.com.getnet.voucher_pool_manager.controller;

import br.com.getnet.voucher_pool_manager.domain.Destinatario;
import br.com.getnet.voucher_pool_manager.model.DestinatarioRequest;
import br.com.getnet.voucher_pool_manager.model.DestinatarioResponse;
import br.com.getnet.voucher_pool_manager.service.DestinatarioService;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DestinatarioController.DESTINATARIO_URL)
@Api(tags = "Destinatarios")
public class DestinatarioController {

    public static final String DESTINATARIO_URL = "/api/v1/destinatarios";
    private final DestinatarioService destinatarioService;
    private final ModelMapper modelMapper;

    public DestinatarioController(DestinatarioService destinatarioService, ModelMapper modelMapper) {
        this.destinatarioService = destinatarioService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ApiOperation(value = "Gerar uma nova Destinatario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Destinatario criado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição mal formada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<DestinatarioResponse> gerarVoucherParaDestinatario(@ApiParam(value = "Dados necessários para criar um novo Destinatario") @Valid @RequestBody DestinatarioRequest destinatarioRequest) {
        Destinatario destinatario = modelMapper.map(destinatarioRequest, Destinatario.class);
        Destinatario novaDestinatario = destinatarioService.criarDestinatario(destinatario);
        return new ResponseEntity<>(modelMapper.map(novaDestinatario, DestinatarioResponse.class), HttpStatus.CREATED);
    }

}

package br.com.getnet.voucher_pool_manager.controller;

import br.com.getnet.voucher_pool_manager.domain.OfertaEspecial;
import br.com.getnet.voucher_pool_manager.model.OfertaEspecialRequest;
import br.com.getnet.voucher_pool_manager.model.OfertaEspecialResponse;
import br.com.getnet.voucher_pool_manager.service.OfertaEspecialService;
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
@RequestMapping(OfertaEspecialController.OFERTA_URL)
@Api(tags = "Ofertas")
public class OfertaEspecialController {

    public static final String OFERTA_URL = "/api/v1/ofertas";
    private final OfertaEspecialService ofertaEspecialService;
    private final ModelMapper modelMapper;

    public OfertaEspecialController(OfertaEspecialService ofertaEspecialService, ModelMapper modelMapper) {
        this.ofertaEspecialService = ofertaEspecialService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ApiOperation(value = "Gerar uma nova Oferta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Oferta gerada com sucesso"),
            @ApiResponse(code = 400, message = "Requisição mal formada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<OfertaEspecialResponse> gerarVoucherParaDestinatario(@ApiParam(value = "Dados necessários para gerar uma nova Oferta") @Valid @RequestBody OfertaEspecialRequest ofertaEspecialRequest) {
        OfertaEspecial ofertaEspecial = modelMapper.map(ofertaEspecialRequest, OfertaEspecial.class);
        OfertaEspecial novaOfertaEspecial = ofertaEspecialService.criarOfertaEspecial(ofertaEspecial);
        return new ResponseEntity<>(modelMapper.map(novaOfertaEspecial, OfertaEspecialResponse.class), HttpStatus.CREATED);
    }

}

package com.marcelojssantos.dio.estoquecerveja.controller;

import com.marcelojssantos.dio.estoquecerveja.dto.CervejaDTO;
import com.marcelojssantos.dio.estoquecerveja.dto.QuantidadeDTO;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaEstaRegistradaException;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaNaoEncontradaException;
import com.marcelojssantos.dio.estoquecerveja.exception.EstoqueCervejaExcedidoException;
import com.marcelojssantos.dio.estoquecerveja.service.CervejaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cervejas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaController {

    private final CervejaService cervejaService;

    @GetMapping
    public List<CervejaDTO> listaCervejas() {
        return cervejaService.listAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CervejaDTO insereCerveja(@RequestBody @Valid CervejaDTO cervejaDTO)
            throws CervejaEstaRegistradaException {
        return cervejaService.insert(cervejaDTO);
    }

    @GetMapping("/{nome}")
    public CervejaDTO encontraPorNome(@PathVariable String nome) throws CervejaNaoEncontradaException {
        return cervejaService.findByNome(nome);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaPorId(@PathVariable Long id) throws CervejaNaoEncontradaException {
        cervejaService.deleteById(id);
    }

    @PatchMapping("/{id}/incrementa")
    public CervejaDTO incrementaQuantidade(@PathVariable Long id,
                                           @RequestBody @Valid QuantidadeDTO quantidadeDTO)
            throws CervejaNaoEncontradaException, EstoqueCervejaExcedidoException {
        return cervejaService.increment(id, quantidadeDTO.getQuantidade());
    }

}
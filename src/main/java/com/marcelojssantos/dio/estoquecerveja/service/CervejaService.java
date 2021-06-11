package com.marcelojssantos.dio.estoquecerveja.service;

import com.marcelojssantos.dio.estoquecerveja.dto.CervejaDTO;
import com.marcelojssantos.dio.estoquecerveja.entity.Cerveja;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaEstaRegistradaException;
import com.marcelojssantos.dio.estoquecerveja.exception.EstoqueCervejaExcedidoException;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaNaoEncontradaException;
import com.marcelojssantos.dio.estoquecerveja.mapper.CervejaMapper;
import com.marcelojssantos.dio.estoquecerveja.repository.CervejaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaService {

    private final CervejaRepository cervejaRepository;
    private final CervejaMapper cervejaMapper = CervejaMapper.INSTANCE;

    public List<CervejaDTO> listAll() {
        return cervejaRepository.findAll()
                .stream()
                .map(cervejaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CervejaDTO insert(CervejaDTO cervejaDTO) throws CervejaEstaRegistradaException {
        verificaSeEstaRegistrado(cervejaDTO.getNome());
        Cerveja cerveja = cervejaMapper.toModel(cervejaDTO);
        Cerveja cervejaSalva = cervejaRepository.save(cerveja);
        return cervejaMapper.toDTO(cervejaSalva);
    }

    public CervejaDTO findByNome(String nome) throws CervejaNaoEncontradaException {
        Cerveja cervejaEncontrada = cervejaRepository.findByNome(nome)
                .orElseThrow(() -> new CervejaNaoEncontradaException(nome));
        return cervejaMapper.toDTO(cervejaEncontrada);
    }

    public void deleteById(Long id) throws CervejaNaoEncontradaException {
        verificaSeExiste(id);
        cervejaRepository.deleteById(id);
    }

    public CervejaDTO increment(Long id, int quantidadeParaIncrementar) throws CervejaNaoEncontradaException,
            EstoqueCervejaExcedidoException {
        Cerveja cervejaParaAumentarEstoque = verificaSeExiste(id);
        int quantidadeAposIncremento = quantidadeParaIncrementar + cervejaParaAumentarEstoque.getQuantidade();
        if (quantidadeAposIncremento <= cervejaParaAumentarEstoque.getQuantMax()) {
            cervejaParaAumentarEstoque.setQuantidade(cervejaParaAumentarEstoque.getQuantidade() +
                    quantidadeParaIncrementar);
            Cerveja incrementedBeerStock = cervejaRepository.save(cervejaParaAumentarEstoque);
            return cervejaMapper.toDTO(incrementedBeerStock);
        }
        throw new EstoqueCervejaExcedidoException(id, quantidadeParaIncrementar);
    }

    private void verificaSeEstaRegistrado(String nome) throws CervejaEstaRegistradaException {
        Optional<Cerveja> cervejaSalva = cervejaRepository.findByNome(nome);
        if (cervejaSalva.isPresent()) {
            throw new CervejaEstaRegistradaException(nome);
        }
    }

    private Cerveja verificaSeExiste(Long id) throws CervejaNaoEncontradaException {
        return cervejaRepository.findById(id)
                .orElseThrow(() -> new CervejaNaoEncontradaException(id));
    }

}
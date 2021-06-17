package com.marcelojssantos.dio.estoquecerveja.service;

import com.marcelojssantos.dio.estoquecerveja.dto.CervejaDTO;
import com.marcelojssantos.dio.estoquecerveja.entity.Cerveja;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaEstaRegistradaException;
import com.marcelojssantos.dio.estoquecerveja.exception.CervejaNaoEncontradaException;
import com.marcelojssantos.dio.estoquecerveja.exception.EstoqueCervejaMaxQuantExcedidoException;
import com.marcelojssantos.dio.estoquecerveja.exception.EstoqueCervejaMaxQuantMenorZeroException;
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

    public CervejaDTO changeStock(Long id, int quantidadeParaIncrementar)
            throws CervejaNaoEncontradaException,
            EstoqueCervejaMaxQuantExcedidoException,
            EstoqueCervejaMaxQuantMenorZeroException {
        Cerveja cervejaParaAlterarEstoque = verificaSeExiste(id);
        int quantidadeAposIncremento = quantidadeParaIncrementar + cervejaParaAlterarEstoque.getQuantidade();
        if ((quantidadeAposIncremento >= 0) && (quantidadeAposIncremento <= cervejaParaAlterarEstoque.getQuantMax())){
            cervejaParaAlterarEstoque.setQuantidade(cervejaParaAlterarEstoque.getQuantidade() +
                    quantidadeParaIncrementar);
            Cerveja cervejaEstoqueAlterado = cervejaRepository.save(cervejaParaAlterarEstoque);
            return cervejaMapper.toDTO(cervejaEstoqueAlterado);
        }
        if (quantidadeParaIncrementar > 0){
            throw new EstoqueCervejaMaxQuantExcedidoException(id, quantidadeParaIncrementar);
        } else {
            throw new EstoqueCervejaMaxQuantMenorZeroException(id, quantidadeParaIncrementar);
        }
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
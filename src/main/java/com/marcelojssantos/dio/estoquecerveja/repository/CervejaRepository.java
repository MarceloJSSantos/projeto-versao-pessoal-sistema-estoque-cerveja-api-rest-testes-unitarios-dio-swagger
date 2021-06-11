package com.marcelojssantos.dio.estoquecerveja.repository;

import com.marcelojssantos.dio.estoquecerveja.entity.Cerveja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CervejaRepository extends JpaRepository<Cerveja, Long> {

    Optional<Cerveja> findByNome(String nome);
}

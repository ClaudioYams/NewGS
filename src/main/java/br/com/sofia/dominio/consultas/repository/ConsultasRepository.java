package br.com.sofia.dominio.consultas.repository;

import br.com.sofia.dominio.consultas.entity.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultasRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByUserUuid(String uuid);
    Page<Consulta> findByUserUuid(String uuid, Pageable pageable);
}

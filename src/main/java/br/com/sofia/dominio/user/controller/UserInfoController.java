package br.com.sofia.dominio.user.controller;

import br.com.sofia.dominio.consultas.entity.Consulta;
import br.com.sofia.dominio.consultas.repository.ConsultasRepository;
import br.com.sofia.dominio.user.dto.UserInfoDTO;
import br.com.sofia.dominio.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user/{uuid}/info")
public class UserInfoController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private ConsultasRepository consultasRepository;

    @GetMapping
    public ResponseEntity<UserInfoDTO> findUserInfo(@PathVariable String uuid) {
        var user = repo.findById(uuid);
        var consultaList = consultasRepository.findByUserUuid(uuid);

        if (user.isPresent() && consultaList != null) {
            Set<Consulta> consultas = new HashSet<>(consultaList);

            var dataGravidez = user.get().getDataGravidez();
            var dataAtual = java.time.LocalDate.now();

            var semanas = ChronoUnit.WEEKS.between(dataGravidez, dataAtual);

            return ResponseEntity.ok(UserInfoDTO.toDTO(user.get(), semanas, consultas));
        }

        return ResponseEntity.notFound().build();
    }

}

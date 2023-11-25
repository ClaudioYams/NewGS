package br.com.sofia.dominio.consultas.controller;

import br.com.sofia.dominio.consultas.dto.ConsultaDTO;
import br.com.sofia.dominio.consultas.entity.Consulta;
import br.com.sofia.dominio.consultas.repository.ConsultasRepository;
import br.com.sofia.dominio.user.controller.UserController;
import br.com.sofia.dominio.user.dto.UserDTO;
import br.com.sofia.dominio.user.entity.User;
import br.com.sofia.dominio.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/consulta")

public class ConsultasController {

    @Autowired
    private ConsultasRepository repo;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ConsultaDTO>>> findAll(
            @PageableDefault(size = 5, sort = {"dataHoraConsulta"}) Pageable pageable,
            @RequestParam(required = false) String uuid) {
        Page<Consulta> consultas;
        if (uuid != null) {
            consultas = repo.findByUserUuid(uuid, pageable);
        } else {
            consultas = repo.findAll(pageable);
        }
        return ResponseEntity.ok(toPagedModel(consultas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ConsultaDTO>> findById(@PathVariable Long id) {
        var consulta = repo.findById(id);
        if (consulta.isPresent()) {
            EntityModel<ConsultaDTO> entityModel = toModel(consulta.get());
            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping()
    @Transactional
    public ResponseEntity<EntityModel<ConsultaDTO>> save(@RequestBody @Valid ConsultaDTO dto, UriComponentsBuilder ucBuilder) {
        if (dto.uuidUser() == null) {
            return ResponseEntity.badRequest().build();
        }

        var user = userRepository.findById(dto.uuidUser());

        if (user.isPresent()) {
            var consulta = repo.save(dto.toModel(user.get()));
            var uri = ucBuilder.path("/{uuid}").buildAndExpand(consulta.getUser().getUuid()).toUri();
            return ResponseEntity.created(uri).body(EntityModel.of(ConsultaDTO.of(consulta)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ConsultaDTO> update(@PathVariable Long id, @RequestBody @Valid ConsultaDTO dto) {
        var consulta = repo.findById(id);

        if (consulta.isPresent()) {
            Consulta updatedConsulta = consulta.get();
            updatedConsulta.setDataHoraConsulta(dto.dataHoraConsulta());
            updatedConsulta.setTipoConsulta(dto.tipoConsulta());

            return ResponseEntity.ok(ConsultaDTO.of(updatedConsulta));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        var consulta = repo.findById(id);
        if (consulta.isPresent()) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private PagedModel<EntityModel<ConsultaDTO>> toPagedModel(Page<Consulta> consultas) {
        List<EntityModel<ConsultaDTO>> consultaDTOs = consultas.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return PagedModel.of(consultaDTOs, new PagedModel.PageMetadata(
                consultas.getSize(),
                consultas.getNumber(),
                consultas.getTotalElements(),
                consultas.getTotalPages()
        ));
    }

    private EntityModel<ConsultaDTO> toModel(Consulta consulta) {
        EntityModel<ConsultaDTO> model;

        model = EntityModel.of(ConsultaDTO.of(consulta))
                .add(linkTo(
                        methodOn(ConsultasController.class)
                                .findById(consulta.getId()))
                        .withSelfRel()
                        .withTitle(consulta.getTipoConsulta())
                );

        model.add(linkTo(methodOn(UserController.class).findAll()).withRel(IanaLinkRelations.COLLECTION));
        return model;
    }
}
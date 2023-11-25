package br.com.sofia.dominio.user.controller;

import br.com.sofia.dominio.user.dto.UserDTO;
import br.com.sofia.dominio.user.entity.User;
import br.com.sofia.dominio.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository repo;

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> findAll() {
        Collection<User> users = repo.findAll();
        return ResponseEntity.ok(toCollectionModel(users));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EntityModel<UserDTO>> findById(@PathVariable String uuid) {
        var user = repo.findById(uuid);
        if (user.isPresent()) {
            EntityModel<UserDTO> entityModel = toModel(user.get());
            return ResponseEntity.ok(entityModel);
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    @PostMapping()
    public ResponseEntity<EntityModel<UserDTO>> save(@RequestBody @Valid UserDTO dto, UriComponentsBuilder ucBuilder) {
        var user = repo.save(dto.toModel());
        var uri = ucBuilder.path("/{uuid}").buildAndExpand(user.getUuid()).toUri();
        return ResponseEntity.created(uri).body(toModel(user));
    }

    @Transactional
    @PutMapping("/{uuid}")
    public ResponseEntity<UserDTO> update(@PathVariable String uuid, @RequestBody @Valid UserDTO dto) {
        Optional<User> user = repo.findById(uuid);

        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setNome(dto.nome());
            updatedUser.setDataGravidez(dto.dataGravidez());

            return ResponseEntity.ok(UserDTO.of(updatedUser));
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Object> deleteById(@PathVariable String uuid) {
        var user = repo.findById(uuid);
        if (user.isPresent()) {
            repo.deleteById(uuid);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private CollectionModel<EntityModel<UserDTO>> toCollectionModel(Collection<User> users) {
        var dtos = CollectionModel.of(users.stream().map(this::toModel).collect(Collectors.toSet()));
        dtos.add(linkTo(methodOn(UserController.class).findAll()).withSelfRel());
        return dtos;
    }

    private EntityModel<UserDTO> toModel(User user) {
        EntityModel<UserDTO> model;

        model = EntityModel.of(UserDTO.of(user))
                .add(linkTo(
                        methodOn(UserInfoController.class).findUserInfo(user.getUuid()))
                        .withSelfRel()
                        .withTitle(user.getNome())
                );

        model.add(linkTo(methodOn(UserController.class).findAll()).withRel(IanaLinkRelations.COLLECTION));
        return model;
    }
}
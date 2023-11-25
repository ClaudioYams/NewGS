package br.com.sofia.dominio.user.dto;

import br.com.sofia.dominio.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Relation(collectionRelation = "users")
public record UserDTO(
        String uuid,
        String nome,
        @PastOrPresent
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataGravidez
) {

    public static UserDTO of(String uuid) {
        return new UserDTO(uuid, "", null);
    }

    public static Set<UserDTO> of(Collection<User> users) {
        return users.stream().map(UserDTO::of).collect(Collectors.toCollection(LinkedHashSet<UserDTO>::new));
    }

    public static UserDTO of(User user) {
        return new UserDTO(user.getUuid(), user.getNome(), user.getDataGravidez());
    }

    public User toModel() {
        var random_uuid = randomUUID().toString();
        return new User().setUuid(random_uuid).setNome(nome).setDataGravidez(dataGravidez);
    }
}
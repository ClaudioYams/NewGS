package br.com.sofia.dominio.consultas.dto;

import br.com.sofia.dominio.consultas.entity.Consulta;
import br.com.sofia.dominio.user.dto.UserDTO;
import br.com.sofia.dominio.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Relation(collectionRelation = "consultas")
public record ConsultaDTO(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataHoraConsulta,
        String tipoConsulta,
        String descricaoConsulta,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String uuidUser,
        UserDTO user
) {

    public static ConsultaDTO of(Consulta consulta) {
        User user = consulta.getUser();
        String uuidUser = user != null ? user.getUuid() : null;
        UserDTO userDTO = user != null ? UserDTO.of(user) : null;
        return new ConsultaDTO(
                consulta.getId(),
                consulta.getDataHoraConsulta(),
                consulta.getTipoConsulta(),
                consulta.getDescricaoConsulta(),
                uuidUser,
                userDTO
        );
    }

    public static Set<ConsultaDTO> of(Collection<Consulta> consultas) {
        return consultas.stream().map(ConsultaDTO::of).collect(Collectors.toCollection(LinkedHashSet<ConsultaDTO>::new));
    }

    public Consulta toModel(User user) {
        return new Consulta()
                .setDataHoraConsulta(dataHoraConsulta)
                .setTipoConsulta(tipoConsulta)
                .setDescricaoConsulta(descricaoConsulta)
                .setUser(user);
    }
}
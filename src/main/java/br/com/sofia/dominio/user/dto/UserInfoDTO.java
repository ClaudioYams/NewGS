package br.com.sofia.dominio.user.dto;

import br.com.sofia.dominio.consultas.dto.ConsultaDTO;
import br.com.sofia.dominio.consultas.entity.Consulta;
import br.com.sofia.dominio.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Set;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserInfoDTO (
        @JsonUnwrapped
        UserDTO user,
        long semanasGestacao,
        Set<ConsultaDTO> consulta
) {
    public static UserInfoDTO toDTO(User user, long idadeGestacional, Set<Consulta> consulta) {
        UserDTO userDTO = UserDTO.of(user);
        Set<ConsultaDTO> consultaDTO = consulta.stream()
                .map(c -> new ConsultaDTO(c.getId(), c.getDataHoraConsulta(), c.getTipoConsulta(), c.getDescricaoConsulta(), c.getUser().getUuid(), null))
                .collect(Collectors.toSet());

        return new UserInfoDTO(userDTO, idadeGestacional, consultaDTO);
    }
}
package br.com.sofia.dominio.consultas.entity;

import br.com.sofia.dominio.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SOFIA_CONSULTA")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATA_HORA_CONSULTA")
    private LocalDateTime dataHoraConsulta;

    @Column(name = "TIPO_CONSULTA")
    private String tipoConsulta;

    @Column(name = "DS_CONSULTA")
    private String descricaoConsulta;

    @ManyToOne
    @JoinColumn(name="UUID_USER", nullable=false, referencedColumnName = "UUID_USER", foreignKey = @ForeignKey(name = "FK_CONSULTA_USER"))
    private User user;

    public Long getId() {
        return id;
    }

    public Consulta setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDateTime getDataHoraConsulta() {
        return dataHoraConsulta;
    }

    public Consulta setDataHoraConsulta(LocalDateTime dataHoraConsulta) {
        this.dataHoraConsulta = dataHoraConsulta;
        return this;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public Consulta setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Consulta setUser(User user) {
        this.user = user;
        return this;
    }

    public String getDescricaoConsulta() {
        return descricaoConsulta;
    }

    public Consulta setDescricaoConsulta(String descricaoConsulta) {
        this.descricaoConsulta = descricaoConsulta;
        return this;
    }
}
package br.com.sofia.dominio.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "SOFIA_USER")
public class User {

    @Id
    @Column(name = "UUID_USER")
    private String uuid;

    @Column(name = "NM_USER")
    private String nome;

    @Column(name = "DT_GRAVIDEZ")
    private LocalDate dataGravidez;

    public String getUuid() {
        return uuid;
    }

    public User setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public User setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public LocalDate getDataGravidez() {
        return dataGravidez;
    }

    public User setDataGravidez(LocalDate dataGravidez) {
        this.dataGravidez = dataGravidez;
        return this;
    }
}

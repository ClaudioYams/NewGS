package br.com.sofia.dominio.user.repository;

import br.com.sofia.dominio.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

package ru.nasyrov.spr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nasyrov.spr.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}

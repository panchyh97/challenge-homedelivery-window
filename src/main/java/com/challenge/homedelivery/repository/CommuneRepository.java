package com.challenge.homedelivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge.homedelivery.model.Commune;

public interface CommuneRepository extends JpaRepository<Commune, Long> {
  Optional<Commune> findByNameIgnoreCase(String name);

  @EntityGraph(attributePaths = "zone")
  List<Commune> findAllByOrderByNameAsc();
}
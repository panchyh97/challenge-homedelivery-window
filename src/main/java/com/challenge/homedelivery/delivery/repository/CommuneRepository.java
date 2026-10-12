package com.challenge.homedelivery.delivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge.homedelivery.delivery.model.Commune;

public interface CommuneRepository extends JpaRepository<Commune, Long> {
  Optional<Commune> findByNameIgnoreCase(String name);
}
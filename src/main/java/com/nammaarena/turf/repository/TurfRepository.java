package com.nammaarena.turf.repository;

import com.nammaarena.turf.entity.Turf;
import com.nammaarena.turf.enums.TurfStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TurfRepository extends JpaRepository<Turf, Long> {
    List<Turf> findByOwnerId(Long ownerId);
    List<Turf> findByStatus(TurfStatus status);
    Optional<Turf> findByNameAndOwnerId(String name, Long ownerId);
    Optional<Turf> findByIdAndOwnerId(Long id, Long ownerId);
}
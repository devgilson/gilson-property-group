package com.example.breken30daysback.Repository;

import com.example.breken30daysback.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, String> {
    Optional<Property> findByPropertyId(String propertyId);
}

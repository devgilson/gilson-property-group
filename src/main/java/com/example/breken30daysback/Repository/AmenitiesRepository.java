package com.example.breken30daysback.Repository;

import com.example.breken30daysback.Models.Amenities;
import com.example.breken30daysback.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AmenitiesRepository extends JpaRepository<Amenities, Long> {
    Optional<Amenities> findByPropertyAndItem(Property property, String item);
}

package com.fc.toy_project3.domain.itinerary.repository;

import com.fc.toy_project3.domain.itinerary.entity.Itinerary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

}

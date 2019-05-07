package com.infobelt.aadhaar.example.repository;

import com.infobelt.aadhaar.example.domain.Thingy;
import com.infobelt.aadhaar.example.domain.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingyRepository extends JpaRepository<Thingy, Long> {
}

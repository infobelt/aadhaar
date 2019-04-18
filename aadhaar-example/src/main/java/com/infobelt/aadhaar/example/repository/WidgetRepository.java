package com.infobelt.aadhaar.example.repository;

import com.infobelt.aadhaar.example.domain.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {
}

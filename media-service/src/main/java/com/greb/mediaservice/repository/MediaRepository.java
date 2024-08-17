package com.greb.mediaservice.repository;

import com.greb.mediaservice.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, String> {
}

package com.inpusduat.inpusduat.repository;

import com.inpusduat.inpusduat.domain.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Page<Media> findByType(Media.MediaType type, Pageable pageable);
    Page<Media> findByLanguage(String language, Pageable pageable);
    Page<Media> findByGenre(String genre, Pageable pageable);
}

package com.inpusduat.inpusduat.repository;

import com.inpusduat.inpusduat.domain.UserMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserMediaRepository extends JpaRepository<UserMedia, Long> {
    Page<UserMedia> findByUserId(Long userId, Pageable pageable);
    Page<UserMedia> findByUserIdAndStatus(Long userId, UserMedia.Status status, Pageable pageable);
    Optional<UserMedia> findByUserIdAndMediaId(Long userId, Long mediaId);
    boolean existsByUserIdAndMediaId(Long userId, Long mediaId);
}
package com.inpusduat.inpusduat.repository;

import com.inpusduat.inpusduat.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByMediaIdOrderBySeasonNumberAsc(Long mediaId);
}

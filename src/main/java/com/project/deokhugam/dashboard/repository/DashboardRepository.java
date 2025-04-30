package com.project.deokhugam.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.deokhugam.dashboard.entity.Dashboard;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

	@Modifying
	@Query("DELETE FROM Dashboard d WHERE d.type = :type AND d.period = :period")
	void deleteByTypeAndPeriod(@Param("type") String type, @Param("period") String period);

	@Query("SELECT d FROM Dashboard d WHERE d.type = :type AND d.period = :period ORDER BY d.rank ASC")
	List<Dashboard> findRankingFirstPage(@Param("type") String type, @Param("period") String period,
		org.springframework.data.domain.Pageable pageable);

	@Query("""
		SELECT d FROM Dashboard d
		WHERE d.type = :type AND d.period = :period
		AND (
		    (:direction = 'ASC' AND d.rank > :cursor AND d.createdAt > :after) OR
		    (:direction = 'DESC' AND d.rank < :cursor AND d.createdAt < :after)
		)
		ORDER BY d.rank ASC
		""")
	List<Dashboard> findRankingWithCursor(
		@Param("type") String type,
		@Param("period") String period,
		@Param("direction") String direction,
		@Param("cursor") Long cursor,
		@Param("after") java.time.LocalDateTime after,
		org.springframework.data.domain.Pageable pageable
	);

	@Query("""
		SELECT d FROM Dashboard d
		WHERE d.type = :type AND d.period = :period
		AND (
		    (:direction = 'ASC' AND d.score > :cursor AND d.createdAt > :after) OR
		    (:direction = 'DESC' AND d.score < :cursor AND d.createdAt < :after)
		)
		ORDER BY d.score ASC
		""")
	List<Dashboard> findRankingWithCursor(
		@Param("type") String type,
		@Param("period") String period,
		@Param("direction") String direction,
		@Param("cursor") Double cursor,
		@Param("after") java.time.LocalDateTime after,
		org.springframework.data.domain.Pageable pageable
	);

	long countByTypeAndPeriod(String type, String period);
}

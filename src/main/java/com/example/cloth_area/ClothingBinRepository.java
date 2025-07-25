package com.example.cloth_area;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClothingBinRepository extends JpaRepository<ClothingBin, Long> {

    // 반경 조회 Query 추가
    @Query(value = "SELECT * FROM (" +
            "SELECT *, (6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
            "cos(radians(longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(latitude)))) AS distance " +
            "FROM CLOTHING_BIN" +
            ") AS subquery " +
            "WHERE distance < :radiusKm " +
            "ORDER BY distance",
            nativeQuery = true)
    List<ClothingBin> findBinsWithinRadius(@Param("lat") double lat,
                                           @Param("lng") double lng,
                                           @Param("radiusKm") double radiusKm);

}
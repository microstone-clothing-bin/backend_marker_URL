package com.example.cloth_area;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothingBinRepository extends JpaRepository<ClothingBin, Long> {
// 필요한 커스텀 메서드가 있으면 여기에 추가
}
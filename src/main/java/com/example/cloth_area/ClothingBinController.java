// http://localhost:8081/api/clothing-bins → 백엔드 웹
// https://microstone-clothing-bin.github.io/backend_marker_URL/ → 웹 테스트 (GithubPage)

package com.example.cloth_area;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ClothingBinController {

    private final ClothingBinRepository repository;

    // 생성자 주입 방식 (필수!)
    public ClothingBinController(ClothingBinRepository repository) {
        this.repository = repository;
    }

    // Query 파라미터 메서드
    @GetMapping(value = "/clothing-bins", produces = "application/json; charset=UTF-8")
    public List<ClothingBin> getClothingBins(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) Double radiusKm) {

        if (lat != null && lng != null && radiusKm != null) {
            return repository.findBinsWithinRadius(lat, lng, radiusKm);
        } else {
            return repository.findAll();
        }
    }
}
// http://localhost:8081/api/clothing-bins → 백엔드 웹
// https://microstone-clothing-bin.github.io/backend_marker_URL/ → 웹 테스트 (GithubPage)

package com.example.cloth_area;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ClothingBinController {

    private final ClothingBinService clothingBinService; // Repository 대신 Service를 주입받음

    public ClothingBinController(ClothingBinService clothingBinService) {
        this.clothingBinService = clothingBinService;
    }

    // 전체 조회 API
    @GetMapping(value = "/clothing-bins", produces = "application/json; charset=UTF-8")
    public List<ClothingBin> getClothingBins(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) Double radiusKm) {

        // 모든 로직을 Service에 위임하고, 결과만 받아서 반환한다.
        return clothingBinService.findClothingBins(lat, lng, radiusKm);
    }

    // 프론트엔드에서 귀퉁이 좌표(swLat, swLng, neLat, neLng)를 받아서 서비스에 전달, 결과 그대로 반환하는 새로운 API
    @GetMapping("/clothing-bins/inbounds")
    public List<ClothingBin> getBinsInbounds(
            @RequestParam double swLat,
            @RequestParam double swLng,
            @RequestParam double neLat,
            @RequestParam double neLng) {
        return clothingBinService.findBinsInBounds(swLat, swLng, neLat, neLng);
    }
}
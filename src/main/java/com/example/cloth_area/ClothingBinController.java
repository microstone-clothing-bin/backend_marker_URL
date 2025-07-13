//http://localhost:8081/api/clothing-bins -> 백엔드 웹
//http://localhost:8081/webtest.html ->웹 테스트

package com.example.cloth_area;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value = "/clothing-bins", produces = "application/json; charset=UTF-8")
    public List<ClothingBin> getClothingBins() {
        return repository.findAll();
    }
}
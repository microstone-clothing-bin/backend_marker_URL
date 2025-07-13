package com.example.cloth_area;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Component
public class ClothingBinLoader implements CommandLineRunner {

    private final ClothingBinRepository repository;

    public ClothingBinLoader(ClothingBinRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        String filename = "전국_의류수거함.csv";
        System.out.println("======================================");
        System.out.println("파일 읽기 시작: " + filename);

        int savedCount = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("csv/" + filename).getInputStream(), Charset.forName("EUC-KR")))) {

            String headerLine = reader.readLine(); // 첫 줄 헤더 스킵
            if (headerLine == null) {
                System.out.println("파일이 비어있음: " + filename);
                return;
            }

            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;

                try {
                    String roadAddr = parts[0].trim();
                    String landLotAddr = parts[1].trim();
                    double lat = parseDoubleSafe(parts[2].trim());
                    double lon = parseDoubleSafe(parts[3].trim());

                    if (lat == 0 || lon == 0) continue;

                    ClothingBin bin = new ClothingBin(roadAddr, landLotAddr, lat, lon);
                    repository.save(bin);
                    savedCount++;
                } catch (Exception e) {
                    System.err.println("줄 " + lineNum + " 처리 오류: " + e.getMessage());
                }
            }
        }

        System.out.println("파일: " + filename + " - 헤더명: [도로명주소, 지번주소, 위도, 경도]");
        System.out.println("파일 읽기 완료: " + filename + " / 저장 개수: " + savedCount);
        System.out.println("======================================");
        System.out.println("총 저장된 의류수거함 개수: " + savedCount);
    }

    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }
}

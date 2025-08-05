// CSV 로직 분리, 트랜젝션 추가,(saveALL로 일괄 저장), 중복 데이터 제거, 불필요한 실행 방지, 트랜잭션 범위 축소

package com.example.cloth_area;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClothingBinLoader implements CommandLineRunner {

    private final ClothingBinRepository repository;

    public ClothingBinLoader(ClothingBinRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 존재하면 CSV 로드 및 저장 생략
        if (repository.count() > 0) {
            System.out.println("이미 의류 수거함 데이터가 존재합니다. CSV 로드를 생략합니다.");
            return;
        }

        System.out.println("======================================");
        System.out.println("CSV 파싱 시작");
        List<ClothingBin> bins = loadBinsFromCsv("csv/전국_의류수거함.csv"); // CSV 파일 파싱
        saveBins(bins); // DB에 저장 (중복 검사 포함)
        System.out.println("CSV 파싱 및 저장 완료 / 저장 개수: " + bins.size());
        System.out.println("======================================");
    }

    // @Transactional: 이 메서드 내부에서만 트랜잭션 처리
    @Transactional
    public void saveBins(List<ClothingBin> bins) {
        List<ClothingBin> filtered = new ArrayList<>();
        for (ClothingBin bin : bins) {
            // 위도와 경도로 중복 검사
            if (!repository.existsByLatitudeAndLongitude(bin.getLatitude(), bin.getLongitude())) {
                filtered.add(bin);
            }
        }

        repository.saveAll(filtered); // 중복 제거된 데이터만 저장
    }

    // CSV 파일을 읽어서 ClothingBin 리스트로 변환
    private List<ClothingBin> loadBinsFromCsv(String path) throws Exception {
        List<ClothingBin> bins = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(path).getInputStream(), Charset.forName("EUC-KR")))) {

            String headerLine = reader.readLine(); // 헤더 스킵
            if (headerLine == null) {
                throw new IllegalStateException("CSV 파일이 비어있습니다.");
            }

            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                try {
                    String[] parts = line.split(",", -1); // 공백 허용 split
                    if (parts.length < 4) continue;

                    String roadAddr = parts[0].trim();
                    String landLotAddr = parts[1].trim();
                    double lat = parseDoubleSafe(parts[2].trim());
                    double lon = parseDoubleSafe(parts[3].trim());

                    // 유효한 좌표가 아니면 생략
                    if (lat == 0 || lon == 0) continue;

                    bins.add(new ClothingBin(roadAddr, landLotAddr, lat, lon));

                } catch (Exception e) {
                    System.err.println("줄 " + lineNum + " 처리 오류: " + e.getMessage());
                }
            }
        }

        return bins;
    }

    // 문자열을 double로 안전하게 파싱 (오류 시 0 반환)
    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
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

@Component // 애플리케이션 실행 시 자동으로 실행되게 해주는 컴포넌트로 등록
public class ClothingBinLoader implements CommandLineRunner {

    private final ClothingBinRepository repository;

    // 생성자 주입 (DB 저장을 위해 ClothingBinRepository 사용)
    public ClothingBinLoader(ClothingBinRepository repository) {
        this.repository = repository;
    }

    // 애플리케이션 시작 시 실행되는 메서드
    @Override
    public void run(String... args) throws Exception {
        loadBinsWithTransaction(); // CSV 읽고 DB 저장
    }

    // 전체 작업을 트랜잭션으로 감싸서 한번에 처리
    @Transactional
    public void loadBinsWithTransaction() throws Exception {
        String filename = "전국_의류수거함.csv";
        System.out.println("======================================");
        System.out.println("파일 읽기 시작: " + filename);

        // CSV 파일을 읽어와서 객체 리스트로 변환
        List<ClothingBin> binsToSave = loadBinsFromCsv("csv/" + filename);

        // DB에 한번에 저장
        repository.saveAll(binsToSave);

        System.out.println("파일 읽기 완료: " + filename + " / 저장 개수: " + binsToSave.size());
        System.out.println("======================================");
    }

    // CSV 파일을 읽고, ClothingBin 객체 리스트로 파싱
    private List<ClothingBin> loadBinsFromCsv(String path) throws Exception {
        List<ClothingBin> bins = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(path).getInputStream(), Charset.forName("EUC-KR")))) {

            String headerLine = reader.readLine(); // 첫 줄은 헤더이므로 건너뜀
            if (headerLine == null) {
                throw new IllegalStateException("CSV 파일이 비어있습니다.");
            }

            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                try {
                    String[] parts = line.split(",", -1); // 빈 값도 포함해서 split
                    if (parts.length < 4) continue; // 필드 개수가 부족하면 건너뜀

                    String roadAddr = parts[0].trim();     // 도로명 주소
                    String landLotAddr = parts[1].trim();  // 지번 주소
                    double lat = parseDoubleSafe(parts[2].trim()); // 위도
                    double lon = parseDoubleSafe(parts[3].trim()); // 경도

                    if (lat == 0 || lon == 0) continue; // 좌표가 유효하지 않으면 건너뜀

                    // 파싱한 값으로 ClothingBin 객체 생성 후 리스트에 추가
                    bins.add(new ClothingBin(roadAddr, landLotAddr, lat, lon));

                } catch (Exception e) {
                    // 개별 줄 처리 중 오류가 발생해도 전체 멈추지 않고 로그만 출력
                    System.err.println("줄 " + lineNum + " 처리 오류: " + e.getMessage());
                }
            }
        }

        return bins;
    }

    // 문자열을 안전하게 double로 파싱 (실패하면 0 반환)
    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
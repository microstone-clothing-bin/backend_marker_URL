package com.example.cloth_area;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Component
public class ClothingBinLoader implements CommandLineRunner {

    private final ClothingBinRepository repository;

    // 생성자 주입: Repository를 주입받음
    public ClothingBinLoader(ClothingBinRepository repository) {
        this.repository = repository;
    }

    // 트랜젝션 처리된 메서드 호출
    @Override
    public void run(String... args) throws Exception {
        loadBinsWithTransaction();
    }

    @Transactional  // 트랜젝션 추가
    public void loadBinsWithTransaction() throws Exception {
        String filename = "전국_의류수거함.csv";
        System.out.println("======================================");
        System.out.println("파일 읽기 시작: " + filename);

        int savedCount = 0; // 저장 성공한 개수 카운터

        // ClassPathResource로 resource/csv 폴더 아래 파일을 EUC-KR 인코딩으로 읽음
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource("csv/" + filename).getInputStream(), Charset.forName("EUC-KR")))) {

            String headerLine = reader.readLine(); // 첫 줄 헤더 스킵
            if (headerLine == null) {
                System.out.println("파일이 비어있음: " + filename);
                return;
            }

            String line;
            int lineNum = 1; // 현재 읽고 있는 줄 번호 (헤더 제외하고 1부터)
            while ((line = reader.readLine()) != null) { // 파일 끝까지 읽기
                lineNum++;
                String[] parts = line.split(",", -1); // , 기준으로 분리, 빈 문자열도 배열에 포함
                if (parts.length < 4) continue; // 4개 미만이면 무시

                try {
                    String roadAddr = parts[0].trim(); // 도로명 주소
                    String landLotAddr = parts[1].trim(); // 지번 주소
                    double lat = parseDoubleSafe(parts[2].trim()); // 위도
                    double lon = parseDoubleSafe(parts[3].trim()); // 경도

                    if (lat == 0 || lon == 0) continue; // 위도 또는 경도가 0이면 무시

                    ClothingBin bin = new ClothingBin(roadAddr, landLotAddr, lat, lon);
                    repository.save(bin); // DB에 저장
                    savedCount++; // 저장 카운트 증가
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

    // 안전하게 문자열을 double로 파싱하는 메서드
    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0; // 실패 시 0 반환
        }
    }
}
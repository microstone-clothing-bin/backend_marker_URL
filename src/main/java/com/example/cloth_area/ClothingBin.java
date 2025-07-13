package com.example.cloth_area;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class ClothingBin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roadAddress;       // 도로명주소
    private String landLotAddress;    // 지번주소
    private double latitude;          // 위도
    private double longitude;         // 경도

    public ClothingBin() {}

    public ClothingBin(String roadAddress, String landLotAddress, double latitude, double longitude) {
        this.roadAddress = roadAddress;
        this.landLotAddress = landLotAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // getter, setter 모두 추가 (간략히 생략 가능)
    public Long getId() { return id; }
    public String getRoadAddress() { return roadAddress; }
    public String getLandLotAddress() { return landLotAddress; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public void setId(Long id) { this.id = id; }
    public void setRoadAddress(String roadAddress) { this.roadAddress = roadAddress; }
    public void setLandLotAddress(String landLotAddress) { this.landLotAddress = landLotAddress; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}





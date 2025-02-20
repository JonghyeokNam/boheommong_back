package com.sesac.boheommong.config; // 또는 com.sesac.boheommong.runner

import com.sesac.boheommong.domain.userhealthinfo.entity.UserHealthInfo;
import com.sesac.boheommong.domain.userhealthinfo.repository.UserHealthInfoRepository;
import com.sesac.boheommong.domain.insuranceproduct.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insuranceproduct.repository.InsuranceProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.Reader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvDataLoader implements CommandLineRunner {

    // CSV 파일 경로
    private final String userHealthCsvPath = "scripts/user_health_info.csv";
    private final String insuranceCsvPath = "scripts/insurance_products.csv";

    @Autowired
    private UserHealthInfoRepository userHealthRepo;

    @Autowired
    private InsuranceProductRepository productRepo;

    @Override
    public void run(String... args) throws Exception {
        // 1. 둘 중 하나라도 데이터가 있으면 Skip
        long userHealthCount = userHealthRepo.count();
        long productCount = productRepo.count();

        if (userHealthCount > 0 || productCount > 0) {
            System.out.println("이미 데이터가 존재하여 CSV Import를 Skip합니다. "
                    + "(userHealthRepo=" + userHealthCount
                    + ", productRepo=" + productCount + ")");
            return;
        }

        // 2. 데이터가 없는 경우에만 Insert
        loadUserHealthInfo("scripts/user_health_info.csv");
        loadInsuranceProducts("scripts/insurance_products.csv");
    }

    private void loadUserHealthInfo(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader());
        ) {
            List<UserHealthInfo> list = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                UserHealthInfo info = new UserHealthInfo();
                // CSV 헤더 이름과 match
                info.setUserName(record.get("user_name"));
                info.setAge(Integer.parseInt(record.get("age")));
                info.setGender(record.get("gender"));
                info.setHeight(Float.parseFloat(record.get("height")));
                info.setWeight(Float.parseFloat(record.get("weight")));
                info.setBmi(Float.parseFloat(record.get("bmi")));
                info.setIsSmoker(Integer.parseInt(record.get("is_smoker")));
                info.setIsDrinker(Integer.parseInt(record.get("is_drinker")));
                info.setHasChronicDisease(Integer.parseInt(record.get("has_chronic_disease")));
                info.setChronicDiseaseList(record.get("chronic_disease_list"));
                info.setSurgeryHistory(record.get("surgery_history"));
                info.setBloodPressure(record.get("blood_pressure"));
                info.setBloodSugar(record.get("blood_sugar"));
                list.add(info);
            }
            userHealthRepo.saveAll(list);
            System.out.println("[CsvDataLoader] user_health_info -> " + list.size() + "건 insert 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadInsuranceProducts(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader());
        ) {
            List<InsuranceProduct> productList = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                InsuranceProduct product = new InsuranceProduct();
                product.setCompanyName(record.get("company_name"));
                product.setProductName(record.get("product_name"));
                product.setProductCategory(record.get("product_category"));
                product.setCoverageDetails(record.get("coverage_details"));
                product.setMonthlyPremium(Integer.parseInt(record.get("monthly_premium")));
                product.setMinAge(Integer.parseInt(record.get("min_age")));
                product.setMaxAge(Integer.parseInt(record.get("max_age")));
                productList.add(product);
            }
            productRepo.saveAll(productList);
            System.out.println("[CsvDataLoader] insurance_products -> " + productList.size() + "건 insert 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

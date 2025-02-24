package com.sesac.boheommong.global.config;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.enums.Role;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
import com.sesac.boheommong.domain.userhealth.enums.JobType;
import com.sesac.boheommong.domain.userhealth.repository.UserHealthRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvDataLoader
 * - Spring Boot 실행 시, DB에 데이터가 없는 경우 CSV를 파싱하여 Insert
 */
@Component
public class CsvDataLoader implements CommandLineRunner {

    // CSV 파일 경로
    private final String userCsvPath       = "scripts/users.csv";
    private final String userHealthCsvPath = "scripts/user_health.csv";
    private final String insuranceCsvPath  = "scripts/insurance_products.csv";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserHealthRepository userHealthRepo;

    @Autowired
    private InsuranceProductRepository productRepo;

    @Override
    public void run(String... args) throws Exception {
        long userCount       = userRepo.count();
        long userHealthCount = userHealthRepo.count();
        long productCount    = productRepo.count();

        if (userCount > 0 || userHealthCount > 0 || productCount > 0) {
            System.out.println("[CsvDataLoader] 이미 데이터가 존재하여 CSV Import를 Skip합니다.");
            System.out.println("(userCount=" + userCount +
                    ", userHealthCount=" + userHealthCount +
                    ", productCount=" + productCount + ")");
            return;
        }

        // CSV -> DB
        loadUsers(userCsvPath);
        loadUserHealthInfo(userHealthCsvPath);
        loadInsuranceProducts(insuranceCsvPath);
    }

    /**
     * [users.csv] -> User 엔티티
     * CSV Header: user_id, loginEmail, userEmail, name, role
     * role: "ADMIN", "USER", "PARTNER" etc. (Enum)
     */
    private void loadUsers(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<User> userList = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                // user_id (AutoIncrement, 굳이 set 안함)
                String loginEmail = record.get("loginEmail");
                String userEmail  = record.get("userEmail");
                String name       = record.get("name");
                String roleStr    = record.get("role"); // e.g. "USER"

                Role role = Role.valueOf(roleStr.toUpperCase());

                // 정적 팩토리 메서드
                User user = User.create(name, loginEmail, userEmail, role);
                userList.add(user);
            }
            userRepo.saveAll(userList);
            System.out.println("[CsvDataLoader] users.csv -> " + userList.size() + "건 insert 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * [user_health_info.csv] -> UserHealth
     * CSV Header (예시):
     * health_id, user_id, age, gender, height, weight, bmi,
     * bloodPressureLevel, bloodSugarLevel, surgeryCount,
     * isSmoker, isDrinker, chronicDiseaseList,
     * jobType, hasChildren, hasOwnHouse, hasPet, hasFamilyHistory
     */
    private void loadUserHealthInfo(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<UserHealth> list = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                // 1) user_id로 User 찾기
                Long userId = Long.valueOf(record.get("user_id"));
                User user = userRepo.findById(userId)
                        .orElse(null);
                if (user == null) {
                    // user가 없으면 스킵하거나, throw
                    System.out.println("User not found for user_id=" + userId + ", 스킵");
                    continue;
                }

                UserHealth info = new UserHealth();
                info.setUser(user);

                // 2) 나머지 필드들
                info.setAge(Integer.valueOf(record.get("age")));
                info.setGender(record.get("gender")); // "M"/"F"
                info.setHeight(Integer.valueOf(record.get("height")));
                info.setWeight(Integer.valueOf(record.get("weight")));
                // BMI는 DB에 그대로 저장한다고 가정 (CSV에서 읽음)
                info.setBmi(Float.valueOf(record.get("bmi")));

                info.setBloodPressureLevel(Integer.valueOf(record.get("bloodPressureLevel")));
                info.setBloodSugarLevel(Integer.valueOf(record.get("bloodSugarLevel")));
                info.setSurgeryCount(Integer.valueOf(record.get("surgeryCount")));

                info.setIsSmoker(Boolean.valueOf(record.get("isSmoker")));
                info.setIsDrinker(Boolean.valueOf(record.get("isDrinker")));
                info.setChronicDiseaseList(record.get("chronicDiseaseList")); // "고혈압,당뇨"

                // jobType: Enum
                String jobTypeStr = record.get("jobType"); // e.g. "OFFICE"
                // 변환 로직
                if ("SELFEMP".equalsIgnoreCase(jobTypeStr)) {
                    jobTypeStr = "SELF_EMPLOYED";
                } else if ("HOMEMAKER".equalsIgnoreCase(jobTypeStr)) {
                    jobTypeStr = "HOUSEWIFE";
                }
                info.setJobType(JobType.valueOf(jobTypeStr.toUpperCase()));

                info.setHasChildren(Boolean.valueOf(record.get("hasChildren")));
                info.setHasOwnHouse(Boolean.valueOf(record.get("hasOwnHouse")));
                info.setHasPet(Boolean.valueOf(record.get("hasPet")));
                info.setHasFamilyHistory(Boolean.valueOf(record.get("hasFamilyHistory")));

                list.add(info);
            }

            userHealthRepo.saveAll(list);
            System.out.println("[CsvDataLoader] user_health_info.csv -> " + list.size() + "건 insert 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * [insurance_products.csv] -> InsuranceProduct
     * CSV Header:
     * product_id, company_name, product_name, product_type, coverage_details, monthly_premium
     */
    private void loadInsuranceProducts(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<InsuranceProduct> productList = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                InsuranceProduct product = new InsuranceProduct();
                // productId auto_increment → 굳이 set 안함

                product.setCompanyName(record.get("company_name"));
                product.setProductName(record.get("product_name"));

                // Enum 변환
                String typeStr = record.get("product_type"); // e.g. "CANCER"
                product.setProductCategory(InsuranceType.valueOf(typeStr.toUpperCase()));

                product.setCoverageDetails(record.get("coverage_details"));
                product.setMonthlyPremium(Integer.parseInt(record.get("monthly_premium")));

                productList.add(product);
            }
            productRepo.saveAll(productList);
            System.out.println("[CsvDataLoader] insurance_products.csv -> "
                    + productList.size() + "건 insert 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.sesac.boheommong.global.config;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.enums.Role;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
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

@Component
public class CsvDataLoader implements CommandLineRunner {

    // CSV 파일 경로
    private final String userCsvPath       = "scripts/users.csv";
    private final String userHealthCsvPath = "scripts/user_health_info.csv";
    private final String insuranceCsvPath  = "scripts/insurance_products.csv";

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserHealthRepository userHealthRepo;
    @Autowired
    private InsuranceProductRepository productRepo;

    @Override
    public void run(String... args) throws Exception {
        // 1) 이미 데이터가 있으면 Skip
        long userCount       = userRepo.count();
        long userHealthCount = userHealthRepo.count();
        long productCount    = productRepo.count();

        if (userCount > 0 || userHealthCount > 0 || productCount > 0) {
            System.out.println("[CsvDataLoader] 이미 데이터가 존재하여 CSV Import를 Skip합니다.");
            System.out.println(" (userCount=" + userCount
                    + ", userHealthCount=" + userHealthCount
                    + ", productCount=" + productCount + ")");
            return;
        }

        // 2) 데이터가 없는 경우에만 CSV Import
        loadUsers(userCsvPath);
        loadUserHealthInfo(userHealthCsvPath);
        loadInsuranceProducts(insuranceCsvPath);
    }

    /**
     * users.csv -> User 엔티티 Insert
     * CSV 헤더: user_id, loginEmail, userEmail, name, role
     * role: enum 문자열 ("ADMIN", "PARTNER", "USER") 등
     */
    private void loadUsers(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<User> userList = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                // CSV 예: user_id, loginEmail, userEmail, name, role
                // user_id 는 DB에서 auto_increment 라면 굳이 set 안 해도 됩니다.

                String loginEmail = record.get("loginEmail");
                String userEmail  = record.get("userEmail");
                String name       = record.get("name");
                String roleStr    = record.get("role"); // 예: "ADMIN", "PARTNER", "USER"

                // 대소문자 등 맞춰 Enum 파싱
                // CSV가 "USER", "ADMIN", "PARTNER" 라고 쓴다면 그대로 valueOf 가능
                Role role = Role.valueOf(roleStr.toUpperCase());

                // 정적 팩토리 메서드로 User 생성
                // User 엔티티에 @NoArgsConstructor(access = AccessLevel.PROTECTED)이므로
                // new User() 직업 호출 불가 -> create(...) 사용
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
     * user_health_info.csv -> UserHealthInfo 엔티티 Insert
     * CSV 헤더: health_id, user_name, age, gender, height, weight, ...
     */
    private void loadUserHealthInfo(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<UserHealth> list = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                UserHealth info = new UserHealth();
                // @GeneratedValue면 healthId는 굳이 set 안 해도 됨

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
            System.out.println("[CsvDataLoader] user_health_info.csv -> " + list.size() + "건 insert 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * insurance_products.csv -> InsuranceProduct 엔티티 Insert
     * CSV 헤더: product_id, company_name, product_name, product_category, coverage_details, ...
     */
    private void loadInsuranceProducts(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<InsuranceProduct> productList = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                InsuranceProduct product = new InsuranceProduct();
                // @GeneratedValue면 productId는 굳이 set 안 해도 됨

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
            System.out.println("[CsvDataLoader] insurance_products.csv -> "
                    + productList.size() + "건 insert 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

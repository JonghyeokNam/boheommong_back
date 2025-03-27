package com.sesac.boheommong.global.config;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * InsuranceCsvLoader
 * - Spring Boot 실행 시, DB에 보험 상품 데이터가 없으면,
 *   insurance_products.csv를 파싱하여 Insert
 */
@Component
public class InsuranceCsvLoader implements CommandLineRunner {

    // CSV 파일 경로 (필요에 맞게 수정)
    private static final String INSURANCE_CSV_PATH = "scripts/insurance_products.csv";

    private final InsuranceProductRepository productRepo;

    public InsuranceCsvLoader(InsuranceProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public void run(String... args) {
        long productCount = productRepo.count();

        if (productCount > 0) {
            System.out.println("[InsuranceCsvLoader] 이미 보험 상품 데이터가 존재하여 CSV Import를 Skip합니다.");
            System.out.println("(productCount=" + productCount + ")");
            return;
        }

        // CSV -> DB
        loadInsuranceProducts(INSURANCE_CSV_PATH);
    }

    /**
     * [insurance_products.csv] -> InsuranceProduct
     * CSV Header (예시):
     * product_id, company_name, product_name, product_type, coverage_details, monthly_premium
     */
    private void loadInsuranceProducts(String csvPath) {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(csvPath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            List<InsuranceProduct> productList = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                String companyName     = record.get("company_name");
                String productName     = record.get("product_name");
                String typeStr         = record.get("product_type");
                String coverageDetails = record.get("coverage_details");
                String premiumStr      = record.get("monthly_premium");

                // 월 보험료 정수 변환 (예: "23500" / "23,500" 등)
                // 만약 CSV에 콤마가 들어있다면 제거 후 parse
                int monthlyPremium = Integer.parseInt(premiumStr.replaceAll(",", ""));

                // InsuranceType 변환
                InsuranceType category = InsuranceType.valueOf(typeStr.toUpperCase());

                // 정적 팩토리 메서드 create(...) 사용 예시
                InsuranceProduct product = InsuranceProduct.create(
                        companyName,
                        productName,
                        category,
                        coverageDetails,
                        monthlyPremium
                );

                productList.add(product);
            }
            productRepo.saveAll(productList);
            System.out.println("[InsuranceCsvLoader] insurance_products.csv -> "
                    + productList.size() + "건 insert 완료");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

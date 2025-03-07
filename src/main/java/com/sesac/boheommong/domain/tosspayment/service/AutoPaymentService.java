package com.sesac.boheommong.domain.tosspayment.service;

import com.sesac.boheommong.domain.tosspayment.dto.AutoPaymentDto;
import com.sesac.boheommong.domain.tosspayment.entity.AutoPaymentEntity;
import com.sesac.boheommong.domain.tosspayment.repository.AutoPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutoPaymentService {

    private final AutoPaymentRepository repository;

    public AutoPaymentService(AutoPaymentRepository repository) {
        this.repository = repository;
    }

    public AutoPaymentDto createAutoPayment(AutoPaymentDto dto) {
        AutoPaymentEntity entity = new AutoPaymentEntity();
        entity.setUserId(dto.getUserId());
        entity.setDayOfMonth(dto.getDayOfMonth());
        entity.setTime(dto.getTime());

        AutoPaymentEntity saved = repository.save(entity);

        AutoPaymentDto result = new AutoPaymentDto();
        result.setId(saved.getId());
        result.setDayOfMonth(saved.getDayOfMonth());
        result.setTime(saved.getTime());
        return result;
    }

    public List<AutoPaymentDto> getAllAutoPayments() {
        List<AutoPaymentEntity> entities = repository.findAll();
        return entities.stream().map(e -> {
            AutoPaymentDto dto = new AutoPaymentDto();
            dto.setId(e.getId());
            dto.setDayOfMonth(e.getDayOfMonth());
            dto.setTime(e.getTime());
            return dto;
        }).collect(Collectors.toList());
    }

    public AutoPaymentDto updateAutoPayment(Long id, AutoPaymentDto dto) {
        AutoPaymentEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AutoPayment not found with id=" + id));

        entity.setDayOfMonth(dto.getDayOfMonth());
        entity.setTime(dto.getTime());

        AutoPaymentEntity saved = repository.save(entity);

        return AutoPaymentDto.fromEntity(saved);
    }

    public void deleteAutoPayment(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AutoPayment not found with id=" + id));
        repository.deleteById(id);
    }
}

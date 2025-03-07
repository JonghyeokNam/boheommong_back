package com.sesac.boheommong.domain.tosspayment.controller;

import com.sesac.boheommong.domain.tosspayment.dto.AutoPaymentDto;
import com.sesac.boheommong.domain.tosspayment.service.AutoPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AutoPaymentController {

    private final AutoPaymentService autoPaymentService;

    public AutoPaymentController(AutoPaymentService autoPaymentService) {
        this.autoPaymentService = autoPaymentService;
    }

    @PostMapping("/autoPayment")
    public ResponseEntity<AutoPaymentDto> createAutoPayment(@RequestBody AutoPaymentDto dto) {
        AutoPaymentDto saved = autoPaymentService.createAutoPayment(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/autoPayment")
    public ResponseEntity<List<AutoPaymentDto>> getAutoPayments() {
        List<AutoPaymentDto> list = autoPaymentService.getAllAutoPayments();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/autoPayment/{id}")
    public ResponseEntity<AutoPaymentDto> updateAutoPayment(
            @PathVariable Long id,
            @RequestBody AutoPaymentDto dto) {
        AutoPaymentDto updated = autoPaymentService.updateAutoPayment(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/autoPayment/{id}")
    public ResponseEntity<Void> deleteAutoPayment(@PathVariable Long id) {
        autoPaymentService.deleteAutoPayment(id);
        return ResponseEntity.noContent().build();
    }
}

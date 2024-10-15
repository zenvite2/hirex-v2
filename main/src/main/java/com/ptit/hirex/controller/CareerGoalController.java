package com.ptit.hirex.controller;

import com.ptit.hirex.dto.request.CareerGoalRequest;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.service.CareerGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/career-goal")
public class CareerGoalController {

    private final CareerGoalService careerGoalService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Object>> createCareerGoal( @Valid @RequestBody CareerGoalRequest careerGoalRequest) {
        return careerGoalService.createCareerGoal(careerGoalRequest);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> updateCareerGoal(@PathVariable Long id, @Valid @RequestBody CareerGoalRequest careerGoalRequest) {
        return careerGoalService.updateCareerGoal(id, careerGoalRequest);
    }

    @GetMapping()
    public ResponseEntity<ResponseDto<Object>> getCareerGoal() {
        return careerGoalService.getCareerGoal();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Object>> deleteCareerGoal(@PathVariable Long id) {
        return careerGoalService.deleteCareerGoal(id);
    }
}

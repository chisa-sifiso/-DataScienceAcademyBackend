package com.datascienceacademy.backenddatascienceacademy.controller;

import com.datascienceacademy.backenddatascienceacademy.dto.ApplicationDto;
import com.datascienceacademy.backenddatascienceacademy.dto.ModuleDto;
import com.datascienceacademy.backenddatascienceacademy.model.ApplicationForm;
import com.datascienceacademy.backenddatascienceacademy.service.ApplicationFormService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
public class ApplicationFormController {

    private final ApplicationFormService service;

    public ApplicationFormController(ApplicationFormService service) {
        this.service = service;
    }

    @Operation(summary = "Submit a new application")
    @PostMapping
    public ApplicationForm submitApplication(
            @Valid @RequestBody ApplicationForm form
    ) {
        form.setSubmissionDate(LocalDate.now());
        form.setApplicationStatus("Pending");

        if (form.getCvFile() != null && !form.getCvFile().isBlank()) {
            try {
                Base64.getDecoder().decode(form.getCvFile());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid base64 CV content", e);
            }
        }

        if (form.getFinalYearModules() != null) {
            form.getFinalYearModules().forEach(mm -> mm.setApplicationForm(form));
        }

        return service.save(form);
    }

    @Operation(summary = "Get all applications (raw entity)")
    @GetMapping
    public List<ApplicationForm> getAllApplicationsRaw() {
        return service.getAll();
    }

    @Operation(summary = "Get all applications (DTO projection)")
    @GetMapping("/nocv")
    public List<ApplicationDto> getAllApplicationsDto() {
        return service.getAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ApplicationDto toDto(ApplicationForm form) {
        List<ModuleDto> modules = form.getFinalYearModules().stream()
                .map(mm -> new ModuleDto(mm.getId(), mm.getModuleName(), mm.getMark()))
                .collect(Collectors.toList());

        return new ApplicationDto(
                form.getId(), form.getApplicationId(), form.getSubmissionDate(),
                form.getFullName(), form.getIdNumber(), form.getEmail(), form.getPhoneNumber(),
                form.getGender(), form.getNationality(), form.getProvince(), form.getCity(),
                form.getHighestQualification(), form.getInstitution(), form.getYearCompleted(),
                form.getCurrentStatus(), form.getGithubUrl(), form.getLinkedinUrl(),
                form.getHowDidYouHearAboutUs(), form.getMotivationLetter(),
                form.isWillingToRelocate(), form.getPreferredLocation(),
                form.getApplicationStatus(), form.getRelevantSkillsCount(),
                form.getCourseCode(), modules
        );
    }

    @Operation(summary = "Get application by database ID")
    @GetMapping("/{id}")
    public ApplicationForm getApplication(@PathVariable Long id) {
        return service.getById(id);
    }


    @Operation(summary = "Update only the applicationStatus field")
    @PatchMapping("/{id}/status")
    public ApplicationForm updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdate statusUpdate
    ) {
        return service.updateStatus(id, statusUpdate.getStatus());
    }

    public static class StatusUpdate {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    @GetMapping("/home")
    public String hello() {
        return "hello";
    }
}

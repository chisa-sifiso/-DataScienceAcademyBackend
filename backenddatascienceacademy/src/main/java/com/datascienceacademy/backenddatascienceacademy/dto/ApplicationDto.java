package com.datascienceacademy.backenddatascienceacademy.dto;

import java.time.LocalDate;
import java.util.List;

public class ApplicationDto {
    private Long id;
    private String applicationId;
    private LocalDate submissionDate;
    private String fullName;
    private String idNumber;
    private String email;
    private String phoneNumber;
    private String gender;
    private String nationality;
    private String province;
    private String city;
    private String highestQualification;
    private String institution;
    private String yearCompleted;
    private String currentStatus;
    private String githubUrl;
    private String linkedinUrl;
    private String howDidYouHearAboutUs;
    private String motivationLetter;
    private boolean willingToRelocate;
    private String preferredLocation;
    private String applicationStatus;
    private int relevantSkillsCount;
    private String courseCode;
    private List<ModuleDto> finalYearModules;

    public ApplicationDto(
            Long id, String applicationId, LocalDate submissionDate,
            String fullName, String idNumber, String email, String phoneNumber,
            String gender, String nationality, String province, String city,
            String highestQualification, String institution, String yearCompleted,
            String currentStatus, String githubUrl, String linkedinUrl,
            String howDidYouHearAboutUs, String motivationLetter,
            boolean willingToRelocate, String preferredLocation,
            String applicationStatus, int relevantSkillsCount,
            String courseCode, List<ModuleDto> finalYearModules
    ) {
        this.id = id;
        this.applicationId = applicationId;
        this.submissionDate = submissionDate;
        this.fullName = fullName;
        this.idNumber = idNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.nationality = nationality;
        this.province = province;
        this.city = city;
        this.highestQualification = highestQualification;
        this.institution = institution;
        this.yearCompleted = yearCompleted;
        this.currentStatus = currentStatus;
        this.githubUrl = githubUrl;
        this.linkedinUrl = linkedinUrl;
        this.howDidYouHearAboutUs = howDidYouHearAboutUs;
        this.motivationLetter = motivationLetter;
        this.willingToRelocate = willingToRelocate;
        this.preferredLocation = preferredLocation;
        this.applicationStatus = applicationStatus;
        this.relevantSkillsCount = relevantSkillsCount;
        this.courseCode = courseCode;
        this.finalYearModules = finalYearModules;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDate submissionDate) { this.submissionDate = submissionDate; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getHighestQualification() { return highestQualification; }
    public void setHighestQualification(String highestQualification) { this.highestQualification = highestQualification; }
    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }
    public String getYearCompleted() { return yearCompleted; }
    public void setYearCompleted(String yearCompleted) { this.yearCompleted = yearCompleted; }
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    public String getHowDidYouHearAboutUs() { return howDidYouHearAboutUs; }
    public void setHowDidYouHearAboutUs(String howDidYouHearAboutUs) { this.howDidYouHearAboutUs = howDidYouHearAboutUs; }
    public String getMotivationLetter() { return motivationLetter; }
    public void setMotivationLetter(String motivationLetter) { this.motivationLetter = motivationLetter; }
    public boolean isWillingToRelocate() { return willingToRelocate; }
    public void setWillingToRelocate(boolean willingToRelocate) { this.willingToRelocate = willingToRelocate; }
    public String getPreferredLocation() { return preferredLocation; }
    public void setPreferredLocation(String preferredLocation) { this.preferredLocation = preferredLocation; }
    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }
    public int getRelevantSkillsCount() { return relevantSkillsCount; }
    public void setRelevantSkillsCount(int relevantSkillsCount) { this.relevantSkillsCount = relevantSkillsCount; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public List<ModuleDto> getFinalYearModules() { return finalYearModules; }
    public void setFinalYearModules(List<ModuleDto> finalYearModules) { this.finalYearModules = finalYearModules; }
}

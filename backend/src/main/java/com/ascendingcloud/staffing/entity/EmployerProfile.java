package com.ascendingcloud.staffing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employer_profiles")
public class EmployerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 30)
    private String phone;

    @Column(length = 100)
    private String designation;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "primary_contact_name", length = 150)
    private String primaryContactName;

    @Column(name = "contact_email", length = 150)
    private String contactEmail;

    @Column(name = "hiring_locations", length = 255)
    private String hiringLocations;

    @Column(name = "employee_types_needed", length = 255)
    private String employeeTypesNeeded;

    @Column(name = "preferred_work_arrangements", length = 255)
    private String preferredWorkArrangements;

    @Column(name = "primary_roles", length = 500)
    private String primaryRoles;

    @Column(name = "expected_hires")
    private Integer expectedHires;

    @Column(name = "hiring_timeline", length = 100)
    private String hiringTimeline;

    @Column(name = "billing_address", length = 500)
    private String billingAddress;

    @Column(name = "selected_plan", length = 100)
    private String selectedPlan;

    @Column(name = "preferred_interview_method", length = 50)
    private String preferredInterviewMethod;

    @Column(name = "placement_terms_accepted")
    private Boolean placementTermsAccepted;

    @Column(name = "notification_preferences", length = 255)
    private String notificationPreferences;

    public EmployerProfile() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getHiringLocations() {
        return hiringLocations;
    }

    public void setHiringLocations(String hiringLocations) {
        this.hiringLocations = hiringLocations;
    }

    public String getEmployeeTypesNeeded() {
        return employeeTypesNeeded;
    }

    public void setEmployeeTypesNeeded(String employeeTypesNeeded) {
        this.employeeTypesNeeded = employeeTypesNeeded;
    }

    public String getPreferredWorkArrangements() {
        return preferredWorkArrangements;
    }

    public void setPreferredWorkArrangements(String preferredWorkArrangements) {
        this.preferredWorkArrangements = preferredWorkArrangements;
    }

    public String getPrimaryRoles() {
        return primaryRoles;
    }

    public void setPrimaryRoles(String primaryRoles) {
        this.primaryRoles = primaryRoles;
    }

    public Integer getExpectedHires() {
        return expectedHires;
    }

    public void setExpectedHires(Integer expectedHires) {
        this.expectedHires = expectedHires;
    }

    public String getHiringTimeline() {
        return hiringTimeline;
    }

    public void setHiringTimeline(String hiringTimeline) {
        this.hiringTimeline = hiringTimeline;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(String selectedPlan) {
        this.selectedPlan = selectedPlan;
    }

    public String getPreferredInterviewMethod() {
        return preferredInterviewMethod;
    }

    public void setPreferredInterviewMethod(String preferredInterviewMethod) {
        this.preferredInterviewMethod = preferredInterviewMethod;
    }

    public Boolean getPlacementTermsAccepted() {
        return placementTermsAccepted;
    }

    public void setPlacementTermsAccepted(Boolean placementTermsAccepted) {
        this.placementTermsAccepted = placementTermsAccepted;
    }

    public String getNotificationPreferences() {
        return notificationPreferences;
    }

    public void setNotificationPreferences(String notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }
}

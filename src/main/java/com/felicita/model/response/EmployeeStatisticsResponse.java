package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeStatisticsResponse {

    // =========================================
    // KPI SUMMARY CARDS
    // =========================================
    private KpiSummary kpiSummary;

    // =========================================
    // CHART DATA
    // =========================================
    private List<DepartmentWiseEmployees> departmentWiseEmployees;
    private List<EmployeeTypeDistribution> employeeTypeDistribution;
    private List<WorkLocationDistribution> workLocationDistribution;
    private List<EmployeeGradeDistribution> employeeGradeDistribution;
    private List<MonthlyHiringTrend> monthlyHiringTrend;
    private List<SalaryByDepartment> salaryByDepartment;
    private List<PerformanceRatingDistribution> performanceRatingDistribution;
    private List<SkillDistribution> skillDistribution;
    private List<AssetDistribution> assetDistribution;
    private List<ShiftDistribution> shiftDistribution;

    // =========================================
    // KPI SUMMARY
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KpiSummary {
        private Long totalEmployees;
        private Long activeEmployees;
        private Long inactiveEmployees;
        private Long employeesWithoutSupervisor;
        private Long employeesJoinedThisMonth;
        private BigDecimal averageRating;
        private Long totalAssets;
    }

    // =========================================
    // 1. Department Wise Employees
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentWiseEmployees {
        private String departmentName;
        private Long employeeCount;
    }

    // =========================================
    // 2. Employee Type Distribution
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeTypeDistribution {
        private String employeeType;
        private Long employeeCount;
    }

    // =========================================
    // 3. Work Location Distribution
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkLocationDistribution {
        private String workLocation;
        private Long employeeCount;
    }

    // =========================================
    // 4. Employee Grade Distribution
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeGradeDistribution {
        private String employeeGrade;
        private Long employeeCount;
    }

    // =========================================
    // 5. Monthly Hiring Trend
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyHiringTrend {
        private String month;
        private Long hiredCount;
    }

    // =========================================
    // 6. Salary by Department
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalaryByDepartment {
        private String departmentName;
        private BigDecimal averageSalary;
        private BigDecimal totalSalary;
    }

    // =========================================
    // 7. Performance Rating Distribution
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceRatingDistribution {
        private Integer ratingGroup;
        private Long totalReviews;
    }

    // =========================================
    // 8. Skill Distribution
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillDistribution {
        private String skillName;
        private Long employeeCount;
    }

    // =========================================
    // 9. Asset Distribution
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetDistribution {
        private String assetType;
        private Long totalAssets;
    }

    // =========================================
    // 10. Shift Distribution
    // =========================================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShiftDistribution {
        private String shiftName;
        private Long employeeCount;
    }
}
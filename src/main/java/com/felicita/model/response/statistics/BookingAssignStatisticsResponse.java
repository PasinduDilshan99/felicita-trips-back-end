package com.felicita.model.response.statistics;

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
public class BookingAssignStatisticsResponse {

    private Summary summary;

    private List<EmployeeWorkload> employeeWorkloads;

    private List<EmployeeRevenue> employeeRevenues;

    private List<DepartmentDistribution> departmentDistributions;

    private List<DesignationDistribution> designationDistributions;

    private List<MonthlyAssignmentTrend> monthlyAssignmentTrends;

    private List<AssignmentStatusDistribution> assignmentStatusDistributions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Long totalBookings;

        private Long assignedBookings;

        private Long unassignedBookings;

        private Long totalAssignedEmployees;

        private BigDecimal averageBookingsPerEmployee;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmployeeWorkload {

        private Long employeeId;

        private Long userId;

        private String employeeName;

        private String designationName;

        private String departmentName;

        private Long totalBookings;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmployeeRevenue {

        private Long employeeId;

        private Long userId;

        private String employeeName;

        private Long totalBookings;

        private BigDecimal totalRevenue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DepartmentDistribution {

        private Long departmentId;

        private String departmentName;

        private Long totalBookings;

        private BigDecimal percentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DesignationDistribution {

        private Long designationId;

        private String designationName;

        private Long totalBookings;

        private BigDecimal percentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MonthlyAssignmentTrend {

        private Integer year;

        private Integer month;

        private Long totalAssignedBookings;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AssignmentStatusDistribution {

        private String assignmentType;

        private Long totalBookings;

        private BigDecimal percentage;
    }
}
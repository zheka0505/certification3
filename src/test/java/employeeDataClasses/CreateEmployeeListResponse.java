package employeeDataClasses;

public record CreateEmployeeListResponse(int id, String firstName, String lastName, String middleName, int companyId, String email, String url, String phone, String birthdate, boolean isActive) {
}

package employeeDataClasses;

public record ChangeEmployeeDataRequest(String lastName, String email, String url, String phone, boolean isActive) {
}

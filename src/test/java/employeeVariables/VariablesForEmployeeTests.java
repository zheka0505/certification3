package employeeVariables;

import employeeDataClasses.ChangeEmployeeDataRequest;
import employeeDataClasses.CreateCompanyRequest;
import employeeDataClasses.CreateEmployeeRequest;

import java.io.IOException;

import static employeeRestAssuredMethods.APIRequests.createNewCompany;


public class VariablesForEmployeeTests {

    public static final CreateCompanyRequest NEW_COMPANY_DATA = new CreateCompanyRequest("ООО Цветочек", "Новая компания");

    public static final int NEGATIVE_ID = -5;

    public static int NEW_COMPANY;

    static {
        try {
            NEW_COMPANY = createNewCompany();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static CreateEmployeeRequest NEW_EMPLOYEE_RUSSIAN = new CreateEmployeeRequest(0, "Иван", "Иванов", "Иванович", NEW_COMPANY, "ivan@mail.ru", "ivan.ru", "1235465", "2024-08-25T11:07:11.228Z", true);

    public static CreateEmployeeRequest NEW_EMPLOYEE_LATIN = new CreateEmployeeRequest(0, "Ivan", "Ivanov", "Ivanovich", NEW_COMPANY, "ivan_english@mail.ru", "ivan_english.ru", "777777", "2001-08-10T11:07:11.228Z", false);

    public static CreateEmployeeRequest NEGATIVE_EMPLOYEE_COMPANY_DOES_NT_EXIST = new CreateEmployeeRequest(0, "Иван", "Иванов", "Иванович", NEGATIVE_ID, "ivan@mail.ru", "ivan.ru", "1235465", "2024-08-25T11:07:11.228Z", true);

    public static ChangeEmployeeDataRequest CHANGED_DATA_EMPLOYEE_RUSSIAN = new ChangeEmployeeDataRequest("Петров", "petrov@mail.ru", "petrov.ru", "555554", false);




}



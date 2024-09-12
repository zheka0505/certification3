package employeeTests;

import DBConnection.EmployeeEntity;
import DBConnection.MyPUI;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import static services.DBQuery.*;
import static services.RestAssuredRequests.createNewCompanyWithEmployees;

import static employeeVariables.LoginData.getProperties;
import static employeeVariables.LoginData.url;
import static employeeVariables.VariablesForEmployeeTests.*;
import static employeeVariables.VariablesForEmployeeTests.EMPLOYEE_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBTests {

    private static EntityManager entityManager;

    @BeforeAll
    public static void setUp() throws IOException {
        Properties properties = getProperties();
        PersistenceUnitInfo pui = new MyPUI(properties);

        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        EntityManagerFactory emf = hibernatePersistenceProvider.createContainerEntityManagerFactory(pui, pui.getProperties());
        entityManager = emf.createEntityManager();

        RestAssured.baseURI = url();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Проверка списка сотрудников")
    public void getListOfEmployeesDB() throws IOException {
        TypedQuery<EmployeeEntity> query = entityManager.createQuery("SELECT ce FROM EmployeeEntity ce WHERE ce.companyId = :companyId", EmployeeEntity.class);
        query.setParameter("companyId", createNewCompanyWithEmployees());

        List<EmployeeEntity> employee = query.getResultList();

        assertTrue(employee.get(0).getLastName().contains("Смирнов"));
        assertTrue(employee.get(1).getLastName().contains("Landson"));

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Проверка пустого списка сотрудников")
    public void getEmptyListOfEmployeesDB() throws IOException {
        TypedQuery<EmployeeEntity> query = entityManager.createQuery("SELECT ce FROM EmployeeEntity ce WHERE ce.companyId = :companyId", EmployeeEntity.class);
        query.setParameter("companyId", createNewCompanyDB());

        List<EmployeeEntity> employee = query.getResultList();

        assertEquals(0, employee.size());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение сотрудника по id, только обязательные поля")
    public void getEmployeeByIdRequiredFields() throws IOException {

        EmployeeEntity employeeFromDB = getEmployeeByIdDB(createEmployeeDB(new EmployeeEntity(), russianDB).getId());

        assertEquals(RUSSIAN_NAME, employeeFromDB.getFirstName());
        assertEquals(RUSSIAN_LASTNAME, employeeFromDB.getLastName());
        assertEquals(EMPLOYEE_PHONE, employeeFromDB.getPhone());
        assertTrue(employeeFromDB.isActive());

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Получение сотрудника, заполнены все поля")
    public void getEmployeeFullData() throws IOException {

        EmployeeEntity employeeFromDB = getEmployeeByIdDB(createEmployeeDB(new EmployeeEntity(), fullFieldsRussianDB).getId());

        String toStringBirthday = new SimpleDateFormat("yyyy-MM-dd").format(employeeFromDB.getBirthdate());

        assertEquals(RUSSIAN_NAME, employeeFromDB.getFirstName());
        assertEquals(RUSSIAN_LASTNAME, employeeFromDB.getLastName());
        assertEquals(EMPLOYEE_PHONE, employeeFromDB.getPhone());
        assertEquals(RUSSIAN_MIDDLE_NAME, employeeFromDB.getMiddleName());
        assertEquals(EMPLOYEE_BIRTHDAY, toStringBirthday);
        assertEquals(EMPLOYEE_URL, employeeFromDB.getAvatar_url());
        assertEquals(EMPLOYEE_EMAIL, employeeFromDB.getEmail());

    }


}

package employeeTests;

import employeeDBConnectionClasses.EmployeeEntity;
import employeeDBConnectionClasses.MyPUI;
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
import java.util.List;
import java.util.Properties;

import static employeeRestAssuredMethods.APIRequests.createNewCompany;
import static employeeRestAssuredMethods.APIRequests.createNewCompanyWithEmployees;
import static employeeVariables.LoginData.getProperties;
import static employeeVariables.LoginData.url;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBTests {

    private static EntityManager entityManager;

    @BeforeAll
    public static void setUp() throws IOException {
        RestAssured.baseURI = url();

        Properties properties = getProperties();
        PersistenceUnitInfo pui = new MyPUI(properties);

        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        EntityManagerFactory entityManagerFactory = hibernatePersistenceProvider.createContainerEntityManagerFactory(pui, pui.getProperties());
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Проверка списка сотрудников из БД")
    public void getListOfEmployeesDB() throws IOException {
        TypedQuery<EmployeeEntity> query = entityManager.createQuery("SELECT ce FROM EmployeeEntity ce WHERE ce.companyId = :companyId", EmployeeEntity.class);
        query.setParameter("companyId", createNewCompanyWithEmployees());

        List<EmployeeEntity> employee = query.getResultList();

        assertTrue(employee.get(0).getLastName().contains("Иванов"));
        assertTrue(employee.get(1).getLastName().contains("Ivanov"));

    }

    @Test
    @Tag("Позитивный")
    @DisplayName("Проверка пустого списка сотрудников из БД")
    public void getEmptyListOfEmployeesDB() throws IOException {
        TypedQuery<EmployeeEntity> query = entityManager.createQuery("SELECT ce FROM EmployeeEntity ce WHERE ce.companyId = :companyId", EmployeeEntity.class);
        query.setParameter("companyId", createNewCompany());

        List<EmployeeEntity> employee = query.getResultList();

        assertEquals(0, employee.size());

    }

}

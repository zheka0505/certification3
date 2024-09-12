package services;

import DBConnection.EmployeeEntity;
import DBConnection.MyPUI;
import DBConnection.CompanyEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.io.IOException;
import java.sql.Date;
import java.util.Properties;
import java.util.function.Function;


import static employeeVariables.LoginData.getProperties;
import static employeeVariables.VariablesForEmployeeTests.*;



public class DBQuery {

    private static EntityManager entityManager;

    public static void connectionDB() throws IOException {
        Properties properties = getProperties();
        PersistenceUnitInfo pui = new MyPUI(properties);

        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        EntityManagerFactory entityManagerFactory = hibernatePersistenceProvider.createContainerEntityManagerFactory(pui, pui.getProperties());
        entityManager = entityManagerFactory.createEntityManager();
    }

    public static Function<EmployeeEntity, EmployeeEntity> russianDB = new Function<EmployeeEntity, EmployeeEntity>() {
        @Override
        public EmployeeEntity apply(EmployeeEntity e) {
            e.setFirstName(RUSSIAN_NAME);
            e.setLastName(RUSSIAN_LASTNAME);
            e.setCompanyId(NEW_COMPANY);
            e.setPhone(EMPLOYEE_PHONE);
            e.setActive(true);
            return e;
        }
    };

    public static Function<EmployeeEntity, EmployeeEntity> latinDB = new Function<EmployeeEntity, EmployeeEntity>() {
        @Override
        public EmployeeEntity apply(EmployeeEntity e) {
            e.setFirstName(LATIN_NAME);
            e.setLastName(LATIN_LASTNAME );
            e.setCompanyId(NEW_COMPANY);
            e.setPhone(EMPLOYEE_PHONE);
            e.setActive(true);
            return e;
        }
    };

    public static Function<EmployeeEntity, EmployeeEntity> fullFieldsRussianDB = new Function<EmployeeEntity, EmployeeEntity>() {
        @Override
        public EmployeeEntity apply(EmployeeEntity e) {
            e.setFirstName(RUSSIAN_NAME);
            e.setLastName(RUSSIAN_LASTNAME);
            e.setMiddleName(RUSSIAN_MIDDLE_NAME);
            e.setBirthdate(Date.valueOf(EMPLOYEE_BIRTHDAY));
            e.setEmail(EMPLOYEE_EMAIL);
            e.setCompanyId(NEW_COMPANY);
            e.setPhone(EMPLOYEE_PHONE);
            e.setActive(true);
            e.setAvatar_url(EMPLOYEE_URL);
            return e;
        }
    };

    public static Function<EmployeeEntity, EmployeeEntity> specialCharactersDB = new Function<EmployeeEntity, EmployeeEntity>() {
        @Override
        public EmployeeEntity apply(EmployeeEntity e) {
            e.setFirstName(SPECIAL_CHARACTERS);
            e.setLastName(SPECIAL_CHARACTERS);
            e.setMiddleName(SPECIAL_CHARACTERS);
            e.setCompanyId(NEW_COMPANY);
            e.setPhone(EMPLOYEE_PHONE);
            e.setActive(true);
            return e;
        }
    };

    public static EmployeeEntity createEmployeeDB(EmployeeEntity employee, Function<EmployeeEntity, EmployeeEntity> rule) throws IOException {
        connectionDB();

        rule.apply(employee);
        entityManager.getTransaction().begin();
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
        return employee;
    }

    public static int createNewCompanyDB() throws IOException {
        connectionDB();

        CompanyEntity newCompany = new CompanyEntity();
        newCompany.setName(COMPANY_NAME);
        newCompany.setActive(true);


        entityManager.getTransaction().begin();
        entityManager.persist(newCompany);
        entityManager.getTransaction().commit();
        return newCompany.getId();
    }

    public static EmployeeEntity getEmployeeByIdDB(int employeeID) throws IOException {

        connectionDB();
        return entityManager.find(EmployeeEntity.class, employeeID);
    }

}

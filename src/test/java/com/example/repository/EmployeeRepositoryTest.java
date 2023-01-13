package com.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.example.domain.Employee;

@JdbcTest
public class EmployeeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {
        this.employeeRepository = new EmployeeRepositoryImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("Repository Test - Get all employees")
    @Sql({ "schema.sql", "test-data.sql" })
    public void testGetAllEmployees() {
        List<Employee> list = employeeRepository.getAllEmployees();

        assertEquals(1, list.size());

        assertEquals(1, list.get(0).getId());
        assertEquals("Grace", list.get(0).getFirstName());
        assertEquals("Hopper", list.get(0).getLastName());
        assertEquals(20, list.get(0).getAge());
        assertEquals("ghopper@inthenavy.us", list.get(0).getEmail());
        assertEquals(new BigDecimal(1000), list.get(0).getMonthlySalary());
    }

    @Test
    @DisplayName("Repository Test - Get employee by id")
    @Sql({ "schema.sql", "test-data.sql" })
    public void testGetEmployeeById() {
        Employee employee = employeeRepository.getEmployeeById(1);

        assertEquals(1, employee.getId());
        assertEquals("Grace", employee.getFirstName());
        assertEquals("Hopper", employee.getLastName());
        assertEquals(20, employee.getAge());
        assertEquals("ghopper@inthenavy.us", employee.getEmail());
        assertEquals(new BigDecimal(1000), employee.getMonthlySalary());
    }

    @Test
    @DisplayName("Repository Test - Get employee by id with exception")
    @Sql({ "schema.sql", "test-data.sql" })
    public void testGetEmployeeByIdWithException() {
        Employee employee = employeeRepository.getEmployeeById(2);

        assertNull(employee);
    }

    @Test
    @DisplayName("Repository Test - add employee")
    @Sql({ "schema.sql", "test-data.sql" })
    public void testAddEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Jack");
        employee.setLastName("Lee");
        employee.setAge(30);
        employee.setEmail("jack@example.com");
        employee.setMonthlySalary(new BigDecimal(2000));

        employeeRepository.addEmployee(employee);

        List<Employee> list = employeeRepository.getAllEmployees();

        assertEquals(2, list.size());
        assertEquals(2, employee.getId());
    }

    @Test
    @DisplayName("Repository Test - update employee")
    @Sql({ "schema.sql", "test-data.sql" })
    public void testUpdateEmployee() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Grace");
        employee.setLastName("Hopper");
        employee.setAge(30);
        employee.setEmail("ghopper@inthenavy.com");
        employee.setMonthlySalary(new BigDecimal(2000));

        employeeRepository.updateEmployee(employee);

        employee = employeeRepository.getEmployeeById(1);

        assertEquals(1, employee.getId());
        assertEquals("Grace", employee.getFirstName());
        assertEquals("Hopper", employee.getLastName());
        assertEquals(30, employee.getAge());
        assertEquals("ghopper@inthenavy.com", employee.getEmail());
        assertEquals(new BigDecimal(2000), employee.getMonthlySalary());
    }

    @Test
    @DisplayName("Repository Test - delete employee by id")
    @Sql({ "schema.sql", "test-data.sql" })
    public void testDeleteEmployeeById() {
        List<Employee> employees = employeeRepository.getAllEmployees();
        assertEquals(1, employees.size());
        assertEquals(1, employees.get(0).getId());

        employeeRepository.deleteEmployeeById(1);

        employees = employeeRepository.getAllEmployees();
        assertEquals(0, employees.size());
    }

}

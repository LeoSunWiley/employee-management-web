package com.example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Employee;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final Logger LOG = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Employee> getAllEmployees() {
        final String SELECT_ALL_EMPLOYEES = "SELECT * FROM employee";
        return jdbcTemplate.query(SELECT_ALL_EMPLOYEES, new EmployeeMapper());
    }

    @Override
    public Employee getEmployeeById(int id) {
        try {
            final String SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employee WHERE id = ?";
            return jdbcTemplate.queryForObject(SELECT_EMPLOYEE_BY_ID, new EmployeeMapper(), id);
        } catch (DataAccessException ex) {
            LOG.info("Employee with id " + id + " not found.");
            return null;
        }
    }

    @Override
    @Transactional
    public Employee addEmployee(Employee employee) {
        final String INSERT_EMPLOYEE = "INSERT INTO employee(first_name, last_name, age, email, monthly_salary) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(INSERT_EMPLOYEE, employee.getFirstName(), employee.getLastName(), employee.getAge(), employee.getEmail(), employee.getMonthlySalary());

        Integer newId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        if (newId != null) {
            employee.setId(newId);
        }
        return employee;
    }

    @Override
    public void updateEmployee(Employee employee) {
        LOG.info("employee age " + employee.getAge());
        final String UPDATE_EMPLOYEE = "UPDATE employee SET first_name = ?, last_name = ?, age = ?, email = ?, monthly_salary = ? WHERE id = ?";
        jdbcTemplate.update(UPDATE_EMPLOYEE, employee.getFirstName(), employee.getLastName(), employee.getAge(), employee.getEmail(), employee.getMonthlySalary(), employee.getId());
    }

    @Override
    public void deleteEmployeeById(int id) {
        final String DELETE_EMPLOYEE = "DELETE FROM employee WHERE id = ?";
        jdbcTemplate.update(DELETE_EMPLOYEE, id);
    }

    private static final class EmployeeMapper implements RowMapper<Employee> {

        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setId(rs.getInt("id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            employee.setAge(rs.getInt("age"));
            employee.setEmail(rs.getString("email"));
            employee.setMonthlySalary(rs.getBigDecimal("monthly_salary"));

            return employee;
        }
    }
    
}

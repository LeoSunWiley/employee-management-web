package com.example.repository;

import java.util.List;

import com.example.domain.Employee;

public interface EmployeeRepository {
    
    List<Employee> getAllEmployees();

    Employee getEmployeeById(int id);

    Employee addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployeeById(int id);
}

package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public Employee getEmployee(int id) {
        return employeeRepository.getEmployeeById(id);
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.addEmployee(employee);
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.updateEmployee(employee);
    }

    public void deleteEmployeeById(int id) {
        employeeRepository.deleteEmployeeById(id);
    }
}

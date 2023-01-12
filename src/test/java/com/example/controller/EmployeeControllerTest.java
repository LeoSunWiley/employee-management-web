package com.example.controller;

import com.example.domain.Employee;
import com.example.service.EmployeeService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeService employeeService;

	@Test
	@DisplayName("Controller Test - Get all employees")
	public void testGetAllEmployees() throws Exception {
		Employee employee = new Employee();
		employee.setId(1);
		employee.setFirstName("Grace");
		employee.setLastName("Hopper");
		employee.setAge(20);
		employee.setEmail("ghopper@inthenavy.us");
		employee.setMonthlySalary(new BigDecimal(1000));

		List<Employee> employeeList = Arrays.asList(employee);
		when(employeeService.getAllEmployees()).thenReturn(employeeList);

		mockMvc.perform(get("/employees")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].firstName").value("Grace"))
				.andExpect(jsonPath("$[0].lastName").value("Hopper")).andExpect(jsonPath("$[0].age").value(20))
				.andExpect(jsonPath("$[0].email").value("ghopper@inthenavy.us"))
				.andExpect(jsonPath("$[0].monthlySalary").value(new BigDecimal(1000)));
	}

	@Test
    @DisplayName("Controller Test - Get employee with exception")
    public void testGetEmployeeWithException() throws Exception {
        when(employeeService.getEmployee(1)).thenReturn(null);

        mockMvc.perform(get("/employees/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

	@Test
	@DisplayName("Controller Test - Get employee")
	public void testGetEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setId(1);
		employee.setFirstName("Grace");
		employee.setLastName("Hopper");
		employee.setAge(20);
		employee.setEmail("ghopper@inthenavy.us");
		employee.setMonthlySalary(new BigDecimal(1000));

		when(employeeService.getEmployee(1)).thenReturn(employee);
		mockMvc.perform(get("/employees/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.firstName").value("Grace"))
				.andExpect(jsonPath("$.lastName").value("Hopper")).andExpect(jsonPath("$.age").value(20))
				.andExpect(jsonPath("$.email").value("ghopper@inthenavy.us"))
				.andExpect(jsonPath("$.monthlySalary").value(new BigDecimal(1000)));
	}

	@Test
	@DisplayName("Controller Test - Create employee")
	public void testAddEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setId(1);
		employee.setFirstName("Grace");
		employee.setLastName("Hopper");
		employee.setAge(20);
		employee.setEmail("ghopper@inthenavy.us");
		employee.setMonthlySalary(new BigDecimal(1000));

		when(employeeService.addEmployee(any(Employee.class))).thenReturn(employee);
		mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(
				"{\"id\":1,\"firstName\":\"Grace\",\"lastName\":\"Hopper\",\"age\":20,\"email\":\"ghopper@inthenavy.us\",\"monthlySalary\":1000}"))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Controller Test - Update employee with exception")
	public void testUpdateEmployeeWithException() throws Exception {
		mockMvc.perform(put("/employees/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"{\"id\":2,\"firstName\":\"Grace\",\"lastName\":\"Hopper\",\"age\":20,\"email\":\"ghopper@inthenavy.us\",\"monthlySalary\":1000}"))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Controller Test - Update employee")
	public void testUpdateEmployee() throws Exception {
		Employee employee = new Employee();
		employee.setId(1);
		employee.setFirstName("Grace");
		employee.setLastName("Hopper");
		employee.setAge(20);
		employee.setEmail("ghopper@inthenavy.us");
		employee.setMonthlySalary(new BigDecimal(1000));

		doAnswer((Answer<Employee>) invocation -> employee).when(employeeService).updateEmployee(employee);

		mockMvc.perform(put("/employees/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"{\"id\":1,\"firstName\":\"Grace\",\"lastName\":\"Hopper\",\"age\":20,\"email\":\"ghopper@inthenavy.us\",\"monthlySalary\":1000}"))
				.andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Controller Test - Delete employee")
	public void testDeleteEmployee() throws Exception {
		doAnswer((Answer<Employee>) invocation -> null).when(employeeService).deleteEmployeeById(1);
		mockMvc.perform(delete("/employees/1"))
				.andDo(print())
				.andExpect(status().isNoContent());
	}
}

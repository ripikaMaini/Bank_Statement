package com.test.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.Employee;
import com.test.service.EmployeeService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/employees")
public class TestController {

	private static Logger logger = LoggerFactory.getLogger(TestController.class);

	private final EmployeeService employeeService;

	@Autowired
	public TestController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping
	public List<Employee> generate(@RequestBody Employee account) {
		try {
			return employeeService.generateStatement().apply(account);
		} catch (Exception e) {
			logger.error("Exception in generate() {}", e);
			return null;
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET, consumes = "application/json")
	public Boolean login(@RequestParam("userName") String userName, @RequestParam("password") String password) {
		try {
			Boolean isAuth = employeeService.login().apply(userName, password);
			return isAuth;
		} catch (Exception e) {
			logger.error("Exception in login() {}", e);
			return null;
		}
	}
}
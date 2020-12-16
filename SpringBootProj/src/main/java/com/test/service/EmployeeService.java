package com.test.service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.test.model.Employee;

public interface EmployeeService {

	public Function<Employee, List<Employee>> generateStatement();
	
	public BiFunction<String, String,Boolean> login();

}

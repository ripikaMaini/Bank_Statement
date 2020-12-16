package com.test.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.test.model.Employee;
import com.test.repository.StatementRepository;
import com.test.specification.StatementSpecification;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	private final StatementRepository statementRepository;
	private static Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Value("${adminUserName}")
	private String adminUserName;
	@Value("${adminPass}")
	private String adminPass;
	@Value("${testUserName}")
	private String testUserName;
	@Value("${testPass}")
	private String testPass;

	@Autowired
	public EmployeeServiceImpl(StatementRepository statementRepository) {
		this.statementRepository = statementRepository;
	}

	@Override
	public Function<Employee, List<Employee>> generateStatement() {
		return emplDto -> statementRepository.findAll(StatementSpecification.getStatement(emplDto)).stream()
				.map(st -> Employee.builder().accountId(st.getAccountId().getAccountId().toString())
						.accountNumber(hashAccountNumber().apply(st.getAccountId().getAccountNumber()))
						.fromAmount(st.getAmount()).fromDate(st.getDateField())
						.accountType(st.getAccountId().getAccountType()).build())
				.collect(Collectors.toList());
	}

	private UnaryOperator<String> hashAccountNumber() {
		return accountNumber -> {
			String generatedAccountNumber = null;
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(accountNumber.getBytes());

				byte[] bytes = md.digest();

				// Convert it to hexadecimal format
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < bytes.length; i++) {
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}
				// Get complete hashed password in hex format
				generatedAccountNumber = sb.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return generatedAccountNumber;

		};
	}

	@Override
	public BiFunction<String, String, Boolean> login() {
		return (userName, password) -> {
			if (StringUtils.equalsIgnoreCase(userName, adminUserName)
					&& StringUtils.equalsIgnoreCase(password, adminPass)) {
				return Boolean.TRUE;
			} else if (StringUtils.equalsIgnoreCase(userName, testUserName)
					&& StringUtils.equalsIgnoreCase(password, testPass)) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		};
	}
}

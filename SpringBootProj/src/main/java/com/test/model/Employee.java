package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	@Getter
	private String accountId;
	
	@Getter
	private String fromAmount;
	
	@Getter
	private String toAmount;
	
	@Getter
	@Setter
	private String fromDate;
	
	@Getter
	@Setter
	private String toDate;
	
	@Getter
	private String accountNumber;
	
	@Getter
	private String accountType;

	

}

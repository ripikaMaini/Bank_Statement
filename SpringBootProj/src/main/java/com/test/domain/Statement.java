package com.test.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STATEMENT")

public class Statement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STATEMENT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATEMENT_SEQ")
	@SequenceGenerator(name = "STATEMENT_SEQ", sequenceName = "STATEMENT_SEQ", allocationSize = 1)
	private Long statementId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID")
	private Account accountId;

	@Column(name = "DATEFIELD")
	private String dateField;

	@Column(name = "AMOUNT")
	private String amount;

}
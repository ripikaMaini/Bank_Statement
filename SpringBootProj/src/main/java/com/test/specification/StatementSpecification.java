package com.test.specification;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.test.domain.Account;
import com.test.domain.Statement;
import com.test.model.Employee;

public class StatementSpecification {

	private static final String SIMPLE_DATE_FORMAT = "yyyy/MM/dd";
	private static final String DATE_FIELD = "dateField";
	private static final String ACCOUNT_ID = "accountId";
	private static final String AMOUNT = "amount";

	public static Specification<Statement> getStatement(Employee criteria) {
		return new Specification<Statement>() {

			public Predicate toPredicate(Root<Statement> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				final List<Order> orderList = new ArrayList<>();
				final List<Predicate> predicates = new ArrayList<>();
				if (validateUserInput().test(criteria)) {
					Date date = Calendar.getInstance().getTime();
					DateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
					String toDate = dateFormat.format(date);
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.MONTH, -3);
					String fromDate = dateFormat.format(calendar.getTime());

					criteria.setFromDate(fromDate);
					criteria.setToDate(toDate);

					Expression<String> exp = root.get(DATE_FIELD);
					predicates.add(cb.between(exp, criteria.getFromDate(), criteria.getToDate()));

				} else {
					if (StringUtils.isNotEmpty(criteria.getAccountId())) {
						Join<Statement, Account> queueJoin = root.join(ACCOUNT_ID);
						Expression<String> exp = queueJoin.get(ACCOUNT_ID);
						predicates.add(exp.in(criteria.getAccountId()));
					}
					if (StringUtils.isNotEmpty(criteria.getFromAmount())
							&& StringUtils.isNotEmpty(criteria.getToAmount())) {
						Expression<String> exp = root.get(AMOUNT);
						predicates.add(cb.between(exp, criteria.getFromAmount(), criteria.getToAmount()));
					}

					if (StringUtils.isNotEmpty(criteria.getFromDate())
							&& StringUtils.isNotEmpty(criteria.getToDate())) {
						Expression<String> exp = root.get(DATE_FIELD);
						predicates.add(cb.between(exp, convertDate().apply(criteria.getFromDate()),
								convertDate().apply(criteria.getToDate())));
					}
				}
				orderList.add(cb.desc(root.get(DATE_FIELD)));
				query.orderBy(orderList);

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	private static java.util.function.Predicate<Employee> validateUserInput() {
		return criteria -> StringUtils.isNotEmpty(criteria.getAccountId())
				&& StringUtils.isNotEmpty(criteria.getFromAmount()) && StringUtils.isNotEmpty(criteria.getToAmount())
				&& StringUtils.isNotEmpty(criteria.getFromDate()) && StringUtils.isNotEmpty(criteria.getToDate());
	}

	private static UnaryOperator<String> convertDate() {
		return dates -> new SimpleDateFormat(SIMPLE_DATE_FORMAT).format(parseUTCtoGMT().apply(dates));
	}

	public static Function<String, Date> parseUTCtoGMT() {
		return date -> {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		};
	}

}

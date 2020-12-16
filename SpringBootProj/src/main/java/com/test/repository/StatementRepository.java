package com.test.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.test.domain.Statement;

public interface StatementRepository extends CrudRepository<Statement, Long>,JpaSpecificationExecutor<Statement> {

}

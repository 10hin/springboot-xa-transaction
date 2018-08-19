package com.example.distributedtransaction2;

import com.example.distributedtransaction2.mysql.Tbl1Repository;
import com.example.distributedtransaction2.postgresql.Tbl2Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class Before implements CommandLineRunner, Ordered {

    @Autowired
    private Tbl1Repository tbl1Repository;

    @Autowired
    private Tbl2Repository tbl2Repository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info(",---");
        log.info("| BEFORE:");
        log.info("| allTbl1 = {}", this.tbl1Repository.findAll());
        log.info("| allTbl2 = {}", this.tbl2Repository.findAll());
        log.info("`---");

    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}

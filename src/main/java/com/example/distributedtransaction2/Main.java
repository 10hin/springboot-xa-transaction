package com.example.distributedtransaction2;

import com.example.distributedtransaction2.mysql.Tbl1Entity;
import com.example.distributedtransaction2.mysql.Tbl1Repository;
import com.example.distributedtransaction2.postgresql.Tbl2Entity;
import com.example.distributedtransaction2.postgresql.Tbl2Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Slf4j
public class Main implements CommandLineRunner {

    @Autowired
    private Tbl1Repository tbl1Repository;

    @Autowired
    private Tbl2Repository tbl2Repository;

    @Override
    @Transactional
    public void run(String... args) {

        log.info(",---");
        log.info("| allTbl1 = {}", this.tbl1Repository.findAll());
        log.info("| allTbl2 = {}", this.tbl2Repository.findAll());
        log.info("`---");

        Tbl1Entity tbl1_1 = new Tbl1Entity();
        tbl1_1.setContents("tbl1_1");
        tbl1_1.setUKey(1);
        tbl1_1 = this.tbl1Repository.save(tbl1_1);

        log.info(",---");
        log.info("| allTbl1 = {}", this.tbl1Repository.findAll());
        log.info("| allTbl2 = {}", this.tbl2Repository.findAll());
        log.info("`---");

        Tbl2Entity tbl2_1 = new Tbl2Entity();
        tbl2_1.setContents("tbl2_1");
        tbl2_1.setUKey(1);
        tbl2_1 = this.tbl2Repository.save(tbl2_1);

        log.info(",---");
        log.info("| allTbl1 = {}", this.tbl1Repository.findAll());
        log.info("| allTbl2 = {}", this.tbl2Repository.findAll());
        log.info("`---");

        Tbl1Entity tbl1_2 = new Tbl1Entity();
        tbl1_2.setContents("tbl1_2");
        tbl1_2.setUKey(2);
        tbl1_2 = this.tbl1Repository.save(tbl1_2);

        log.info(",---");
        log.info("| allTbl1 = {}", this.tbl1Repository.findAll());
        log.info("| allTbl2 = {}", this.tbl2Repository.findAll());
        log.info("`---");

        Tbl2Entity tbl2_2 = new Tbl2Entity();
        tbl2_2.setContents("tbl2_2");
        tbl2_2.setUKey(2);
        tbl2_2 = this.tbl2Repository.save(tbl2_2);

        log.info(",---");
        log.info("| allTbl1 = {}", this.tbl1Repository.findAll());
        log.info("| allTbl2 = {}", this.tbl2Repository.findAll());
        log.info("`---");

    }

}

package com.example.distributedtransaction2.postgresql;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tbl2", uniqueConstraints = {
        @UniqueConstraint(name = "tbl2_u_key_ukc", columnNames = {"uKey"})
})
@Data
public class Tbl2Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl2_id_generator")
    @SequenceGenerator(name = "tbl2_id_generator", sequenceName = "tbl2_id_seq")
    private Long id;

    private String contents;

    private Integer uKey;

}

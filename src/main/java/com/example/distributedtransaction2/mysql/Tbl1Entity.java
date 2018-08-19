package com.example.distributedtransaction2.mysql;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tbl1", uniqueConstraints = {
        @UniqueConstraint(name = "tbl1_u_key_ukc", columnNames = {"uKey"})
})
@Data
public class Tbl1Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    private Integer uKey;

}

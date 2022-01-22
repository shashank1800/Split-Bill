package com.shashankbhat.splitbill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bill_share")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillShareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    Integer billId;
    Integer userId;
    Float spent;
    Float share;
    Long dateCreated;
}
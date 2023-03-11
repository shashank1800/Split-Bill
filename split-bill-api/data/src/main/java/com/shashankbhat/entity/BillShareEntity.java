package com.shashankbhat.entity;

import com.common.util.Valid;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bill_share")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BillShareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Setter
    Integer billId;
    Integer userId;
    Float spent;
    Float share;
    Long dateCreated;
    Integer uniqueId;

    public static Valid<BillShareEntity> create(Integer id, Integer billId, Integer userId, Float spent, Float share,
                                                Long dateCreated, Integer uniqueId) {
        if (Objects.isNull(uniqueId))
            return Valid.fail("Unique Id cannot be null");

        if (Objects.isNull(userId))
            return Valid.fail("User Id cannot be null");

        return Valid.success(new BillShareEntity(id, billId, userId, spent, share, dateCreated, uniqueId));
    }
}
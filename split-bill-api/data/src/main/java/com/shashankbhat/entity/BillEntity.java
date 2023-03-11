package com.shashankbhat.entity;

import com.common.util.Valid;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bill")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    Integer groupId;
    String name;
    Float totalAmount;
    Long dateCreated;
    Integer uniqueId;

    public static Valid<BillEntity> create(Integer id, Integer groupId, String name, Float totalAmount,
                                           Long dateCreated, Integer uniqueId) {
        if (Objects.isNull(uniqueId))
            return Valid.fail("Unique Id cannot be null");

        if (Objects.isNull(groupId))
            return Valid.fail("Group Id cannot be null");

        if (Objects.isNull(totalAmount))
            return Valid.fail("Total amount cannot be null");

        if (totalAmount < 0)
            return Valid.fail("Total amount should be more than 0");

        if (Objects.isNull(name) || name.isEmpty())
            return Valid.fail("Bil name cannot be empty");

        return Valid.success(new BillEntity(id, groupId, name, totalAmount, dateCreated, uniqueId));
    }
}
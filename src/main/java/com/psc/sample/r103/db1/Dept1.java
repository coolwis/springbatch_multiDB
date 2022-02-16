package com.psc.sample.r103.db1;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dept1")
public class Dept1 {

    @Id
    Integer deptNo;
    String dName;
    String loc;

}

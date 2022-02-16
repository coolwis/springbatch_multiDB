package com.psc.sample.r103.db2;

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
@Table(name="dept2")
public class Dept2 {

    @Id
    Integer deptNo;
    String dName;
    String loc;

}

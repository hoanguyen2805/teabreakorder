package com.nta.teabreakorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nta.teabreakorder.config.AuditingModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prices")
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="text")
    @JsonProperty(value="text")
    private String text;

    @Column(name="value")
    @JsonProperty(value="value")
    private long value;

    @Column(name="unit")
    @JsonProperty(value="unit")
    private String unit;

}

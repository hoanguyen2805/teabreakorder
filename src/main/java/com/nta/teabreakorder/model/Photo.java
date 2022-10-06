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
@Table(name = "photos")
public class Photo  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="value")
    @JsonProperty(value="value")
    private String value;

    @Column(name="height")
    @JsonProperty(value="height")
    private int height;

    @Column(name="width")
    @JsonProperty(value="width")
    private int width;

}

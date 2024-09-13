package com.chadfield.multidata.movie;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("movies")
public class Movie {

    @Id
    private int id;
    private String name;
    private String info;
    private String genre;
    private String length;

}

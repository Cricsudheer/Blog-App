package com.springboot.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=  GenerationType.IDENTITY)
    private long id ;
    private  String name ;
    @Column(nullable = false , unique = true)
    private String username ;
    @Column(nullable = false , unique = true)
    private  String email ;
    @Column(nullable = false)
    private  String password ;

    @ManyToMany(fetch = FetchType.EAGER ,cascade =  CascadeType.ALL )
    @JoinTable(name = "user_roles",
            joinColumns =  @JoinColumn(name="users_id", referencedColumnName = "id"), // user waala
            inverseJoinColumns = @JoinColumn(name = "role_id" , referencedColumnName = "id" )
    )
    private Set<Role> roles;
    /*
    Creating a set of roles
    - Many to many annotation is a jpa annotation used to create link
    - Fetchtype eager ensures whenever we want to load users its roles are automatically loaded
    - cascade type - whenever we do something with parents
    - @join table is used to join tables to create the joined table
    - joinColumns is to created the columns the name is the name in the new table and referced column is the foreign key
    - inversejoin columns says not from this table
     */

}

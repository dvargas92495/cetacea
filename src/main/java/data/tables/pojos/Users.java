/*
 * This file is generated by jOOQ.
*/
package main.java.data.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Users implements Serializable {

    private static final long serialVersionUID = -1537095646;

    private Integer id;
    private String  firstName;
    private String  lastName;
    private String  email;
    private String  oauthId;

    public Users() {}

    public Users(Users value) {
        this.id = value.id;
        this.firstName = value.firstName;
        this.lastName = value.lastName;
        this.email = value.email;
        this.oauthId = value.oauthId;
    }

    public Users(
        Integer id,
        String  firstName,
        String  lastName,
        String  email,
        String  oauthId
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.oauthId = oauthId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOauthId() {
        return this.oauthId;
    }

    public void setOauthId(String oauthId) {
        this.oauthId = oauthId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Users (");

        sb.append(id);
        sb.append(", ").append(firstName);
        sb.append(", ").append(lastName);
        sb.append(", ").append(email);
        sb.append(", ").append(oauthId);

        sb.append(")");
        return sb.toString();
    }
}

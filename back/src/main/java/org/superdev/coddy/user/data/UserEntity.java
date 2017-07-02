package org.superdev.coddy.user.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

@Document(indexName = "account", type = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String login;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private byte[] password;

    @Field(type = FieldType.String, index = FieldIndex.no)
    private String salting;

    @Field(type = FieldType.String)
    private String[] permissions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getSalting() {
        return salting;
    }

    public void setSalting(String salting) {
        this.salting = salting;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}

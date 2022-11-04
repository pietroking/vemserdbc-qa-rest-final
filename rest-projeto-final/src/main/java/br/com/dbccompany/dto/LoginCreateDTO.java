package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class LoginCreateDTO {
    String login;
    String senha;

    public LoginCreateDTO(){};
    public LoginCreateDTO(String login, String senha){
        this.login = login;
        this.senha = senha;
    }
}

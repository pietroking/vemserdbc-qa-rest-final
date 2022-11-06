package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class RelatorioDTO {

    private Integer idPessoa;
    private String nomePessoa;
    private String email;
    private String nomePet;
    private String numeroContato;
    private String cep;
    private String cidade;
    private String estado;
    private String pais;
}
package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class ContatoDTO {

    private Integer idPessoa;
    private String tipoContato;
    private String telefone;
    private String descricao;
    private Integer idContato;
}

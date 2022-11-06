package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class EnderecoDTO extends PessoaDTO{
    private Integer idPessoa;
    private String tipo;
    private String logradouro;
    private String numero;
    private String complemento;
    private String cep;
    private String cidade;
    private String estado;
    private String pais;
    private Integer idEndereco;
}
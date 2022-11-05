package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class PessoaComEnderecoDTO extends EnderecoDTO {
    private String nome;
    private String dataNascimento;
    private String cpf;
    private String email;
    private String idPessoa;
    private EnderecoDTO[] enderecos;
}

package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class PessoaDTO extends ResponseDTO{
    private String nome;
    private String dataNascimento;
    private String cpf;
    private String email;
    private Integer idPessoa;
    private ContatoDTO[] contatos;
    private EnderecoDTO[] enderecos;
    public PessoaDTO(){

    }
}

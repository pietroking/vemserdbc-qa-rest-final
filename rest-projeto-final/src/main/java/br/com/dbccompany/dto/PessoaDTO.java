package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class PessoaDTO extends ResponseDTO{
    public PessoaDTO() {
    }

    public PessoaDTO(String nome, String dataNascimento, String cpf, String email) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.email = email;
    }

    private String nome;
    private String dataNascimento;
    private String cpf;
    private String email;
    private String idPessoa;
//    private ContatoDTO[] contatos;
//    private EnderecoDTO[] enderecos;
}

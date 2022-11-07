package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class PageDTOPessoaDTO{
    private Integer totalElements;
    private Integer totalPages;
    private Integer page;
    private Integer size;
    private PessoaDTO[] content;
}

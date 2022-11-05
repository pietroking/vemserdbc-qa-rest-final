package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties
@Data
public class EnderecoListaDTO {
    Integer totalElements;
    Integer totalPages;
    Integer page;
    Integer size;
    EnderecoDTO[] content;
}

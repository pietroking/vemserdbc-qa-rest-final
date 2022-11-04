package br.com.dbccompany.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties
public class ResponseDTO{

    private String timestamp;
    private String status;
    private String message;
    private List<String> errors;
}

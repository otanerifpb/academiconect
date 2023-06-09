package br.edu.ifpb.pweb2.academiConect.ui;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// RECNFUNC 09 - Paginação das Tabelas
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class NavPage {

    private int currentPage;
    private long totalItems;
    private int totalPages;
    private int pageSize;
    
}

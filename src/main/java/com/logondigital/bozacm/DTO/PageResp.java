package com.logondigital.bozacm.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResp<T>  {
/**
 * DTO générique pour les réponses paginées.
 *
 * @param <T> Type de l'élément contenu dans la page.
 */
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
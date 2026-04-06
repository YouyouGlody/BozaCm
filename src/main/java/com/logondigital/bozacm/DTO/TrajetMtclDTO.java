package com.logondigital.bozacm.DTO;

import java.util.List;

public class TrajetMtclDTO {

    private Integer idTrajet;
    private List<String> mtclPositifs;
    private List<String> mtclNegatifs;
    private Double moyenneNotes;

    public Integer getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(Integer idTrajet) {
        this.idTrajet = idTrajet;
    }

    public List<String> getMtclPositifs() {
        return mtclPositifs;
    }

    public void setMtclPositifs(List<String> mtclPositifs) {
        this.mtclPositifs = mtclPositifs;
    }

    public List<String> getMtclNegatifs() {
        return mtclNegatifs;
    }

    public void setMtclNegatifs(List<String> mtclNegatifs) {
        this.mtclNegatifs = mtclNegatifs;
    }

    public Double getMoyenneNotes() {
        return moyenneNotes;
    }

    public void setMoyenneNotes(Double moyenneNotes) {
        this.moyenneNotes = moyenneNotes;
    }
}
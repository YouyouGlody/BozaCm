package com.logondigital.bozacm.enums;

/**
 * Énumération représentant les différents statuts possibles d'une réservation.

 * Un Enum est un type spécial qui permet de définir un ensemble FIXE de constantes.
 * C'est plus sûr qu'un String, car on évite les fautes de frappe.
 */
public enum StatutReservation {

    /**
     * La réservation vient d'être créée et attend la validation de l'agence.
     * C'est le statut par défaut quand un client fait une réservation.
     */
    EN_ATTENTE,

    /**
     * La réservation a été validée par l'agence.
     * Le client peut maintenant procéder au paiement.
     */
    CONFIRMEE,

    /**
     * La réservation a été annulée (par le client ou l'agence).
     * Le billet n'est plus valide.
     */
    ANNULEE,

    /**
     * Le voyage a été effectué.
     * Cette réservation fait partie de l'historique passé du client.
     */
    TERMINEE
}

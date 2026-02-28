package com.logondigital.bozacm.config;

/**
 * Constantes centralisées pour tous les noms de caches.
 * Utiliser ces constantes dans les annotations @Cacheable, @CacheEvict, @CachePut
 * pour éviter les erreurs de frappe et faciliter la maintenance.
 */
public final class CacheNames {

    private CacheNames() {}  // Classe utilitaire, non instanciable

    /** Classement de toutes les agences par CA — TTL 10 min */
    public static final String CLASSEMENT_AGENCES = "classement-agences";

    /** Liste des offres les plus réservées — TTL 5 min */
    public static final String OFFRES_POPULAIRES  = "offres-populaires";

    /** Liste/détail des offres — TTL 2 min */
    public static final String OFFRES             = "offres";

    /** Liste/détail des agences — TTL 2 min */
    public static final String AGENCES            = "agences";

    /** Liste/détail des trajets — TTL 2 min */
    public static final String TRAJETS            = "trajets";
}
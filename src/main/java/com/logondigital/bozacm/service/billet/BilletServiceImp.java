package com.logondigital.bozacm.service.billet;

import com.logondigital.bozacm.entities.Billet;
import com.logondigital.bozacm.enums.StatutBillet;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.BilletRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service BilletService.

 * Cette classe contient la logique métier liée à la gestion des billets :
 *     CRUD complet (création, lecture, suppression...)
 *     Recherches par numéro, client, statut ou réservation
 *     Opérations métier comme marquer un billet utilisé ou expirer les billets périmés.

 * Le service agit comme un intermédiaire entre la couche Controller et la couche Repository.

 * Chaque méthode encapsule une logique métier et délègue les opérations de persistance
 * au BilletRepo.
 */
@Service
public class BilletServiceImp implements BilletService {

    /** Repository de gestion des entités Billet */
    private final BilletRepo billetRepo;


    // ===========================================================
    // ==========       CONSTRUCTEUR ET INJECTION        =========
    // ===========================================================

    /**
     * Constructeur avec injection de dépendance.

     * Spring injecte automatiquement une instance de BilletRepo
     * grâce à son conteneur d’inversion de contrôle.

     * @param billetRepo le repository de gestion des billets
     */
    // Injection de dépendance via le constructeur
    public BilletServiceImp(BilletRepo billetRepo) {
        this.billetRepo = billetRepo;
    }


    // ===========================================================
    // ==========        CRUD DE BASE DE BILLET       =====
    // ===========================================================


    /**
     * Crée et enregistre un nouveau billet dans la base de données.
     * La date de création est gérée automatiquement par l'annotation @PrePersist dans l’entité Billet.

     * @param billet le billet à enregistrer
     */
    @Override
    public void createBillet(Billet billet) {
        // PAS de setCreatedAt() : @PrePersist s'en charge !
        this.billetRepo.save(billet);
    }


    /**
     * Récupère tous les billets enregistrés.
     *
     * @return une liste de tous les billets
     */
    @Override
    public List<Billet> getAllBillets() {
        return billetRepo.findAll();
    }


    /**
     * Recherche un billet spécifique par son identifiant (idBillet).
     *
     * @param idBillet identifiant unique du billet
     * @return le billet correspondant
     * @throws RessourceNotFoundException si le billet n’existe pas
     */
    @Override
    public Billet getBilletById(Integer idBillet) {
        return this.billetRepo.findById(idBillet). orElseThrow(
                () -> new RessourceNotFoundException("Billet non trouvé avec l'ID: " + idBillet)
        );
    }


    /**
     * Supprime un billet à partir de son identifiant.
     *
     * @param idBillet identifiant du billet à supprimer
     */
    @Override
    public void deleteBilletById(Integer idBillet) {
        this.billetRepo.deleteById(idBillet);
    }


    /**
     * Supprime tous les billets enregistrés dans la base de données.
     */
    @Override
    public void deleteAllBillets() {
        billetRepo.deleteAll();
    }



    /**
     * Compte le nombre total de billets enregistrés.
     *
     * @return nombre total de billets
     */
    @Override
    public long countBillets() {
        return billetRepo.count();
    }



    // ===========================================================
    // ==========        MÉTHODES MÉTIER      =====
    // ===========================================================



    /**
     * Recherche un billet par son numéro unique.
     *
     * @param numeroBillet numéro du billet à rechercher
     * @return le billet correspondant
     * @throws RessourceNotFoundException si le billet n’existe pas
     */
    @Override
    public Billet findByNumeroBillet(String numeroBillet) {
        return billetRepo.findByNumeroBillet(numeroBillet)
                .orElseThrow(
                        () -> new RessourceNotFoundException("Billet non trouvé avec le numéro: " + numeroBillet)
                );
    }


    /**
     * Récupère tous les billets appartenant à un client donné,
     * triés par date d’émission (du plus récent au plus ancien).
     *
     * @param clientId identifiant du client
     * @return liste des billets du client
     */
    @Override
    public List<Billet> getBilletsClient(Integer clientId) {
        return billetRepo.findByClientIdClientOrderByDateEmissionDesc(clientId);
    }


    /**
     * Récupère les billets d’un client selon leur statut (VALIDE, EXPIRE, UTILISE...).
     *
     * @param clientId identifiant du client
     * @param statut   statut du billet à filtrer
     * @return liste des billets correspondants
     */
    @Override
    public List<Billet> getBilletsParStatut(Integer clientId, StatutBillet statut) {
        return billetRepo.findByClientIdClientAndStatutBillet(clientId, statut);
    }


    /**
     * Recherche un billet associé à une réservation donnée.
     *
     * @param reservationId identifiant de la réservation
     * @return billet correspondant à la réservation
     * @throws RessourceNotFoundException si aucun billet n’est trouvé pour cette réservation
     */
    @Override
    public Billet getBilletByReservation(Integer reservationId) {
        return billetRepo.findByReservationIdReservation(reservationId)
                .orElseThrow(
                        () -> new RessourceNotFoundException("Billet non trouvé pour la réservation: " + reservationId)
                );
    }


    /**
     * Compte le nombre total de billets appartenant à un client donné.
     *
     * @param clientId identifiant du client
     * @return nombre de billets du client
     */
    @Override
    public long countBilletsByClient(Integer clientId) {
        return billetRepo.countByClientIdClient(clientId);
    }


    /**
     * Marque un billet comme "UTILISÉ" une fois que le client a voyagé.
     *Cette méthode sert à changer le statut d’un billet pour indiquer qu’il a déjà été utilisé (le client a voyagé).

     * Autrement dit, quand un client monte dans le bus/train/avion et que le billet est scanné ou validé,
     * on veut que ce billet passe de “VALIDE” → “UTILISÉ”.

     * @param numeroBillet numéro du billet à marquer comme utilisé
     * @return le billet mis à jour avec le nouveau statut
     */
    @Override
    public Billet marquerBilletUtilise(String numeroBillet) {
        // On récupère le billet par son numéro
        Billet billet = findByNumeroBillet(numeroBillet);

        // On change son statut
        billet.setStatutBillet(StatutBillet.UTILISE);

        // On sauvegarde la modification
        return billetRepo.save(billet);
    }


    /**
     * Parcourt tous les billets dont la date d’expiration est passée
     * et change leur statut en "EXPIRÉ" s’ils étaient encore valides.

     *  Un billet a une date d’expiration (dateExpiration).
     *      Lorsqu’un billet n’est pas utilisé avant cette date, il doit être marqué comme “EXPIRÉ”.

     * Cette méthode peut être exécutée manuellement ou planifiée automatiquement
     * (par exemple, une fois par jour).
     */
    @Override
    public void expirerBilletsPerimes() {
        // Récupère la date/heure actuelle
        LocalDateTime maintenant = LocalDateTime.now();

        // On récupère au repository tous les billets expirés (dateExpiration < maintenant).
        List<Billet> billetsExpires = billetRepo.findByDateExpirationBefore(maintenant);

        // Pour chaque billet expiré, on vérifie son statut
        for (Billet billet : billetsExpires) {
            // Si le billet était encore VALIDE, on le passe à EXPIRE
            if (billet.getStatutBillet() == StatutBillet.VALIDE) {
                // On le marque comme EXPIRE
                billet.setStatutBillet(StatutBillet.EXPIRE);


                //On sauvegarde la modification
                billetRepo.save(billet);
            }
        }
    }
}

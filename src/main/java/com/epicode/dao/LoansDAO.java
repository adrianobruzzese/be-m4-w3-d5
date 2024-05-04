package com.epicode.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import com.epicode.entities.Loan;
import com.epicode.exceptions.MaterialNotFoundException;

import java.util.List;

public class LoansDAO {

    private final EntityManager entityManager;

    public LoansDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Salva un prestito nel database.
     * @param loan Il prestito da salvare.
     */
    public void save(Loan loan) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(loan);
            transaction.commit();
            System.out.println("Prestito registrato per " + loan.getPrintedMaterial().getTitle() + " a " + loan.getUser().getFirstName() + " " + loan.getUser().getLastName());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Errore nel salvataggio del prestito: " + e.getMessage());
        }
    }

    /**
     * Trova un prestito tramite il suo ID.
     * @param id L'ID del prestito.
     * @return Il prestito trovato, se esiste.
     * @throws MaterialNotFoundException Se il prestito non viene trovato.
     */
    public Loan findById(long id) {
        Loan loan = entityManager.find(Loan.class, id);
        if (loan == null) {
            throw new MaterialNotFoundException("Prestito non trovato con ID: " + id);
        }
        return loan;
    }

    /**
     * Elimina un prestito dal database usando il suo ID.
     * @param id L'ID del prestito da eliminare.
     */
    public void deleteById(long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Loan loan = entityManager.find(Loan.class, id);
            if (loan != null) {
                entityManager.remove(loan);
                transaction.commit();
                System.out.println("Prestito eliminato con successo.");
            } else {
                System.out.println("Prestito non trovato per l'eliminazione.");
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Errore nell'eliminazione del prestito: " + e.getMessage());
        }
    }

    /**
     * Trova tutti i prestiti associati a un numero di tessera specifico.
     * @param cardNumber Il numero di tessera.
     * @return Una lista di prestiti.
     */
    public List<Loan> findBorrowedItemByCardNumber(long cardNumber) {
        TypedQuery<Loan> query = entityManager.createNamedQuery("Loan.findBorrowedItemByCardNumber", Loan.class);
        query.setParameter("cardNumber", cardNumber);
        return query.getResultList();
    }

    /**
     * Trova tutti i prestiti scaduti e non ancora restituiti.
     * @return Una lista di prestiti scaduti.
     */
    public List<Loan> findExpiredNotReturned() {
        TypedQuery<Loan> query = entityManager.createNamedQuery("Loan.findExpiredNotReturned", Loan.class);
        return query.getResultList();
    }
}

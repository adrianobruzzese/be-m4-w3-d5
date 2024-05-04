package com.epicode.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import com.epicode.entities.User;
import com.epicode.exceptions.MaterialNotFoundException;

public class UsersDAO {
    private final EntityManager entityManager;

    public UsersDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Salvo un utente nel database. Questo metodo gestisce l'intero processo di
     * persistenza dell'utente, inclusa l'apertura della transazione, il salvataggio dell'utente,
     * e il commit della transazione.
     * @param user L'utente da salvare nel database.
     */
    public void save(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();  // Inizio la transazione
            entityManager.persist(user);  // Persisto l'utente nel database
            transaction.commit();  // Faccio il commit della transazione
            System.out.println("Utente creato: " + user.getFirstName() + " " + user.getLastName());
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();  // Faccio rollback se la transazione è ancora attiva e c'è un errore
            }
            System.out.println("Errore nel salvataggio dell'utente: " + e.getMessage());
        }
    }

    /**
     * Ricerca un utente per ID. Questo metodo ritorna un utente se trovato, altrimenti
     * lancia un'eccezione.
     * @param id L'identificativo dell'utente da cercare.
     * @return L'utente trovato.
     * @throws MaterialNotFoundException Se l'utente non viene trovato.
     */
    public User findById(long id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new MaterialNotFoundException("Utente non trovato con ID: " + id);
        }
        return user;
    }
}

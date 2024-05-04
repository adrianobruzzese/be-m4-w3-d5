package com.epicode.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import com.epicode.entities.PrintedMaterial;
import com.epicode.entities.Literature;
import com.epicode.exceptions.IsbnNotFoundException;

import java.util.List;

public class PrintedMaterialDAO {

    private final EntityManager entityManager;

    public PrintedMaterialDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Salva un item stampato nel database.
     * @param material Item stampato da salvare.
     */
    public void save(PrintedMaterial material) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(material);
            transaction.commit();
            System.out.println("Materiale salvato: " + material.getTitle());
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Salvataggio fallito del materiale stampato: " + e.getMessage());
        }
    }

    /**
     * Recupera un materiale stampato tramite il suo ID.
     * @param id L'identificativo del materiale stampato.
     * @return Il materiale stampato trovato.
     * @throws IsbnNotFoundException se il materiale non viene trovato.
     */
    public PrintedMaterial findById(String id) {
        PrintedMaterial material = entityManager.find(PrintedMaterial.class, id);
        if (material == null) {
            throw new IsbnNotFoundException("Materiale stampato non trovato con ID: " + id);
        }
        return material;
    }

    /**
     * Elimina un materiale stampato tramite il suo ID.
     * @param id L'identificativo del materiale da eliminare.
     */
    public void deleteById(String id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            PrintedMaterial material = entityManager.find(PrintedMaterial.class, id);
            if (material != null) {
                entityManager.remove(material);
                transaction.commit();
                System.out.println("Materiale stampato eliminato: " + id);
            } else {
                System.out.println("Materiale stampato non trovato per l'eliminazione: " + id);
            }
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Eliminazione fallita: " + e.getMessage());
        }
    }

    /**
     * Trova materiali stampati in base all'anno di pubblicazione.
     * @param year L'anno di pubblicazione.
     * @return Una lista di materiali stampati.
     */
    public List<PrintedMaterial> findByPublicationYear(int year) {
        TypedQuery<PrintedMaterial> query = entityManager.createNamedQuery("PrintedMaterial.findByYear", PrintedMaterial.class);
        query.setParameter("publicationYear", year);
        return query.getResultList();
    }

    /**
     * Trova opere letterarie tramite autore.
     * @param author Il nome dell'autore.
     * @return Una lista di opere letterarie.
     */
    public List<Literature> findByAuthor(String author) {
        TypedQuery<Literature> query = entityManager.createNamedQuery("Literature.findByAuthor", Literature.class);
        query.setParameter("author", author.toLowerCase());
        return query.getResultList();
    }

    /**
     * Trova materiali stampati tramite titolo.
     * @param title Il titolo o parte del titolo da cercare.
     * @return Una lista di materiali stampati.
     */
    public List<PrintedMaterial> findByTitle(String title) {
        TypedQuery<PrintedMaterial> query = entityManager.createNamedQuery("PrintedMaterial.findByTitle", PrintedMaterial.class);
        query.setParameter("title", "%" + title.toLowerCase() + "%");
        return query.getResultList();
    }
}

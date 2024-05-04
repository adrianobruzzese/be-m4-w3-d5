package com.epicode;

import com.github.javafaker.Faker;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import com.epicode.dao.PrintedMaterialDAO;
import com.epicode.dao.LoansDAO;
import com.epicode.dao.UsersDAO;
import com.epicode.entities.*;

import java.time.LocalDate;
import java.util.Locale;

public class Application {
    // Creo una factory per la gestione degli EntityManager, utilizzando il persistence unit specificato
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("u4w3d5");

    public static void main(String[] args) {
        // Creo un EntityManager per gestire le operazioni di persistenza
        EntityManager em = emf.createEntityManager();

        // Inizializzo gli oggetti DAO per gestire le entità specifiche
        PrintedMaterialDAO printedMaterialDAO = new PrintedMaterialDAO(em);
        LoansDAO loansDAO = new LoansDAO(em);
        UsersDAO usersDAO = new UsersDAO(em);

        // Utilizzo Faker per generare dati fittizi per il popolamento iniziale del database
        Faker faker = new Faker(Locale.ITALY);

        try {
            // Popolare il database con utenti fittizi
            initializeUsers(usersDAO, faker);

            // Gestione dei prestiti tra utenti e materiali stampati
            manageLoans(usersDAO, printedMaterialDAO, loansDAO);

            // Operazioni dimostrative di ricerca e manipolazione dei dati
            performDemonstrationOperations(printedMaterialDAO, loansDAO);

        } finally {
            // Chiusura dell'EntityManager e dell'EntityManagerFactory per rilasciare le risorse
            em.close();
            emf.close();
        }
    }

    private static void initializeUsers(UsersDAO usersDAO, Faker faker) {
        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            LocalDate dob = LocalDate.of(faker.number().numberBetween(1940, 2000),
                    faker.number().numberBetween(1, 12),
                    faker.number().numberBetween(1, 28));
            User newUser = new User(firstName, lastName, dob);
            usersDAO.save(newUser);
        }
    }

    private static void manageLoans(UsersDAO usersDAO, PrintedMaterialDAO printedMaterialDAO, LoansDAO loansDAO) {
        User user1 = usersDAO.findById(4);
        User user2 = usersDAO.findById(6);

        PrintedMaterial material1 = printedMaterialDAO.findById("0-01-354243-5");
        PrintedMaterial material2 = printedMaterialDAO.findById("0-07-704248-4");

        Loan loan1 = new Loan(user1, material1);
        Loan loan2 = new Loan(user2, material2, LocalDate.now().minusDays(21));

        loansDAO.save(loan1);
        loansDAO.save(loan2);
    }

    private static void performDemonstrationOperations(PrintedMaterialDAO printedMaterialDAO, LoansDAO loansDAO) {
        System.out.println("------ ADDING MATERIAL TO CATALOGUE--------");
        printedMaterialDAO.save(new Literature("Alice in Wonderland", 356, "Lewis Carroll", "Fantasy"));

        System.out.println("------ SEARCH MATERIAL BY ISBN --------");
        System.out.println(printedMaterialDAO.findById("1-9973620-8-2"));

        System.out.println("------ SEARCH MATERIAL BY PUBLICATION YEAR --------");
        printedMaterialDAO.findByPublicationYear(1712).forEach(System.out::println);

        System.out.println("------ SEARCH MATERIAL BY AUTHOR --------");
        printedMaterialDAO.findByAuthor("Vera Negri").forEach(System.out::println);

        System.out.println("------ SEARCH MATERIAL CURRENTLY ON LOAN BY A CARD NUMBER --------");
        loansDAO.findBorrowedItemByCardNumber(4).forEach(System.out::println);

        System.out.println("------ SEARCH ALL LOANS EXPIRED AND NOT YET RETURNED --------");
        loansDAO.findExpiredNotReturned().forEach(System.out::println);
    }
}

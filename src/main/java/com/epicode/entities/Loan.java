package com.epicode.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@NamedQuery(name = "findBorrowedItemByCardNumber", query = "SELECT l FROM Loan l WHERE l.user.cardNumber = :cardNumber AND l.effectiveReturnDate IS NULL")
@NamedQuery(name = "findExpiredNotReturned", query = "SELECT l FROM Loan l WHERE l.loanReturnDate < CURRENT_DATE AND l.effectiveReturnDate IS NULL")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @OneToOne
    private PrintedMaterial printedMaterial;

    private LocalDate loanStartDate;
    private LocalDate loanReturnDate;
    private LocalDate effectiveReturnDate;

    public Loan() {}

    public Loan(User user, PrintedMaterial printedMaterial) {
        this.user = user;
        this.printedMaterial = printedMaterial;
        this.loanStartDate = LocalDate.now();
        this.loanReturnDate = LocalDate.now().plusDays(30);
        this.effectiveReturnDate = null;
    }

    public Loan(User user, PrintedMaterial printedMaterial, LocalDate effectiveReturnDate) {
        this.user = user;
        this.printedMaterial = printedMaterial;
        this.loanStartDate = generateRandomDate();
        this.loanReturnDate = this.loanStartDate.plusDays(30);
        this.effectiveReturnDate = effectiveReturnDate;
    }

    public User getUser() {
        return user;
    }

    public PrintedMaterial getPrintedMaterial() {
        return printedMaterial;
    }

    public void setEffectiveReturnDate(LocalDate effectiveReturnDate) {
        this.effectiveReturnDate = effectiveReturnDate;
    }

    public static LocalDate generateRandomDate() {
        long minDay = LocalDate.of(2000, 1, 1).toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    @Override
    public String toString() {
        return String.format("Prestito: utente=%s, materiale=%s, inizio=%s, scadenza=%s, rientro=%s",
                user, printedMaterial, loanStartDate, loanReturnDate, effectiveReturnDate);
    }
}

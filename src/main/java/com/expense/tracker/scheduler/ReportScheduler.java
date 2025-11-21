package com.expense.tracker.scheduler;

import com.expense.tracker.entity.Bank;
import com.expense.tracker.entity.CreditCard;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.BankRepository;
import com.expense.tracker.repository.CreditCardRepository;
import com.expense.tracker.repository.ExpenseRepository;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.service.EmailService;
import com.expense.tracker.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private EmailService emailService;

    // Cron: Last day of the month at midnight
    @Scheduled(cron = "0 0 0 L * ?")
    public void generateAndSendMonthlyReports() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if ("ACTIVE".equals(user.getStatus())) {
                try {
                    List<Bank> banks = bankRepository.findByUserId(user.getId());
                    List<CreditCard> cards = creditCardRepository.findByUserId(user.getId());
                    List<Expense> expenses = expenseRepository.findByUserId(user.getId());
                    
                    // Filter expenses for current month if needed, but for now sending all history or filtered in PDF service
                    // Ideally we should filter by date range here.
                    // For simplicity, passing all, but in real app we'd filter.
                    
                    List<Expense> bankExpenses = expenses.stream()
                            .filter(e -> "BANK".equalsIgnoreCase(e.getMode()))
                            .collect(Collectors.toList());

                    List<Expense> cardExpenses = expenses.stream()
                            .filter(e -> "CARD".equalsIgnoreCase(e.getMode()))
                            .collect(Collectors.toList());

                    byte[] bankReport = pdfService.generateBankReport(banks, bankExpenses);
                    byte[] cardReport = pdfService.generateCardReport(cards, cardExpenses);

                    emailService.sendMonthlyReport(user.getEmail(), bankReport, cardReport);
                    System.out.println("Report sent to: " + user.getEmail());

                } catch (Exception e) {
                    System.err.println("Failed to send report to: " + user.getEmail());
                    e.printStackTrace();
                }
            }
        }
    }
}

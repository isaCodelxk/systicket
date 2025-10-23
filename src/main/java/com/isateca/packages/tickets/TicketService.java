package com.isateca.packages.tickets;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    @Transactional
    public Ticket createTicket(String title, String description) {
        if (title == null || title.isBlank() || description == null || description.isBlank()) {
            throw new IllegalArgumentException("El título y la descripción no pueden estar vacíos.");
        }
        var ticket = new Ticket(title, description);
        return ticketRepository.save(ticket);
    }
    @Transactional(readOnly = true)
    public Page<Ticket> list(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

}

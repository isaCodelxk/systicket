package com.isateca.packages.tickets.ui;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.isateca.packages.tickets.Ticket;
import com.isateca.packages.tickets.TicketService;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/tickets")
@PageTitle("Tickets")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Ticket List")
public class TicketView extends VerticalLayout {
    private final TicketService ticketService;
    public TicketView(TicketService ticketService){
        this.ticketService = ticketService;
    }

    private VerticalLayout getCards(){
        VerticalLayout layout = new VerticalLayout();
        int numeroDePagina = 0;
        int tamañoDePagina = 10;
        Pageable pageable = PageRequest.of(numeroDePagina, tamañoDePagina);

        Page<Ticket> paginaDeTickets = ticketService.list(pageable);

        paginaDeTickets.getContent().forEach(ticket -> {
            Card card = createCard(ticket.getTitle(), ticket.getDescription());
            layout.add(card);
        });

        return layout;
    }

    private Card createCard(String title, String description){
        Card card = new Card();

        Avatar avatar = new Avatar("Lapland");
        card.setMedia(avatar);
        card.setTitle(new Div(title));

        card.add(description);
        return card;
    }
    
}

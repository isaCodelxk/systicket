package com.isateca.packages.tickets.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/ticket-create")
@PageTitle("Create ticket")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Ticket Create")
public class TicketForm extends VerticalLayout{
    private Button btnSave;
    private TextField txtTitle;
    private TextArea txtDescription;

    public TicketForm(){
        

    }
}

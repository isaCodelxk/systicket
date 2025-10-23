package com.isateca.packages.areas.ui;

import com.isateca.packages.areas.AreaEntity;
import com.isateca.packages.areas.AreaService;
import com.isateca.packages.utils.custom.components.NotificationCustom;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("admin/areas")
public class AreaView extends VerticalLayout{
    AreaService areaService;
    VerticalLayout mainLayout;
    Grid<AreaEntity> gridAreas;
    Dialog dialogCreate;
    Button btnCreateNew;
    public AreaView(AreaService areaService){
        this.areaService = areaService;
        loadUi();
    }

    public void loadUi(){
        btnCreateNew = new Button("Nuevo");
        btnCreateNew.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        btnCreateNew.addClickListener(event -> {
            createForm();
        });

        HorizontalLayout layoutBtn = new HorizontalLayout();
        layoutBtn.setWidthFull();
        layoutBtn.add(btnCreateNew);

        mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.add(layoutBtn, getGrid());

        add(mainLayout);
    }


    private void createForm(){
        FormLayout layout = new FormLayout();

        TextField txtArea = new TextField();
        H3 labelH3 = new H3("Crear Area");

        Button btnCreate = new Button("Crear Area");
        btnCreate.setIcon(VaadinIcon.PLUS.create());
        btnCreate.addClickListener(event -> {
            if (txtArea.isEmpty()){
                NotificationCustom.getTopEndNotificationWarning("El campo no puede estar avcio");
            }
            AreaEntity areaEntity = new AreaEntity();
            areaEntity.setName(txtArea.getValue());
            areaService.save(areaEntity);

            NotificationCustom.getTopEndNotificationWarning("Area creada");
            gridAreas.setItems(areaService.findAll());
        });

        layout.add(txtArea, btnCreate);

        dialogCreate = new Dialog();

        dialogCreate.add(labelH3, layout);
        dialogCreate.open();
    }

    private Grid<AreaEntity> getGrid(){

        gridAreas = new Grid<>(AreaEntity.class, false);
        gridAreas.addColumn(AreaEntity::getName).setHeader("Nombre del area");
        gridAreas.setItems(areaService.findAll());

        return gridAreas;
    }

}

package com.isateca.packages.roles.ui;


import com.isateca.packages.login.ui.LoginView;
import com.isateca.packages.roles.RoleEntity;
import com.isateca.packages.roles.RoleService;
import com.isateca.packages.users.UserEntity;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("admin/roles")
public class RoleView extends VerticalLayout implements BeforeEnterObserver{
    private final RoleService roleService;

    private Grid<RoleEntity> grid;
    private Binder<RoleEntity> binder;
    private Dialog roleDialog;

    public RoleView(RoleService roleService) {
        this.roleService = roleService;
        buildUI();
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UserEntity currentUser = VaadinSession.getCurrent().getAttribute(UserEntity.class);
        if (currentUser == null) {
            event.rerouteTo(LoginView.class);
        }
    }

    private void buildUI() {
        grid = new Grid<>(RoleEntity.class, false);
        binder = new Binder<>(RoleEntity.class);
        roleDialog = new Dialog();

        setSizeFull();
        Button btnNewRole = new Button("Nuevo Rol", VaadinIcon.PLUS.create());
        btnNewRole.addClickListener(e -> openRoleDialog(new RoleEntity()));

        configureGrid();
        configureDialog();

        add(btnNewRole, grid);
    }

    private void configureGrid() {
        grid.addColumn(RoleEntity::getName).setHeader("Nombre").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(role -> {
            Button btnEdit = new Button(VaadinIcon.EDIT.create());
            btnEdit.addClickListener(e -> openRoleDialog(role));
            return btnEdit;
        })).setHeader("Editar");
        
        refreshGrid();
    }
    
    private void refreshGrid() {
        grid.setItems(roleService.findAll());
    }

    private void configureDialog() {
        H3 title = new H3("Crear / Editar Rol");

        FormLayout formLayout = new FormLayout();
        TextField txtRoleName = new TextField("Nombre del Rol");
        formLayout.add(txtRoleName);

        binder.forField(txtRoleName)
              .asRequired("El nombre es obligatorio.")
              .bind(RoleEntity::getName, RoleEntity::setName);

        Button btnSave = new Button("Guardar", VaadinIcon.CHECK.create());
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(e -> saveRole());

        Button btnCancel = new Button("Cancelar", VaadinIcon.CLOSE.create());
        btnCancel.addClickListener(e -> roleDialog.close());
        
        roleDialog.add(title, formLayout, new HorizontalLayout(btnSave, btnCancel));
    }

    private void openRoleDialog(RoleEntity role) {
        binder.setBean(role);
        roleDialog.open();
    }

    private void saveRole() {
        try {
            RoleEntity roleToSave = binder.getBean();
            binder.writeBean(roleToSave);
            
            roleService.save(roleToSave);
            
            Notification.show("Rol guardado correctamente.", 2000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            roleDialog.close();
            refreshGrid();

        } catch (ValidationException e) {
            Notification.show("Por favor, revisa el campo.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (Exception e) {
            Notification.show("Error al guardar el rol: " + e.getMessage(), 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}

package com.isateca.packages.users.ui;

import com.isateca.packages.login.ui.LoginView;
import com.isateca.packages.roles.RoleEntity;
import com.isateca.packages.roles.RoleService;
import com.isateca.packages.users.UserEntity;
import com.isateca.packages.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import org.springframework.data.domain.PageRequest;

@Route("admin/users")
public class UserAdminView extends VerticalLayout {

    private final UserService userService;
    private final RoleService roleService;

    private Grid<UserEntity> grid;
    private Binder<UserEntity> binder;
    private Dialog userDialog;

    private int currentPage = 0;
    private final int pageSize = 10;

    UserEntity uEntity = VaadinSession.getCurrent().getAttribute(UserEntity.class);

    public UserAdminView(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.grid = new Grid<>(UserEntity.class, false);
        this.binder = new Binder<>(UserEntity.class);

        if (uEntity != null){
            configureUI();
        }else {
            UI.getCurrent().navigate(LoginView.class);
        }
    }

    private void configureUI() {
        setSizeFull();
        Button btnNewUser = new Button("Nuevo Usuario", VaadinIcon.PLUS.create());
        btnNewUser.addClickListener(e -> openUserDialog(new UserEntity()));

        configureGrid();
        configureDialog();

        Button btnPrev = new Button("Anterior", e -> {
            if (currentPage > 0) {
                currentPage--;
                refreshGridData();
            }
        });

        Button btnNext = new Button("Siguiente", e -> {
            currentPage++;
            refreshGridData();
        });

        HorizontalLayout paginationControls = new HorizontalLayout(btnPrev, btnNext);

        add(btnNewUser, grid, paginationControls);

        refreshGridData();
    }

    private void refreshGridData() {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);

        org.springframework.data.domain.Page<UserEntity> paginaDeUsuarios = userService.getAllUsers(pageRequest);
        grid.setItems(paginaDeUsuarios.getContent());
    }

    private void configureGrid() {
        grid.addColumn(UserEntity::getUsername).setHeader("Username").setSortable(true);
        grid.addColumn(UserEntity::getFirstName).setHeader("Nombre").setSortable(true);
        grid.addColumn(UserEntity::getLastname).setHeader("Apellido").setSortable(true);
        grid.addColumn(UserEntity::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(user -> user.getRole().getName()).setHeader("Rol").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(user -> {
            Button btnEdit = new Button(VaadinIcon.EDIT.create());
            btnEdit.addClickListener(e -> openUserDialog(user));
            return btnEdit;
        })).setHeader("Acciones");
    }

    private void configureDialog() {
        userDialog = new Dialog();
        H3 title = new H3("Crear / Editar Usuario");

        FormLayout formLayout = new FormLayout();
        TextField txtUsername = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        EmailField emailField = new EmailField("Email");
        TextField txtName = new TextField("Nombre");
        TextField txtLastname = new TextField("Apellido");
        ComboBox<RoleEntity> cbxRoles = new ComboBox<>("Rol");

        cbxRoles.setItems(roleService.findAll());
        cbxRoles.setItemLabelGenerator(RoleEntity::getName);

        binder.forField(txtUsername).asRequired("El username es obligatorio.").bind(UserEntity::getUsername, UserEntity::setUsername);
        binder.forField(passwordField).bind(UserEntity::getPassword, UserEntity::setPassword);
        binder.forField(emailField).asRequired("El email es obligatorio.").bind(UserEntity::getEmail, UserEntity::setEmail);
        binder.forField(txtName).asRequired("El nombre es obligatorio.").bind(UserEntity::getFirstName, UserEntity::setFirstName);
        binder.forField(txtLastname).asRequired("El apellido es obligatorio.").bind(UserEntity::getLastname, UserEntity::setLastname);
        binder.forField(cbxRoles).asRequired("El rol es obligatorio.").bind(UserEntity::getRole, UserEntity::setRole);
        
        formLayout.add(txtUsername, passwordField, emailField, txtName, txtLastname, cbxRoles);
        
        Button btnSave = new Button("Guardar", VaadinIcon.CHECK.create());
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSave.addClickListener(e -> saveUser());

        Button btnCancel = new Button("Cancelar", VaadinIcon.CLOSE.create());
        btnCancel.addClickListener(e -> userDialog.close());
        
        userDialog.add(title, formLayout, new HorizontalLayout(btnSave, btnCancel));
    }

    private void openUserDialog(UserEntity user) {
        binder.setBean(user);
        userDialog.open();
    }

    private void saveUser() {
        try {
            UserEntity userToSave = binder.getBean();
            if (userToSave.getId() == null && userToSave.getPassword().isBlank()) {
                Notification.show("La contraseña es obligatoria para usuarios nuevos.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            binder.writeBean(userToSave); 
            userService.createUser(userToSave);
            
            Notification.show("Usuario guardado correctamente.", 2000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            userDialog.close();
            grid.getDataProvider().refreshAll();

        } catch (ValidationException e) {
            Notification.show("Por favor, corrige los errores en el formulario.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (Exception e) {
             Notification.show("Error al guardar el usuario: " + e.getMessage(), 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}

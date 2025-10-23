package com.isateca.packages.login.ui;

import com.isateca.packages.login.AuthService;
import com.isateca.packages.users.*;
import com.isateca.packages.users.ui.UserAdminView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinSession;


@PageTitle("Login")
@Route(value = "login")
@RouteAlias(value = "")
public class LoginView extends VerticalLayout {

    private final AuthService authService;

    public LoginView(AuthService authService){
        this.authService = authService;

        LoginForm loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.setI18n(createSpanishI18n());

        loginForm.addLoginListener(loginEvent -> {
            authService.authenticate(loginEvent.getUsername(), loginEvent.getPassword())
                .ifPresentOrElse(
                    user -> {
                        VaadinSession.getCurrent().setAttribute(UserEntity.class, user);
                        navigateToProperView(user);
                    },
                    () -> loginForm.setError(true)
                );
        });

        add(loginForm);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-image", "url('https://images4.alphacoders.com/121/1219667.jpg')")
                  .set("background-size", "cover").set("background-repeat", "no-repeat");
    }

    private void navigateToProperView(UserEntity user) {
        if ("ADMIN".equalsIgnoreCase(user.getRole().getName())) {
            UI.getCurrent().navigate(UserAdminView.class);
        } else {

            UI.getCurrent().navigate("main");
        }
    }

    private LoginI18n createSpanishI18n(){
        final LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Isa Cloud");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setPassword("Contraseña");
        i18n.getForm().setSubmit("Entrar");
        i18n.getErrorMessage().setTitle("Usuario o contraseña incorrectos");
        i18n.getErrorMessage().setMessage("Verifica tus credenciales e intenta de nuevo.");
        return i18n;
    }
}
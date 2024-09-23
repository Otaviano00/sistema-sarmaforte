package com.model.classesassociadas;

import com.model.classes.Menu;
import com.model.classes.Perfil;

public class Perfil_Menu {

    private int id;
    private Perfil perfil;
    private Menu menu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Perfil_Menu() {
    }

}

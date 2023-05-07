package ch.ksh.xl2mqb;

import ch.ksh.xl2mqb.gui.XL2mQB;

import javafx.application.Application;

/**
 * The Main class, which has the only use to specifically point out the entry point of the application.
 */
public class Main {

    /**
     * The main method simply launches the gui and passes along the arguments for further evaluation.
     *
     * @param args The application arguments
     */
    public static void main(String[] args) {
        Application.launch(XL2mQB.class, args);
    }
}

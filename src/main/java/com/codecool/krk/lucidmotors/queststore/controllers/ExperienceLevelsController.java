package com.codecool.krk.lucidmotors.queststore.controllers;

import com.codecool.krk.lucidmotors.queststore.dao.ExperienceLevelsDao;
import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import com.codecool.krk.lucidmotors.queststore.models.ExperienceLevels;
import com.codecool.krk.lucidmotors.queststore.models.Mentor;

import java.util.ArrayList;

public class ExperienceLevelsController extends AbstractUserController<Mentor> {

    private ExperienceLevelsDao experienceLevelsDao = new ExperienceLevelsDao();

    public ExperienceLevelsController() throws DaoException {

    }

    /**
     * Switches between menu options.
     *
     * @param choice
     */
    protected void handleUserRequest(String choice) throws DaoException {


        switch (choice) {

            case "1":
                createNewLevel();
                break;

            case "2":
                updateLevel();
                break;

            case "3":
                showLevels();
                break;

            case "0":
                break;

            default:
                userInterface.println("No such option.");
                break;
        }
    }

    protected void showMenu() {
        userInterface.printExperienceLevelsMenu();
    }

    /**
     * Gathers data about new level and insert it into database.
     */
    private void createNewLevel() {

        String[] questions = {"level: ", "needed coins: "};
        String[] types = {"integer", "integer"};
        ArrayList<String> answers = this.userInterface.inputs.getValidatedInputs(questions, types);
        // #TODO implement database connection
        this.userInterface.println("Level added.");
        this.userInterface.pause();
    }

    /**
     * Gathers new data about level and update it in database.
     */
    private void updateLevel() {

        String[] questions = {"level: ", "needed coins: "};
        String[] types = {"integer", "integer"};
        ArrayList<String> answers = this.userInterface.inputs.getValidatedInputs(questions, types);
        // #TODO implement database connection
        this.userInterface.println("Level Updated.");

    private void showLevels() throws DaoException {

        this.userInterface.println(this.experienceLevelsDao.getExperienceLevels().toString());
        this.userInterface.pause();
    }
}

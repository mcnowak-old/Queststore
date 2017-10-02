package com.codecool.krk.lucidmotors.queststore.controllers;

import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import com.codecool.krk.lucidmotors.queststore.exceptions.LoginInUseException;
import com.codecool.krk.lucidmotors.queststore.enums.MentorMenuOptions;
import com.codecool.krk.lucidmotors.queststore.models.*;
import com.codecool.krk.lucidmotors.queststore.views.UserInterface;

import java.util.ArrayList;

class MentorController extends AbstractController<Mentor> {

      /**
     * Switches between methods, acording to userChoice param.
     *
     * @param userChoice
     * @throws DaoException
     */
    protected void handleUserRequest(String userChoice) throws DaoException {

        MentorMenuOptions chosenOption;
        try {
            chosenOption = MentorMenuOptions.values()[userChoice];
        } catch (IndexOutOfBoundsException e) {
            chosenOption = null;
        }

        if (chosenOption != null) {
            switch (chosenOption) {
                case ADD_STUDENT:
                    addStudent();
                    break;

                case ADD_QUEST:
                    addQuest();
                    break;

                case ADD_QUEST_CATEGORY:
                    addQuestCategory();
                    break;

                case UPDATE_QUEST:
                    updateQuest();
                    break;

                case MARK_BOUGHT_ARTIFACT_AS_USED:
                    markBoughtArtifactsAsUsed();
                    break;

                case START_MENTOR_STORE_CONTROLLER:
                    runMentorStoreController();
                    // # TODO write break here

                case EXIT:
                    break;

                default:
                    handleNoSuchCommand();
            }
        }
    }


    protected void showMenu() {
        userInterface.printMentorMenu();
    }

    /**
     * Gather data about new student, create object and insert data into database.
     *
     * @throws DaoException
     */
    private void addStudent() throws DaoException {

        String[] questions = {"Name: ", "Login: ", "Password: ", "Email: "};
        String[] expectedTypes = {"String", "String", "String", "String"};

        ArrayList<String> basicUserData = userInterface.inputs.getValidatedInputs(questions, expectedTypes);
        String name = basicUserData.get(0);
        String login = basicUserData.get(1);
        String password = basicUserData.get(2);
        String email = basicUserData.get(3);
        SchoolClass choosenClass = chooseProperClass();

        try {
            this.school.isLoginAvailable(login);
            Student student = new Student(name, login, password, email, choosenClass);
            student.save();

        } catch (LoginInUseException e) {
            userInterface.println(e.getMessage());
        }

        this.userInterface.pause();
    }

    /**
     * Shows all classes and returns SchoolClass object chosen by user.
     *
     * @return
     * @throws DaoException
     */
    private SchoolClass chooseProperClass() throws DaoException {

        ArrayList<SchoolClass> allClasses = this.school.getAllClasses();
        int userChoice;

        do {
            showAvailableClasses(allClasses);
            userChoice = getUserChoice() - 1;

        } while (userChoice > (allClasses.size() - 1) || userChoice < 0);
        return allClasses.get(userChoice);
    }

    /**
     * Gets choice of class from user
     *
     * @return class number (generated automatically based on ArrayList size)
     */
    private Integer getUserChoice() {

        String[] questions = {"Please choose class: "};
        String[] expectedTypes = {"integer"};
        ArrayList<String> userInput = userInterface.inputs.getValidatedInputs(questions, expectedTypes);

        return Integer.parseInt(userInput.get(0));
    }

    /**
     * Prints all classes and index generated on base of ArrayList size
     *
     * @param allClasses
     */
    private void showAvailableClasses(ArrayList<SchoolClass> allClasses) {
        userInterface.newLine();

        for (int i = 0; i < allClasses.size(); i++) {
            String index = Integer.toString(i + 1);
            System.out.println(index + ". " + allClasses.get(i));
        }

        userInterface.newLine();
    }

    /**
     * Gather data about new Quest and insert it into database
     */
    private void addQuest() {

        String[] questions = {"Name: ", "Quest category: ", "Description: ", "Value: "};
        String[] types = {"string", "string", "string", "integer"};
        this.userInterface.inputs.getValidatedInputs(questions, types);
        // # TODO implement database connection
        this.userInterface.pause();
    }

    /**
     * Gather data about new Quest Category and inset it into database
     */
    private void addQuestCategory() {

        String[] questions = {"Name: "};
        String[] types = {"string"};
        this.userInterface.inputs.getValidatedInputs(questions, types);
        // # TODO implement database connection
        this.userInterface.pause();
    }

    /**
     * Get choice which quest to update.
     * Gather new data about quest.
     * Update database.
     */
    private void updateQuest() {

        Integer id = this.getQuestId();
        String[] questions = {"new name: ", "new quest category: ", "new description: ", "new value: "};
        String[] types = {"string", "string", "string", "integer"};
        this.userInterface.inputs.getValidatedInputs(questions, types);
        // # TODO implement database connection
        this.userInterface.pause();
    }

    /**
     * Gets integer from user
     * @return
     */
    private Integer getQuestId() {

        String[] question = {"Provide quest id: "};
        String[] type = {"integer"};

        return Integer.parseInt(userInterface.inputs.getValidatedInputs(question, type).get(0));
    }

    private void markBoughtArtifactsAsUsed() {

        String mockArtifactsList = "id - owner - name - status\n" +
                "1 - Maciej Nowak - Sanctuary - used\n" +
                "2 - Paweł Polakiewicz - Teleport - not used";

        this.userInterface.println(mockArtifactsList);

        String[] question = {"id: "};
        String[] type = {"integer"};

        if (this.userInterface.inputs.getValidatedInputs(question, type).get(0).equals("1")) {
            this.userInterface.println("Artifact mark as used!");

        } else {
            this.userInterface.println("Artifact already used!");
        }

        this.userInterface.pause();
    }

    private void runMentorStoreController() throws DaoException {

        new MentorStoreController().startController(this.user, this.school);
    }

    private void handleNoSuchCommand() {

        userInterface.println("Wrong command!");
        this.userInterface.pause();
    }
}

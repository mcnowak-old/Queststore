package com.codecool.krk.lucidmotors.queststore.views;

import com.codecool.krk.lucidmotors.queststore.controllers.ManagerController;
import com.codecool.krk.lucidmotors.queststore.enums.ManagerOptions;
import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import com.codecool.krk.lucidmotors.queststore.models.Activity;
import com.codecool.krk.lucidmotors.queststore.models.School;
import com.codecool.krk.lucidmotors.queststore.models.Student;
import com.codecool.krk.lucidmotors.queststore.models.User;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ManagerView {
    private School school;
    User user;
    Map<String, String> formData;

    public ManagerView(School school, User user, Map<String, String> formData) {
        this.school = school;
        this.user = user;
        this.formData = formData;

    }

    public Activity getActivity(ManagerOptions managerOption) throws IOException {
        String response;
        JtwigTemplate template = JtwigTemplate.classpathTemplate("/templates/main.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("title", managerOption.toString());
        model.with("menu_path", "classpath:/templates/snippets/manager-menu-snippet.twig");
        model.with("is_text_available", false);
        try {
            insertData(managerOption, model, formData);
        } catch (DaoException e) {
            e.printStackTrace();
        }

        String contentPath = "classpath:/" + managerOption.getPath();
        model.with("content_path", contentPath);

        response = template.render(model);

        return new Activity(200, response);
    }

    private void insertData(ManagerOptions managerOption, JtwigModel model, Map<String, String> formData) throws DaoException {
        switch (managerOption) {
            case SHOW_MENTORS_CLASS:
                if (formData.containsKey("mentor_id")) {
                    Integer mentorId = Integer.valueOf(formData.get("mentor_id"));
                    List<Student> studentList = new ManagerController(this.school).getMentorClass(mentorId);
                    model.with("students", studentList);
                    model.with("selected_mentor_id", mentorId);
                }

                model.with("mentors", this.school.getAllMentors());
                break;

            case EDIT_MENTOR:
                model.with("mentors", this.school.getAllMentors());
                if(formData.containsKey("mentor_id") &&
                        new ManagerController(this.school).editMentor(formData)) {
                    model.with("is_text_available", true);
                    model.with("text", "Mentor successfully updated");
                }
                break;

            case ADD_MENTOR:
                model.with("school_classes", this.school.getAllClasses());
                if(formData.containsKey("class_id") &&
                        new ManagerController(this.school).addMentor(formData)) {
                    model.with("is_text_available", true);
                    model.with("text", "Mentor successfully created");
                }
                break;

            case CREATE_CLASS:
                if(formData.containsKey("classname") && new ManagerController(this.school).createClass(formData)) {
                    model.with("is_text_available", true);
                    model.with("text", "Class successfully created");
                }
                break;
        }
    }


}

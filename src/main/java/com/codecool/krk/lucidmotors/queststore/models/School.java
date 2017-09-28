package com.codecool.krk.lucidmotors.queststore.models;

import java.util.ArrayList;
import java.sql.SQLException;

import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import com.codecool.krk.lucidmotors.queststore.models.User;
import com.codecool.krk.lucidmotors.queststore.dao.ClassDao;
import com.codecool.krk.lucidmotors.queststore.dao.ManagerDao;
import com.codecool.krk.lucidmotors.queststore.dao.MentorDao;
import com.codecool.krk.lucidmotors.queststore.dao.StudentDao;
import com.codecool.krk.lucidmotors.queststore.exceptions.LoginInUseException;

public class School {

    private final String name;
    private final ClassDao classDao;
    private final ManagerDao managerDao;
    private final MentorDao mentorDao;
    private final StudentDao studentDao;

    public School(String name) throws DaoException {

        this.name = name;
        this.classDao = new ClassDao();
        this.managerDao = new ManagerDao();
        this.mentorDao = new MentorDao(this.classDao);
        this.studentDao = new StudentDao(this.classDao);
    }

    public User getUser(String login) throws DaoException {

        User foundUser = null;

        foundUser = managerDao.getManager(login);
        if (foundUser != null) {
            return foundUser;
        }

        foundUser = mentorDao.getMentor(login);
        if (foundUser != null) {
            return foundUser;
        }

        foundUser = studentDao.getStudent(login);

        return foundUser;
    }

    public Mentor getMentor(Integer id) throws DaoException {
        return this.mentorDao.getMentor(id);
    }

    public void isLoginAvailable(String login) throws LoginInUseException, DaoException {

        if (this.getUser(login) != null) {
            throw new LoginInUseException();
        }
    }

    public ArrayList<SchoolClass> getAllClasses() throws DaoException {

        return this.classDao.getAllClasses();
    }

    public ArrayList<Mentor> getAllMentors() throws DaoException {
        return this.mentorDao.getAllMentors();
    }

}

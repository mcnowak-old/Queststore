package com.codecool.krk.lucidmotors.queststore.dao;

import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import com.codecool.krk.lucidmotors.queststore.models.QuestCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuestCategoryDao {

    private final Connection connection;
    private PreparedStatement stmt = null;

    public QuestCategoryDao() throws DaoException {

        this.connection = DatabaseConnection.getConnection();
    }

    public QuestCategory getQuestCategory(Integer id) throws DaoException {

        QuestCategory questCategory = null;
        String sqlQuery = "SELECT * FROM quest_categories WHERE id = ?;";

        try {
            stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                questCategory = new QuestCategory(name, id);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

        return questCategory;
    }

    public QuestCategory getQuestCategory(String name) throws DaoException {

        QuestCategory questCategory = null;
        String sqlQuery = "SELECT * FROM quest_categories WHERE name = ?;";

        try {
            stmt = connection.prepareStatement(sqlQuery);
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                Integer id = result.getInt("id");
                questCategory = new QuestCategory(name, id);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

        return questCategory;
    }

    public ArrayList<QuestCategory> getAllQuestCategories() throws DaoException {

        ArrayList<QuestCategory> questCategories = new ArrayList<>();
        String sqlQuery = "SELECT * FROM quest_categories";

        try {
            stmt = connection.prepareStatement(sqlQuery);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                QuestCategory questCategory = new QuestCategory(name, id);
                questCategories.add(questCategory);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

        return questCategories;
    }

    public void save(QuestCategory questCategory) throws DaoException {

        String name = questCategory.getName();
        String sqlQuery = "INSERT INTO quest_categories (name) VALUES (?);";

        try {
            stmt = connection.prepareStatement(sqlQuery);
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

    }

}

/*
 * To change this Batch header, choose Batch Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsockets.imageanalyzer.dataaccess;

import com.mysql.jdbc.Connection;
import com.popsockets.imageanalyzer.dataobject.computervision.DOTag;
import com.sunketa.mysql.db.IDbSession;
import com.sunketa.exceptions.CustomException;
import com.sunketa.exceptions.DatabaseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.Logger;

/**
 *
 * @author Suranga
 */
public class DAOTag {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public void create(IDbSession dbSession, String recipeId, String email, DOTag tag) throws CustomException {

        try {

            long currentTime = Instant.now().getEpochSecond();

            Connection dbs = (Connection) dbSession.get();

            String sql = "insert into tag (recipe_id,email,tag_name,confidence_threshold,last_updated_time) values (?,?,?,?,?)";

            try (PreparedStatement preparedStatement = dbs.prepareStatement(sql)) {

                preparedStatement.setString(1, recipeId);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, tag.getName());
                preparedStatement.setDouble(4, tag.getConfidence());
                preparedStatement.setLong(5, currentTime);
                preparedStatement.execute();

                preparedStatement.close();

            } catch (SQLException ex) {

                logger.error(ex.getMessage());
                throw ex;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
    }

    public boolean delete(IDbSession dbSession, String recipeId, String email) throws CustomException {

        boolean isDelete = false;

        try {

            Connection dbs = (Connection) dbSession.get();

            String sql = "delete from tag where recipe_id = ? and email = ?";

            try (PreparedStatement preparedStatement = dbs.prepareStatement(sql)) {
                preparedStatement.setString(1, recipeId);
                preparedStatement.setString(2, email);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                isDelete = true;

            } catch (SQLException ex) {
                logger.error(ex.getMessage());
                throw ex;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }

        return isDelete;

    }

    public ArrayList<DOTag> list(IDbSession dbSession, String recipeId, String email) throws CustomException {
        DOTag tag;
        ArrayList<DOTag> list = new ArrayList<>();
        try {

            Connection dbs = (Connection) dbSession.get();

            Statement stmt = dbs.createStatement();

            String sql = "select * from tag where recipe_id = '" + recipeId + "' and email ='" + email + "'";

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    tag = new DOTag();
                    tag.setName(rs.getString("tag_name"));
                    tag.setConfidence(rs.getDouble("confidence_threshold"));
                    list.add(tag);
                }
                rs.close();
            }
            stmt.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
        return list;
    }

    public ArrayList<DOTag> list(IDbSession dbSession, String recipeId, String email, int page, int limit) throws CustomException {
        DOTag tag;
        ArrayList<DOTag> list = new ArrayList<>();
        try {

            Connection dbs = (Connection) dbSession.get();

            Statement stmt = dbs.createStatement();

            String sql = "select * from tag where recipe_id = '" + recipeId + "' and email ='" + email + "'";

            sql = sql + " limit " + limit + " offset " + ((page - 1) * limit);

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    tag = new DOTag();
                    tag.setName(rs.getString("tag_name"));
                    tag.setConfidence(rs.getDouble("confidence_threshold"));
                    list.add(tag);
                }
                rs.close();
            }
            stmt.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
        return list;
    }

    public int count(IDbSession dbSession, String recipeId, String email) throws CustomException {
        int count = 0;
        try {

            Connection dbs = (Connection) dbSession.get();

            Statement stmt = dbs.createStatement();

            String sql = "select count(*) as count from tag where recipe_id = '" + recipeId + "' and email ='" + email + "'";

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    count = rs.getInt("count");
                    break;
                }
                rs.close();
            }

            stmt.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new DatabaseException(ex.getMessage());
        }
        return count;
    }

    public boolean isExistsByRecipeIdAndEmail(IDbSession dbSession, String recipeId, String email) {
        boolean isExist = false;
        try {
            Connection dbs = (Connection) dbSession.get();

            Statement stmt = dbs.createStatement();

            String sql = "select * from tag where recipe_id = '" + recipeId + "' and email ='" + email + "'";

            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    isExist = true;
                    break;
                }
                rs.close();
            }

            stmt.close();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return isExist;
    }

}

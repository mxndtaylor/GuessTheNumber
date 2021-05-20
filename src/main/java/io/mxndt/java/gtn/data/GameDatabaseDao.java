package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.service.GTNPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mxndt
 */
@Repository
public class GameDatabaseDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game add(Game game) throws GTNPersistenceException {
        final String sql = "INSERT INTO Game(GameInProgress, GameAnswer) "
                + "VALUES(?,?);";
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS);

                statement.setBoolean(1, game.isInProgress());
                statement.setInt(2, game.getAnswer());

                return statement;
            }, keyHolder);

            game.setId(keyHolder.getKey().intValue());
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting to create a game.");
        }

        return game;
    }

    @Override
    public List<Game> getAllGames() throws GTNPersistenceException {
        final String sql = "SELECT Id, GameInProgress FROM Game";
        try {
            return jdbcTemplate.query(sql, new GameMapper());
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting to fetch game list.");
        }
    }

    @Override
    public Game getGame(int gameId) throws GTNPersistenceException {
        final String sqlGetGame = "SELECT Id, GameAnswer, GameInProgress "
                + "FROM Game WHERE Id = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlGetGame, new GameMapper(), gameId);
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting to fetch that game.");
        }
    }

    @Override
    public boolean updateStatus(int gameId, boolean inProgress) throws GTNPersistenceException {
        Game game = getGame(gameId);
        if (game == null || game.isInProgress() == inProgress) {
            return false;
        }
        final String sql = "UPDATE Game (GameInProgress = ?) WHERE Id = ?";

        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS);

                statement.setBoolean(1, false);
                statement.setInt(2, gameId);

                return statement;
            }, keyHolder);
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting to update that game.");
        }

        return true;
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int i) throws SQLException {
            Game gm = new Game();
            gm.setId(rs.getInt("Id"));
            gm.setAnswer(rs.getInt("GameAnswer"));
            gm.setInProgress(rs.getBoolean("GameInProgress"));
            return gm;
        }
    }
}

package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.service.GTNPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

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
        final String sql = "INSERT INTO Game(GameStatus, GameAnswer) "
                + "VALUES(?, ?);";
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, game.getStatus());
                statement.setInt(2, game.getAnswer());

                return statement;
            }, keyHolder);

            game.setId(keyHolder.getKey().intValue());
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting to create a game.", t);
        }

        return game;
    }

    @Override
    public List<Game> getAllGames() throws GTNPersistenceException {
        final String sql = "SELECT Id, GameStatus, GameAnswer FROM Game;";
        try {
            return jdbcTemplate.query(sql, new GameMapper());
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting to fetch game list.", t);
        }
    }

    @Override
    public Game getGame(int gameId) throws GTNPersistenceException {
        final String sqlGetGame = "SELECT Id, GameAnswer, GameStatus "
                + "FROM Game WHERE Id = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlGetGame, new GameMapper(), gameId);
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting to fetch that game.", t);
        }
    }

    @Override
    public void updateStatus(Game game) throws GTNPersistenceException {
        final String sql = "UPDATE Game SET GameStatus = ? WHERE Id = ?;";

        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update((Connection conn) -> {
                PreparedStatement statement = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS);

                statement.setString(1, game.getStatus());
                statement.setInt(2, game.getId());

                return statement;
            }, keyHolder);
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting to update that game.", t);
        }
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int i) throws SQLException {
            Game gm = new Game();
            gm.setId(rs.getInt("Id"));
            gm.setAnswer(rs.getInt("GameAnswer"));
            gm.setStatus(rs.getString("GameStatus"));
            return gm;
        }
    }
}

package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.models.Game;
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
    public Game createGame() {
        Game game = new Game();
        game.setInProgress(true);
        game.setAnswer(generateAnswer());

        final String sql = "INSERT INTO Game(GameInProgress, GameAnswer) "
                + "VALUES(?,?);";
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

        return game;
    }

    @Override
    public List<Game> getAllGames() {
        final String sql = "SELECT Id, GameInProgress FROM Game";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game getGame(int gameId) {
        final String sqlGetGame = "SELECT Id, GameAnswer, GameInProgress "
                + "FROM Game WHERE Id = ?;";
        return jdbcTemplate.queryForObject(sqlGetGame, new GameMapper(), gameId);
    }

    @Override
    public boolean updateStatus(int gameId, boolean inProgress) {
        Game game = getGame(gameId);
        if (game == null || game.isInProgress() == inProgress) {
            return false;
        }
        final String sql = "UPDATE Game (GameInProgress = ?) WHERE Id = ?";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setBoolean(1, false);
            statement.setInt(2, gameId);

            return statement;
        }, keyHolder);

        return true;
    }

    /**
     * helper function that generates a random answer
     *
     * @return - int: a 4 digit answer with no repeated digits
     */
    private int generateAnswer() {
        Set<Integer> answerDigits = new HashSet<>();

        while (answerDigits.size() < 4) {
            answerDigits.add((int) (Math.random() * 10));
        }

        int answer = 0;
        int i = 0;
        for (int digit : answerDigits) {
            answer += digit * Math.pow(10, i);
            i++;
        }
        return answer;
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

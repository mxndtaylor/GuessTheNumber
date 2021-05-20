package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.models.Round;
import io.mxndt.java.gtn.service.GTNPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.List;

/**
 * @author mxndt
 */
@Repository
public class RoundDatabaseDao implements RoundDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoundDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Round add(Round round) throws GTNPersistenceException {
        final String sql = "INSERT INTO "
                + "Round(GuessValue, GuessResult, GameId, GuessTime) "
                + "VALUES(?, ?, ?, CURRENT_TIMESTAMP)";
        Timestamp ts = Timestamp.from(Instant.now());
        round.setTimeOfGuess(ts);
        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update((Connection conn) -> {

                PreparedStatement statement = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS);

                statement.setInt(1, round.getGuess());
                statement.setString(2, round.getResult());
                statement.setInt(3, round.getGameId());
                return statement;

            }, keyHolder);
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while trying to submit this guess", t);
        }

        return round;
    }

    @Override
    public List<Round> getRounds(int gameId) throws GTNPersistenceException {
        final String sql = "SELECT GameId, GuessValue, GuessTime, GuessResult "
                + "FROM Round WHERE GameId = ?";
        try {
            return jdbcTemplate.query(sql, new RoundMapper(), gameId);
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while attempting "
                    + "to find the rounds of that game.", t);
        }
    }

    @Override
    public void delete(Round round) throws GTNPersistenceException {
        final String sql = "DELETE FROM round WHERE GameId = ? AND GuessTime = ?";

        try {
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update((Connection conn) -> {

                PreparedStatement statement = conn.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS);

                statement.setInt(1, round.getGameId());
                statement.setTimestamp(2, round.getGuessTime());
                return statement;

            }, keyHolder);
        } catch (Throwable t) {
            throw new GTNPersistenceException("An error occurred while trying to delete this round.", t);
        }
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int i) throws SQLException {
            Round rd = new Round();
            rd.setGuess(rs.getInt("GuessValue"));
            rd.setTimeOfGuess(rs.getTimestamp("GuessTime"));
            rd.setGameId(rs.getInt("GameId"));
            rd.setResult(rs.getString("GuessResult"));
            return rd;
        }
    }
}

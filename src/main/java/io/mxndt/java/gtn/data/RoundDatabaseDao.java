package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.models.Round;
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
public class RoundDatabaseDao implements RoundDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoundDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Round add(Round round) {
        final String sql = "INSERT INTO "
                + "Round(GuessValue, GuessResult, GameId, GuessTime) "
                + "VALUES(?, ?, ?, CURRENT_TIMESTAMP)";
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

        return round;
    }

    @Override
    public List<Round> getRounds(int gameId) {
        final String sql = "SELECT GuessValue, GuessTime, GuessResult "
                + "FROM Round WHERE GameId = ?";
        return jdbcTemplate.query(sql, new RoundMapper(), gameId);
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int i) throws SQLException {
            Round rd = new Round();
            rd.setGuess(rs.getInt("GuessValue"));
            rd.setTimeOfGuess(rs.getTime("GuessTime"));
            //rd.setGameId(rs.getInt("GameId"));
            rd.setResult(rs.getString("GuessResult"));
            return rd;
        }
    }
}

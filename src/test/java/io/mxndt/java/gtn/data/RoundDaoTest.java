package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.TestApplicationConfiguration;
import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.models.Round;
import io.mxndt.java.gtn.service.GTNPersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class RoundDaoTest {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private RoundDao roundDao;

    private final int[] testAnswers = {1234, 123, 10, 9999, 444, 8485, 8962, 3452};
    private final int[] testGuesses = {5, 0, 11, 23000, 443, 1, 23, 4411, 1234, 3452, 123};

    private List<Integer> gameIds;

    public RoundDaoTest () {
    }

    @Before
    public void setUp() throws Exception {
        // clear the test database
        // remove all games (removes all rounds too)
        List<Game> games = gameDao.getAllGames();
        for (Game game : games) {
            gameDao.delete(game);
        }

        // Add a bunch of dummy games to get started
        gameIds = new ArrayList<>();
        for (int testAnswer : testAnswers) {
            Game created = new Game();
            created.setAnswer(testAnswer);
            created.setStatus(Game.IN_PROGRESS);
            int gameId = gameDao.add(created).getId();
            gameIds.add(gameId);
        }
    }

    @Test
    public void testAddRound() throws GTNPersistenceException {
        // test legal add
        Round created = new Round();
        for (int testGuess : testGuesses) {
            created.setGuess((int) (Math.random() * 10000));
            created.setGameId(gameIds.get((int) (Math.random() * gameIds.size())));
            created.setResult(String.format(
                    "e:%c:p:%c",
                    Character.forDigit((int) (Math.random() * 4), 10),
                    Character.forDigit((int) (Math.random() * 4), 10))
            );
        }

        Round dbCreated = roundDao.add(created);
        created.setGuessTime(dbCreated.getGuessTime());
        assertEquals(created,dbCreated);

        // illegal add should
        Round round = new Round();
        round.setGameId(gameIds.get(0) - 1);  // guaranteed not to be in the db
        round.setGuess((int) (Math.random() * 10000));
        round.setResult(String.format(
                "e:%c:p:%c",
                Character.forDigit((int) (Math.random() * 4), 10),
                Character.forDigit((int) (Math.random() * 4), 10))
        );
        try {
            roundDao.add(round);
            fail("illegal add succeeded or failed silently");
        } catch (GTNPersistenceException ignored) {} // expected result
    }

    @Test
    public void testGetRounds() throws GTNPersistenceException {
        // create test data
        Map<Integer, List<Round>> roundMap = generateTestData();

        // test against database data
        for (int gameId : gameIds) {
            List<Round> dbFetchedList = roundDao.getRounds(gameId);
            List<Round> createdList = roundMap.get(gameId);
            assertEquals(new HashSet<>(dbFetchedList), new HashSet<>(createdList));
        }
    }

    @Test
    public void testDelete() throws GTNPersistenceException {
        // create test data
        Map<Integer, List<Round>> roundMap = generateTestData();

        // test legal delete
        int gameId = gameIds.get((int) (Math.random() * gameIds.size()));
        List<Round> rounds = roundMap.get(gameId);
        Round realRound = rounds.get((int) (Math.random() * rounds.size()));
        int id = realRound.getGameId();
        List<Round> roundsBefore = roundDao.getRounds(id);
        assertTrue("Round was not in list before delete", roundsBefore.contains(realRound));

        roundDao.delete(realRound);
        List<Round> roundsAfter = roundDao.getRounds(id);

        try {
            assertEquals("List size mismatch before/after delete", roundsBefore.size() - 1, roundsAfter.size());
        } catch (AssertionError e) {
            try {
                assertEquals("Deleted more than one game.", roundsBefore.size(), roundsAfter.size());
            } catch (AssertionError ae) {
                fail(String.format("Size before: %s\r size after: %s", roundsBefore.size(), roundsAfter.size()));
            }
        }
        assertFalse("Round was in list after delete", roundsAfter.contains(realRound));


        // test illegal delete
        Round round = new Round();
        round.setGameId(gameIds.get(0) - 1);
        round.setGuess(0);
        round.setResult(String.format(
                "e:%c:p:%c",
                Character.forDigit((int) (Math.random() * 4), 10),
                Character.forDigit((int) (Math.random() * 4), 10))
        );
        try {
            roundDao.delete(round);
            fail("illegal delete succeeded or failed silently");
        } catch (GTNPersistenceException ignored) {} // this is expected
    }

    private Map<Integer, List<Round>> generateTestData() throws GTNPersistenceException {
        Map<Integer, List<Round>> roundMap = new HashMap<>();
        for (int gameId : gameIds) {
            roundMap.put(gameId, new ArrayList<>());
            for (int testGuess : testGuesses) {
                Round created = new Round();
                created.setGuess(testGuess);
                created.setGameId(gameId);
                created.setResult(String.format(
                        "e:%c:p:%c",
                        Character.forDigit((int) (Math.random() * 4), 10),
                        Character.forDigit((int) (Math.random() * 4), 10))
                );
                roundMap.get(gameId).add(roundDao.add(created));
            }
        }
        return roundMap;
    }
}
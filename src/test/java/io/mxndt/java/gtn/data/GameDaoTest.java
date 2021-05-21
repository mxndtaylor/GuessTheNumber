package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.TestApplicationConfiguration;
import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.service.GTNPersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoTest {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private RoundDao roundDao;

    private int[] testAnswers = {1234, 123, 10, 9999, 444, 8485, 8962, 3452};

    public GameDaoTest () {
    }

    @Before
    public void setUp() throws Exception {
        // clear the test database
        // remove all games (removes all rounds, too)
        List<Game> games = gameDao.getAllGames();
        for (Game game : games) {
            gameDao.delete(game);
        }
    }

    @Test
    public void testAddGetUpdateGame() throws GTNPersistenceException {
        Game created = new Game();
        created.setAnswer(testAnswers[0]);
        created.setStatus(Game.IN_PROGRESS);

        Game dbCreated = gameDao.add(created);
        Game dbFetched = gameDao.getGame(dbCreated.getId());

        assertEquals("Did not save the game correctly.", dbCreated.getAnswer(), testAnswers[0]);
        assertEquals("Did not retrieve the game correctly "
                + "or did not store it correctly", dbCreated, dbFetched);

        dbFetched.setStatus(Game.FINISHED);
        gameDao.updateStatus(dbFetched);
        Game dbUpdated = gameDao.getGame(dbFetched.getId());

        assertEquals("Did not return an updated game.", dbUpdated, dbFetched);
        assertEquals("Did not update correctly.", dbUpdated.getStatus(), Game.FINISHED);
    }

    @Test
    public void testGetAllGamesDeleteGame() throws GTNPersistenceException {
        // test get all games
        List<Game> createdGames = new ArrayList<>();
        List<Game> dbCreatedGames = new ArrayList<>();
        for (int testAnswer : testAnswers) {
            Game created = new Game();
            created.setAnswer(testAnswer);
            created.setStatus(Game.IN_PROGRESS);
            createdGames.add(created);
            dbCreatedGames.add(gameDao.add(created));
        }
        assertEquals("Creation of test data failed.", createdGames.size(), dbCreatedGames.size());
        List<Game> dbFetchedGames = gameDao.getAllGames();
        assertEquals("Did not store or retrieve data correctly.", dbCreatedGames, dbFetchedGames);


        // test delete legal game
        List<Game> gamesBefore = dbCreatedGames;
        Game gameToDelete = gamesBefore.get((int) (Math.random() * dbFetchedGames.size()));
        assertTrue("Found unexpected game.", gamesBefore.contains(gameToDelete));
        gameDao.delete(gameToDelete);
        List<Game> gamesAfter = gameDao.getAllGames();

        assertEquals("Did not delete exactly one game. (Either more or less)",
                gamesBefore.size() - 1, gamesAfter.size());
        assertFalse("Incorrect game deleted.", gamesAfter.contains(gameToDelete));

        // test delete illegal game
        gamesBefore = gameDao.getAllGames();
        Game illegalGame = new Game();
        illegalGame.setId(gamesBefore.get(0).getId() - 1);
        try {
            gameDao.delete(illegalGame);
            fail("Illegal game deleted successfully or silently failed.");
        } catch (GTNPersistenceException ignored) {}
    }
}
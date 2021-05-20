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

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoTest {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private RoundDao roundDao;

    public GameDaoTest () {
    }

    @Before
    public void setUp() throws Exception {
        List<Game> games = gameDao.getAllGames();
        for (Game game : games) {
            gameDao.delete(game);
        }
    }

    @Test
    public void testAddGetGame() throws GTNPersistenceException {
        Game created = new Game();
        created.setAnswer(1234);
        created.setStatus(Game.IN_PROGRESS);

        Game dbCreated = gameDao.add(created);
        Game dbFetched = gameDao.getGame(dbCreated.getId());


    }

    @Test
    public void getAllGames() {
    }

    @Test
    public void updateStatus() {
    }
}
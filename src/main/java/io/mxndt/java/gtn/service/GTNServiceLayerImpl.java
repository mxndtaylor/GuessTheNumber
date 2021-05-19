package io.mxndt.java.gtn.service;

import io.mxndt.java.gtn.data.GameDao;
import io.mxndt.java.gtn.data.RoundDao;
import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mxndt
 */
@Service
public class GTNServiceLayerImpl implements GTNServiceLayer {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private RoundDao roundDao;

    @Autowired
    public GTNServiceLayerImpl(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @Override
    public Game createGame() {
        return null;
    }

    @Override
    public Game getGame(int gameId) throws GTNGameNotFoundException {
        return null;
    }

    @Override
    public List<Game> getGames() {
        return null;
    }

    @Override
    public Round guess(Round round) {
        return null;
    }

    @Override
    public List<Round> getGameRounds(int gameId) throws GTNGameNotFoundException {
        return null;
    }

    private int generateNewGameAnswer() {
        return 0;
    }

    private int[] guessMatchesAnswer(int guess, int answer) {
        return new int[]{0, 0};
    }

}

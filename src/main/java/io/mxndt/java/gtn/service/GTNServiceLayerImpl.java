package io.mxndt.java.gtn.service;

import io.mxndt.java.gtn.data.GameDao;
import io.mxndt.java.gtn.data.RoundDao;
import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;

/**
 * @author mxndt
 */
@Service
public class GTNServiceLayerImpl implements GTNServiceLayer {

    @Autowired
    private final GameDao gameDao;

    @Autowired
    private final RoundDao roundDao;

    @Autowired
    public GTNServiceLayerImpl(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    @Override
    public Game createGame() throws
            GTNPersistenceException {
        Game game = new Game();
        game.setInProgress(true);
        game.setAnswer(generateNewGameAnswer());
        return filterAnswer(gameDao.add(game));
    }

    @Override
    public Game getGame(int gameId) throws
            GTNGameNotFoundException,
            GTNPersistenceException {
        Game game = gameDao.getGame(gameId);
        if (game == null) {
            throw new GTNGameNotFoundException("No game by that id.");
        }
        if (gameId != game.getId()) {
            throw new GTNPersistenceException("An Error occurred while finding the game with that id.");
        }
        game = filterAnswer(game);
        return game;
    }

    @Override
    public List<Game> getGames() throws
            GTNPersistenceException {
        List<Game> result = gameDao.getAllGames();
        for (int i = 0; i < result.size(); i++) {
            result.set(i, filterAnswer(result.get(i)));
        }
        return result;
    }

    @Override
    public Round guess(Round round) throws
            GTNGameNotFoundException,
            GTNGameFinishedException,
            GTNGuessFormatException,
            GTNPersistenceException {
        Game game = gameDao.getGame(round.getGameId());
        if (game == null) {
            throw new GTNGameNotFoundException("No game by that id.");
        }
        if (!game.isInProgress()) {
            throw new GTNGameFinishedException("Game no longer accepting answers.");
        }


        int guess = round.getGuess();
        if (guess >= 10000) {
            throw new GTNGuessFormatException("Guess must be at most 4 digits."
                    + "Fills with leading zeroes if less than 4.");
        }

        int answer = game.getAnswer();
        if (answer < 100 || answer >= 10000) {
            throw new GTNPersistenceException("An error occurred while trying to retrieve game data.");
        }

        String result = guessMatchesAnswer(guess, answer);

        round.setResult(result);
        round.setTimeOfGuess(new Time(System.currentTimeMillis()));
        round = roundDao.add(round);

        if (result.contains("e:4")) {
            game.setInProgress(false);
        }

        return round;
    }

    @Override
    public List<Round> getGameRounds(int gameId) throws
            GTNGameNotFoundException,
            GTNPersistenceException {
        Game game = gameDao.getGame(gameId);
        if (game == null) {
            throw new GTNGameNotFoundException("No game by that id.");
        }

        List<Round> rounds = roundDao.getRounds(gameId);

        return rounds;
    }

    /**
     * helper method that returns a copy of the given game without an answer set
     *
     * @param game - Game: game to copy
     * @return - Game: copy of the game with the answer field unset
     */
    private static Game filterAnswer(Game game) {
        boolean inProgress = game.isInProgress();
        int id = game.getId();
        if (inProgress) {
            game = new Game();
            game.setInProgress(inProgress);
            game.setId(id);
        }
        return game;
    }

    /**
     * helper method that calculates the number of exact and partial matches of a guess to an answer
     *
     * @param guess - int: the guess to match to the answer
     * @param answer - int: the answer to match the guess against to
     * @return - String: formatted as "e:#:p:#" where e and p stand for exact and partial matches
     */
    private static String guessMatchesAnswer(int guess, int answer) throws
            GTNPersistenceException {
        LinkedHashSet<Integer> guessSet = numberToDigits(guess);
        LinkedHashSet<Integer> answerSet = numberToDigits(guess);

        // find intersect for partial matches (but it includes exact matches too)
        Set<Integer> intersectSet = new HashSet<>(guessSet);
        intersectSet.retainAll(answerSet);

        // count exact matches using LinkedHashSet's insertion order retention
        int exactCount = 0;
        Iterator<Integer> answerIter = answerSet.iterator();
        for (int guessDigit : guessSet) {
            int answerDigit;
            try {
                answerDigit = answerIter.next();
            // this block catches if either answer has duplicates or has less than 4 elements
            } catch (NoSuchElementException e) {
                throw new GTNPersistenceException("An error occurred while processing this answer.");
            }
            if (guessDigit == answerDigit) {
                // remove exact matches from the intersection
                intersectSet.remove(guessDigit);
                exactCount++;
            }
        }

        // convert to char to guarantee a single character is return for each count
        char exacts = Character.forDigit(exactCount, 10);
        char partials = Character.forDigit(intersectSet.size(), 10);

        return String.format("e:%c:p:%c", exacts, partials);
    }

    /**
     * helper function that generates a random answer
     *
     * @return - int: a 4 digit answer with no repeated digits
     */
    private static int generateNewGameAnswer() {
        // order is irrelevant, but allowing HashSet to decide order may cause trends
        Set<Integer> answerDigits = new LinkedHashSet<>();

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

    /**
     * helper function to split a four digit number into its digits
     *
     * @param number - int: assumed to be a four digit number
     * @return - int[4]: the first four digits of the number
     */
    private static LinkedHashSet<Integer> numberToDigits(int number) {
        LinkedHashSet<Integer> digits = new LinkedHashSet<>();
        while (number / 10 > 0) {
            digits.add(number % 10);
            number /= 10;
        }
        if (digits.size() == 3) {
            digits.add(0);
        }
        return digits;
    }
}

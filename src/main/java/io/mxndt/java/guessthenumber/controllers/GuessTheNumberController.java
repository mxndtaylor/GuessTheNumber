package io.mxndt.java.guessthenumber.controllers;

import io.mxndt.java.guessthenumber.data.GameDao;
import io.mxndt.java.guessthenumber.models.Game;
import io.mxndt.java.guessthenumber.models.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guessthenumber")
public class GuessTheNumberController {

    @Autowired
    private GameDao dao;

    /**
     * starts a game, generates an answer and sets the correct status
     *
     * @return - int: representing id of the created game on success
     */
    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int begin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * makes a guess on a game
     *
     * @param round - Round: parsed JSON with "gameId" and "guess" fields
     * @return - ResponseEntity<Round>: Round on successful guess attempt
     *           ResponseEntity displays appropriate HttpStatus messages
     *           depending on validity of input
     */
    @PostMapping("/guess")
    public ResponseEntity<Round> guess(@RequestBody Round round) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * fetches list of all games
     *
     * @return - List<Game>: containing all games
     */
    @GetMapping("game")
    public List<Game> getAllGames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * gets a single game by its id
     *
     * @param gameId - int: game id to attempt to fetch
     * @return - ResponseEntity<Game>: ResponseEntity to handle user input,
     *                  Game on valid requests
     */
    @GetMapping("game/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable int gameId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * gets all rounds for a game by its id
     *
     * @param gameId - int: game id to attempt to fetch the rounds of
     * @return - ReponseEntity<List<Round>>: ResponseEntity to handle user input,
     *                  List<Round> on valid requests
     */
    @GetMapping("rounds/{gameId}")
    public ResponseEntity<List<Round>> getGameRounds(@PathVariable int gameId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
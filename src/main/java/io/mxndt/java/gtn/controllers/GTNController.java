package io.mxndt.java.gtn.controllers;

import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.models.Round;
import io.mxndt.java.gtn.service.GTNGameNotFoundException;
import io.mxndt.java.gtn.service.GTNServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mxndt
 */
@RestController
@RequestMapping("/api/guessthenumber")
public class GTNController {

    @Autowired
    private final GTNServiceLayer service;

    public GTNController(GTNServiceLayer service) {
        this.service = service;
    }

    /**
     * starts a game, generates an answer and sets the correct status
     *
     * @return - int: representing id of the created game on success
     */
    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int begin() {
        return service.createGame().getId();
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
        try {
            round = service.guess(round);
        } catch (GTNGameNotFoundException e) {
            return new ResponseEntity("No game found by that ID", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(round);
    }

    /**
     * fetches list of all games
     *
     * @return - List<Game>: containing all games
     */
    @GetMapping("game")
    public List<Game> getAllGames() {
        return service.getGames();
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
        Game game;
        try {
            game = service.getGame(gameId);
        } catch (GTNGameNotFoundException e) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(game);
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
        List<Round> result;
        try {
            result = service.getGameRounds(gameId);
        } catch (GTNGameNotFoundException e) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

}

package io.mxndt.java.gtn.controllers;

import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.models.Round;
import io.mxndt.java.gtn.service.*;
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
    public ResponseEntity<Game> begin() {
        try {
            Game game = service.createGame();
            return new ResponseEntity<>(game, HttpStatus.CREATED);
        } catch (GTNPersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (GTNGuessFormatException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (GTNGameFinishedException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        } catch (GTNPersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(round);
    }

    /**
     * fetches list of all games
     *
     * @return - List<Game>: containing all games
     */
    @GetMapping("game")
    public ResponseEntity<List<Game>> getAllGames() {
        try {
            return new ResponseEntity<>(service.getGames(), HttpStatus.OK);
        } catch (GTNPersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (GTNPersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (GTNPersistenceException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (GTNGameFinishedException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        }
        return ResponseEntity.ok(result);
    }

}

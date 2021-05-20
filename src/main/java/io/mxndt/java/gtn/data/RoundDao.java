package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.models.Round;
import io.mxndt.java.gtn.service.GTNPersistenceException;

import java.util.List;

/**
 * @author mxndt
 */
public interface RoundDao {

    /**
     * Adds a Round
     *
     * @param round - Round: the round to be added
     * @return - Round: the round as it is stored
     * @throws GTNPersistenceException - thrown on operation failure
     */
    Round add(Round round) throws GTNPersistenceException;

    /**
     * fetches all rounds for a certain game
     *
     * @param gameId - int: id of the game whose rounds are to be fetched
     * @return - List<Round>: the list of all rounds associated with the game
     * @throws GTNPersistenceException - thrown on operation failure
     */
    List<Round> getRounds(int gameId) throws GTNPersistenceException;

    /**
     * Removes the given round from the table
     *
     * @param round - Round: the round to be deleted
     * @throws GTNPersistenceException - thrown when the operation fails
     */
    void delete(Round round) throws GTNPersistenceException;
}

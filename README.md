# GuessTheNumber

A guess the number api built with spring boot using maven.

Game is played through HTTP requests sent to `{local host}/api/guessthenumber`.  
This path will be omitted from the endpoints, assume they all start with this.


## Instructions for use

### `POST` request to `/begin` to begin a new game

**Use:**  
Starts a game by creating a new `Game` with a randomized answer.  
Aside from memory limits, there's no limit on the number of games you can create.

**Request Body:**  
The body of the request is irrelevant, just leave it empty. (Or don't, game don't care)

**Expected Response:**  
JSON object file representing a `Game` with the following format:

    {
      "id" : [int],
      "answer" : 0, 
      "status" : "in-progress"
    }

* `id`: important, you will use it in order to guess in games.
* `answer`: displayed as `0`, this is to hide it from the user. 
The answer will be in the range `[123, 9876]` (inclusive) and have unique digits. 
Think of it as a length 4 string with unique chars from the set `[0-9]`.
* `status`: will either be "in-progress" or "finished" to indicate the game state.

### `POST` request to `/guess` to make a guess

**Use:**  
Attempt to win an active `Game` by submitted the number you think is the answer.  
This is known as playing a `Round`.

**Request Body:**  
JSON object file with the following format:

    {
        "gameId" : [int],
        "guess" : [int]
    }

* `gameId`: `id` of the `Game` you would like to guess on. 
  Note that a `Game` with `status` set to `finished` will not accept any new guesses.
* `guess`: your suggestion for the answer. Must be strictly less than `10000`.

**Expected Response:**  
JSON object file representing a `Round` with the following format:

    {
      "gameId" : [int],
      "guess" : [int],
      "guessTime" : [timestamp],
      "result": "e:[0-4]:p:[0-4]"
    }

* `gameId`: should match the `gameId` from the request
* `guess`: should match the `guess` from the request
* `guessTime`: auto-generated timestamp of when the `Round` was created
* `result`: string representing the number of exact `e` and partial `p` matches. 
  * `e`: the number of correct digits in the correct position. 
  * `p`: the number of correct digits in the wrong position.

### `GET` request to `/game` or `/game/{id}` to fetch `Game`s


**Use:**  
Fetches the list of all `Game`s or a single `Game` by its `id`.  

**Expected Response:**  
_EITHER_: JSON object file representing a list of `Game`s with the following format:

    {
      {
        "id" : [int],
        "answer" : [0|answer], 
        "status" : "[in-progress|finished]"
      }
      .
      .
      .
    }
 
_OR:_
JSON object file representing a `Game` with the following format:

    {
      "id" : [int],
      "answer" : [0|answer], 
      "status" : "[in-progress|finished]"
    }

* `id`: as above, the identifier of the `Game` resource
* `answer`: for `Game`s with `status` set as `"in-progress"`, it is displayed as `0`. 
  As stated above, this is to hide it from the user. 
  `Game`s with `status` set to `"finished"` display the full answer.
* `status`: will either be `"in-progress"` or `"finished"` to indicate the game state.

### `GET` request to `/rounds/{id}`

**Use:**  
Fetch all `Round` resources for a `Game` given by `id`.

**Expected Response:**  
JSON object file representing a list of `Round`s with the following format:


    {
      {
        "gameId" : [int],
        "guess" : [int],
        "guessTime" : [timestamp],
        "result": "e:[0-4]:p:[0-4]"
      }
      .
      .
      .
    }

Fields are the same as above, `Round` resources are never changed once stored.

## Implementation Details

Coming soon...
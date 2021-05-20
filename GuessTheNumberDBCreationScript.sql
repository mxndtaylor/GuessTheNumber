create database if not exists guessthenumberdb;

use guessthenumberdb;

drop table if exists round;
drop table if exists game;

create table game(
	Id int primary key auto_increment,
    GameStatus varchar(11) not null,
    GameAnswer int not null
); 

create table round(
    GuessTime timestamp primary key not null,
	GameId int not null,
    foreign key fk_round_game(GameId)
		references game(Id),
    GuessValue int not null,
    GuessResult char(7) not null
);
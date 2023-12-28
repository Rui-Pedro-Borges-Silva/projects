/* -*- Mode:Prolog; coding:iso-8859-1; indent-tabs-mode:nil; prolog-indent-width:8; prolog-paren-indent:4; tab-width:8; -*- */
:-consult('logic.pl').

play:-
        printMainMenu,
        askMenuOption,
        read(Input),
        manageInput(Input).

manageInput(1):-
        write('Valid!\n\n'),
        stltsPP.

manageInput(2):-
        write('Valid!\n\n'),
        stltsPC.

manageInput(3):-
        write('Valid!\n\n'),
        stltsCC.

manageInput(4):-
        write('In the menu, choose the option the mode you want to play'),nl,nl,
        write('Players move alternateively, starting the one with the white pieces'),nl,nl,
        write('On each turn a player may do one of the following:'),nl,nl,
        write('1. insert a white or a black pin in one of players pieces'),nl,
        write('2. move one of his pieces.'),nl,nl,
        write('A piece may move to any cell that can be reached by a sequence of one-cell horizontal and vertical steps where the maximum number of vertical steps is the number of white pins in the piece and the maximum number of horizontal moves the number of black pins.'),nl,
        write('A piece with no pins cannot move.'),
        play.


manageInput(5):-
        write('Going to exit!\n\n'),
        fail.

manageInput(_Other):-
        write('Error: not existing option'\n\n),
        askMenuOption,
        read(Input),
        manageInput(Input).

printMainMenu:-
        nl,nl,
        write(' _______________________________________________________________________ '),nl,
        write('|                                                                       |'),nl,
        write('|                                                                       |'),nl,
        write('|                                STLTS                                  |'),nl,
        write('|               -----------------------------------------               |'),nl,
        write('|                                                                       |'),nl,
        write('|                                                                       |'),nl,
        write('|                          1. Player vs Player                          |'),nl,
        write('|                                                                       |'),nl,
        write('|                          2. Player vs Computer                        |'),nl,
        write('|                                                                       |'),nl,
        write('|                          3. Computer vs Computer                      |'),nl,
        write('|                                                                       |'),nl,
        write('|                          4. How to Play                               |'),nl,
        write('|                                                                       |'),nl,
        write('|                          5. Exit                                      |'),nl,
        write('|                                                                       |'),nl,
        write('|                                                                       |'),nl,
        write(' _______________________________________________________________________ '),nl,nl,nl.

askMenuOption :-
    write('> Insert your option: ').
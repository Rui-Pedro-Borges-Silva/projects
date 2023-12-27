/* -*- Mode:Prolog; coding:iso-8859-1; indent-tabs-mode:nil; prolog-indent-width:8; prolog-paren-indent:4; tab-width:8; -*- */
initialBoard([
[[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty]],
[[empty],[empty],[white,0,0],[empty],[empty],[empty],[empty],[empty],[empty],[black,0,0],[empty],[empty]],
[[empty],[white,0,0],[white,0,0],[empty],[black,0,0],[empty],[empty],[white,0,0],[empty],[black,0,0],[black,0,0],[empty]],
[[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty]],
[[empty],[empty],[black,0,0],[empty],[white,0,0],[empty],[empty],[black,0,0],[empty],[white,0,0],[empty],[empty]],
[[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty]],
[[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty]],
[[empty],[empty],[white,0,0],[empty],[black,0,0],[empty],[empty],[white,0,0],[empty],[black,0,0],[empty],[empty]],
[[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty]],
[[empty],[black,0,0],[black,0,0],[empty],[white,0,0],[empty],[empty],[black,0,0],[empty],[white,0,0],[white,0,0],[empty]],
[[empty],[empty],[black,0,0],[empty],[empty],[empty],[empty],[empty],[empty],[white,0,0],[empty],[empty]],
[[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty],[empty]]
]).                 

symbol([empty],S):- S='    . '.
symbol([black,0,0],S):- S='B(0,0)'.
symbol([black,1,0],S):- S='B(1,0)'.
symbol([black,2,0],S):- S='B(2,0)'.
symbol([black,3,0],S):- S='B(3,0)'.
symbol([black,4,0],S):- S='B(4,0)'.
symbol([black,5,0],S):- S='B(5,0)'.
symbol([black,6,0],S):- S='B(6,0)'.
symbol([black,0,1],S):- S='B(0,1)'.
symbol([black,0,2],S):- S='B(0,2)'.
symbol([black,0,3],S):- S='B(0,3)'.
symbol([black,0,4],S):- S='B(0,4)'.
symbol([black,0,5],S):- S='B(0,5)'.
symbol([black,0,6],S):- S='B(0,6)'.
symbol([black,1,1],S):- S='B(1,1)'.
symbol([black,1,2],S):- S='B(1,2)'.
symbol([black,1,3],S):- S='B(1,3)'.
symbol([black,1,4],S):- S='B(1,4)'.
symbol([black,1,5],S):- S='B(1,5)'.
symbol([black,1,6],S):- S='B(1,6)'.
symbol([black,2,1],S):- S='B(2,1)'.
symbol([black,2,2],S):- S='B(2,2)'.
symbol([black,2,3],S):- S='B(2,3)'.
symbol([black,2,4],S):- S='B(2,4)'.
symbol([black,2,5],S):- S='B(2,5)'.
symbol([black,2,6],S):- S='B(2,6)'.
symbol([black,3,1],S):- S='B(3,1)'.
symbol([black,3,2],S):- S='B(3,2)'.
symbol([black,3,3],S):- S='B(3,3)'.
symbol([black,3,4],S):- S='B(3,4)'.
symbol([black,3,5],S):- S='B(3,5)'.
symbol([black,3,6],S):- S='B(3,6)'.
symbol([black,4,1],S):- S='B(4,1)'.
symbol([black,4,2],S):- S='B(4,2)'.
symbol([black,4,3],S):- S='B(4,3)'.
symbol([black,4,4],S):- S='B(4,4)'.
symbol([black,4,5],S):- S='B(4,5)'.
symbol([black,4,6],S):- S='B(4,6)'.
symbol([black,5,1],S):- S='B(5,1)'.
symbol([black,5,2],S):- S='B(5,2)'.
symbol([black,5,3],S):- S='B(5,3)'.
symbol([black,5,4],S):- S='B(5,4)'.
symbol([black,5,5],S):- S='B(5,5)'.
symbol([black,5,6],S):- S='B(5,6)'.
symbol([black,6,1],S):- S='B(6,1)'.
symbol([black,6,2],S):- S='B(6,2)'.
symbol([black,6,3],S):- S='B(6,3)'.
symbol([black,6,4],S):- S='B(6,4)'.
symbol([black,6,5],S):- S='B(6,5)'.
symbol([black,6,6],S):- S='B(6,6)'.
symbol([white,0,0],S):- S='W(0,0)'.
symbol([white,1,0],S):- S='W(1,0)'.
symbol([white,2,0],S):- S='W(2,0)'.
symbol([white,3,0],S):- S='W(3,0)'.
symbol([white,4,0],S):- S='W(4,0)'.
symbol([white,5,0],S):- S='W(5,0)'.
symbol([white,6,0],S):- S='W(6,0)'.
symbol([white,0,1],S):- S='W(0,1)'.
symbol([white,0,2],S):- S='W(0,2)'.
symbol([white,0,3],S):- S='W(0,3)'.
symbol([white,0,4],S):- S='W(0,4)'.
symbol([white,0,5],S):- S='W(0,5)'.
symbol([white,0,6],S):- S='W(0,6)'.
symbol([white,1,1],S):- S='W(1,1)'.
symbol([white,1,2],S):- S='W(1,2)'.
symbol([white,1,3],S):- S='W(1,3)'.
symbol([white,1,4],S):- S='W(1,4)'.
symbol([white,1,5],S):- S='W(1,5)'.
symbol([white,1,6],S):- S='W(1,6)'.
symbol([white,2,1],S):- S='W(2,1)'.
symbol([white,2,2],S):- S='W(2,2)'.
symbol([white,2,3],S):- S='W(2,3)'.
symbol([white,2,4],S):- S='W(2,4)'.
symbol([white,2,5],S):- S='W(2,5)'.
symbol([white,2,6],S):- S='W(2,6)'.
symbol([white,3,1],S):- S='W(3,1)'.
symbol([white,3,2],S):- S='W(3,2)'.
symbol([white,3,3],S):- S='W(3,3)'.
symbol([white,3,4],S):- S='W(3,4)'.
symbol([white,3,5],S):- S='W(3,5)'.
symbol([white,3,6],S):- S='W(3,6)'.
symbol([white,4,1],S):- S='W(4,1)'.
symbol([white,4,2],S):- S='W(4,2)'.
symbol([white,4,3],S):- S='W(4,3)'.
symbol([white,4,4],S):- S='W(4,4)'.
symbol([white,4,5],S):- S='W(4,5)'.
symbol([white,4,6],S):- S='W(4,6)'.
symbol([white,5,1],S):- S='W(5,1)'.
symbol([white,5,2],S):- S='W(5,2)'.
symbol([white,5,3],S):- S='W(5,3)'.
symbol([white,5,4],S):- S='W(5,4)'.
symbol([white,5,5],S):- S='W(5,5)'.
symbol([white,5,6],S):- S='W(5,6)'.
symbol([white,6,1],S):- S='W(6,1)'.
symbol([white,6,2],S):- S='W(6,2)'.
symbol([white,6,3],S):- S='W(6,3)'.
symbol([white,6,4],S):- S='W(6,4)'.
symbol([white,6,5],S):- S='W(6,5)'.
symbol([white,6,6],S):- S='W(6,6)'.




letter(1,L):- L='   12    '.
letter(2,L):- L='   11    '.
letter(3,L):- L='   10    '.
letter(4,L):- L='   9     '.
letter(5,L):- L='   8     '.
letter(6,L):- L='   7     '.
letter(7,L):- L='   6     '.
letter(8,L):- L='   5     '.
letter(9,L):- L='   4     '.
letter(10,L):- L='   3     '.
letter(11,L):- L='   2     '.
letter(12,L):- L='   1     '.

printBoard(X):-
        nl,
        write('         |    A    |    B    |    C    |    D    |    E    |    F    |    G    |    H    |    I    |    J    |    K    |    L    |\n'),
        write('---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|\n'),
        printMatrix(X,1).
  

printMatrix([], 13).

printMatrix([Head|Tail], N) :-
        letter(N,L),
        write(''),
        write(L),
        N1 is N+1,
        write('|'),
        printLine(Head),
        write('\n---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|---------|\n'),
        printMatrix(Tail,N1).

printLine([]).

printLine([Head|Tail]):-
        symbol(Head,S),
        write(S),
        write('   |'),
        printLine(Tail).
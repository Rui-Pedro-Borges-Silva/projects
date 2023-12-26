/* -*- Mode:Prolog; coding:iso-8859-1; indent-tabs-mode:nil; prolog-indent-width:8; prolog-paren-indent:4; tab-width:8; -*- */
:-use_module(library(lists)).
:-use_module(library(random)).
:- consult('board.pl').
:- consult('input.pl').
:- consult('movement.pl').

replaceInList([_|T], 0, Value, [Value|T]).
replaceInList([H|T], Index, Value, [H|TNew]):-
        Index>0,
        Index1 is Index-1,
        replaceInList(T,Index1, Value,TNew).

replaceInMatrix([H|T], 0, Column, Value, [HNew|T]):-
        replaceInList(H,Column,Value,HNew).
replaceInMatrix([H|T], Row, Column,Value,[H|TNew]):-
        Row>0,
        Row1 is Row-1,
        replaceInMatrix(T, Row1, Column, Value, TNew).

getValueFromList([H|_T], 0, Value) :-
        Value = H.

getValueFromList([_H|T], Index, Value) :-
        Index > 0,
        Index1 is Index - 1,
        getValueFromList(T, Index1, Value).

getValueFromMatrix([H|_T], 0, Column, Value) :-
        getValueFromList(H, Column, Value).

getValueFromMatrix([_H|T], Row, Column, Value) :-
        Row > 0,
        Row1 is Row - 1,
        getValueFromMatrix(T, Row1, Column, Value).

addPinoW(Board,black,'B', FinalBoard,'P'):-
        FinalBoard=Board.
addPinoW(Board,black,'B', FinalBoard,'C'):-
        FinalBoard=Board.

addPinoW(Board,white,'B', FinalBoard,'P'):-
        FinalBoard=Board.
addPinoW(Board,white,'B', FinalBoard,'C'):-
        FinalBoard=Board.

addPinoW(Board,white, 'W', FinalBoard,'P'):-
        write('  >Row     '),
        read(RowLetter),
        validateRow(RowLetter,Row),
        write('  > Column '),
        read(ColumnLetter),
        validateColumn(ColumnLetter,Column),
        write('\n'),
        ColumnIndex is Column-1,
        RowIndex is Row-1,
        getValueFromMatrix(Board,RowIndex,ColumnIndex,V),
        indexOf(V,S,0),
        (S==white, substituiWPino(Board,RowIndex,ColumnIndex,V,FinalBoard));
        write('ERROR: Choose a black piece!\n'),
        addPino(Board,black,'Y',FinalBoard,'P').

addPinoW(Board,white, 'W', FinalBoard,'C'):-
        getCoordinatesWhiteMatrix(Board,0,0,Res),
        indexOf(Res,X,0),
        indexOf(Res,Y,1),
        getValueFromMatrix(Board,X,Y,V),
        substituiWPino(Board,X,Y,V,FinalBoard).

         

addPinoW(Board,black, 'W', FinalBoard,'C'):-
        getCoordinatesBlackMatrix(Board,0,0,Res),
        indexOf(Res,X,0),
        indexOf(Res,Y,1),
        getValueFromMatrix(Board,X,Y,V),
        substituiWPino(Board,X,Y,V,FinalBoard).

        
addPinoW(Board,black, 'W', FinalBoard,'P'):-
        write('  >Row     '),
        read(RowLetter),
        validateRow(RowLetter,Row),
        write('  > Column '),
        read(ColumnLetter),
        validateColumn(ColumnLetter,Column),
        write('\n'),
        ColumnIndex is Column-1,
        RowIndex is Row-1,
        getValueFromMatrix(Board,RowIndex,ColumnIndex,V),
        indexOf(V,S,0),
        (S==black, substituiWPino(Board,RowIndex,ColumnIndex,V,FinalBoard));
        write('ERROR: Choose a black piece!\n'),
        addPino(Board,white,'Y',FinalBoard,'P').
        %substituiWPino(Board,RowIndex,ColumnIndex,V,FinalBoard).

addPinoB(Board,black, 'W', FinalBoard,'P'):-
        FinalBoard=Board.
addPinoB(Board,white, 'W', FinalBoard,'P'):-
        FinalBoard=Board.
addPinoB(Board,black, 'W', FinalBoard,'C'):-
        FinalBoard=Board.
addPinoB(Board,white, 'W', FinalBoard,'C'):-
        FinalBoard=Board.
addPinoB(Board,black, 'B', FinalBoard,'P'):-
        write('  >Row     '),
        read(RowLetter),
        validateRow(RowLetter,Row),
        write('  > Column '),
        read(ColumnLetter),
        validateColumn(ColumnLetter,Column),
        write('\n'),
        ColumnIndex is Column-1,
        RowIndex is Row-1,
        getValueFromMatrix(Board,RowIndex,ColumnIndex,V),
        indexOf(V,S,0),
        (S==black, substituiBPino(Board,RowIndex,ColumnIndex,V,FinalBoard));
        write('ERROR: Choose a black piece!\n'),
        addPino(Board,black,'Y',FinalBoard,'P').
        %substituiBPino(Board,RowIndex,ColumnIndex,V,FinalBoard).
addPinoB(Board,white, 'B', FinalBoard,'P'):-
        write('  >Row     '),
        read(RowLetter),
        validateRow(RowLetter,Row),
        write('  > Column '),
        read(ColumnLetter),
        validateColumn(ColumnLetter,Column),
        write('\n'),
        ColumnIndex is Column-1,
        RowIndex is Row-1,
        getValueFromMatrix(Board,RowIndex,ColumnIndex,V),
        indexOf(V,S,0),
        (S==white, substituiBPino(Board,RowIndex,ColumnIndex,V,FinalBoard));
        write('ERROR: Choose a white piece!\n'),
        addPino(Board,white,'Y',FinalBoard,'P').

addPinoB(Board,black, 'B', FinalBoard,'C'):-
        getCoordinatesBlackMatrix(Board,0,0,Res),
        indexOf(Res,X,0),
        indexOf(Res,Y,1),
        getValueFromMatrix(Board,X,Y,V),
        substituiBPino(Board,X,Y,V,FinalBoard).

addPinoB(Board,white, 'B', FinalBoard,'C'):-
        getCoordinatesWhiteMatrix(Board,0,0,Res),
        indexOf(Res,X,0),
        indexOf(Res,Y,1),
        getValueFromMatrix(Board,X,Y,V),
        substituiBPino(Board,X,Y,V,FinalBoard).


substituiBPino(Board, Row, Column, [black, N, M], NewBoard):-
        N1 is N + 1,
        replaceInMatrix(Board,Row,Column, [black, N1, M], NewBoard).

substituiBPino(Board, Row, Column, [white, N, M], NewBoard):-
        N1 is N + 1,
        replaceInMatrix(Board,Row,Column, [white, N1, M], NewBoard).

substituiBPino(Board,_,_,[empty],NewBoard):-
        write('ERROR: No piece in that place.\n\n'),
        addPino(Board,black,'Y',NewBoard,'P').

substituiWPino(Board, Row, Column, [black, N, M], NewBoard):-
        M1 is M + 1,
        replaceInMatrix(Board,Row,Column, [black, N, M1], NewBoard).

substituiWPino(Board,_,_,[empty],NewBoard):-
        write('ERROR: No piece in that place.\n\n'),
        addPino(Board,black,'Y',NewBoard,'P').

substituiWPino(Board, Row, Column, [white, N, M], NewBoard):-
        M1 is M + 1,
        replaceInMatrix(Board,Row,Column, [white, N, M1], NewBoard).

addPino(Board, black,'Y', NewBoard,'P') :-
       write('\n2. Black or white pin? (B/W)'),
       read(PinoBW),
       addPinoB(Board,black,PinoBW,FinalBoard,'P'),
       addPinoW(FinalBoard,black,PinoBW,NewBoard,'P').

addPino(Board, black,'Y', NewBoard,'C') :-
       write('\n2. Black or white pin? (B/W)\n'),
       getRandomBW(['B','W'],Res),
       addPinoB(Board,black,Res,FinalBoard,'C'),
       addPinoW(FinalBoard,black,Res,NewBoard,'C').

addPino(Board, white,'Y', NewBoard,'P') :-
       write('\n2. Black or white pin? (B/W)'),
       read(PinoBW),
       addPinoB(Board,white,PinoBW,FinalBoard,'P'),
       addPinoW(FinalBoard,white,PinoBW,NewBoard,'P').

addPino(Board, white,'Y', NewBoard,'C') :-
       write('\n2. Black or white pin? (B/W)'),
       getRandomBW(['B','W'],Res),
       addPinoB(Board,white,Res,FinalBoard,'C'),
       addPinoW(FinalBoard,white,Res,NewBoard,'C').

addPino(Board,black,'N',NewBoard,'P'):-
        mexer(Board,black,NewBoard,'P').

addPino(Board,black,'N',NewBoard,'C'):-
        mexer(Board,black,NewBoard,'C').

addPino(Board,white,'N',NewBoard,'P'):-
        mexer(Board,white,NewBoard,'P').

addPino(Board,white,'N',NewBoard,'C'):-
        mexer(Board,white,NewBoard,'C').

conditioneqP(A,A,'black',Board,NewBoard, _,_,_,_,_,_,_,_,_):-!,mexer(Board,black,NewBoard,'C').
conditioneqP(A,A,'white',Board,NewBoard, _,_,_,_,_,_,_,_,_):-!,mexer(Board,white,NewBoard,'C').
conditioneqP(_, _,'black',Board, NewBoard, Column, Column2, Row, Row2, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value) :-
        
        Difcolumn2 is Column2 - Column,
        Difrow2 is Row2 - Row,

        Difcolumn is abs(Difcolumn2),
        Difrow is abs(Difrow2),

        indexOf(Value, Value_Pino_Row, 1),  
        indexOf(Value, Value_Pino_Column, 2),
        checkrow(Value_Pino_Column, Difrow,white, Board, NewBoard),
        checkcol(Value_Pino_Row, Difcolumn, white,Board, NewBoard, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value).

conditioneqP(_, _,'white',Board, NewBoard, Column, Column2, Row, Row2, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value) :-
        
        Difcolumn2 is Column2 - Column,
        Difrow2 is Row2 - Row,

        Difcolumn is abs(Difcolumn2),
        Difrow is abs(Difrow2),

        indexOf(Value, Value_Pino_Row, 1),  
        indexOf(Value, Value_Pino_Column, 2),
        checkrow(Value_Pino_Column, Difrow,white, Board, NewBoard),
        checkcol(Value_Pino_Row, Difcolumn, white,Board, NewBoard, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value).

conditioneq(A, A, 'black',Board, NewBoard, _, _, _, _, _, _, _, _, _):- !, write('ERROR: You already have a peace here.'),nl, mexer( Board, black,NewBoard,'P').
conditioneq(A, A, 'white',Board, NewBoard, _, _, _, _, _, _, _, _, _):- !, write('ERROR: You already have a peace here.'),nl, mexer( Board, white,NewBoard,'P').


conditioneq(_, _,'black',Board, NewBoard, Column, Column2, Row, Row2, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value) :-
        
        Difcolumn2 is Column2 - Column,
        Difrow2 is Row2 - Row,

        Difcolumn is abs(Difcolumn2),
        Difrow is abs(Difrow2),

        indexOf(Value, Value_Pino_Row, 1),  
        indexOf(Value, Value_Pino_Column, 2),
        checkrow(Value_Pino_Column, Difrow,white, Board, NewBoard),
        checkcol(Value_Pino_Row, Difcolumn, white,Board, NewBoard, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value).

conditioneq(_, _,'white',Board, NewBoard, Column, Column2, Row, Row2, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value) :-
        
        Difcolumn2 is Column2 - Column,
        Difrow2 is Row2 - Row,

        Difcolumn is abs(Difcolumn2),
        Difrow is abs(Difrow2),

        indexOf(Value, Value_Pino_Row, 1),  
        indexOf(Value, Value_Pino_Column, 2),
        checkrow(Value_Pino_Column, Difrow,white, Board, NewBoard),
        checkcol(Value_Pino_Row, Difcolumn, white,Board, NewBoard, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value).
 

checkempty('empty',black, Board, NewBoard, _, _, _, _):- !, write('ERROR: this block is empty'),nl, mexer( Board, black,NewBoard,'P'). 
checkempty('empty',white, Board, NewBoard, _, _, _, _):- !, write('ERROR: this block is empty'),nl, mexer( Board, white,NewBoard,'P').                         

checkempty(_, black,Board, NewBoard, Column, Row, RowIndex, ColumnIndex):-
        write('\n3.Choose your cell to go.\n'),
        write('  >Row     '),
        read(RowLetter2),
        validateRow(RowLetter2, Row2),
        write('  > Column '),
        read(ColumnLetter2),
        validateColumn(ColumnLetter2,Column2),
        write('\n'),
        ColumnIndex2 is Column2-1,
        RowIndex2 is Row2-1,

        write(Value),
        %write('ERROR: No piece in that position.\n\n').

        getValueFromMatrix(Board,RowIndex,ColumnIndex,Value),
        indexOf(Value, Peace_color1, 0),
        
        getValueFromMatrix(Board,RowIndex2,ColumnIndex2,Value2),
        indexOf(Value2, Peace_color2, 0),
        conditioneq(Peace_color1, Peace_color2, black,Board, NewBoard, Column, Column2, Row, Row2, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value).

checkempty(_, white,Board, NewBoard, Column, Row, RowIndex, ColumnIndex):-
        write('\n3.Choose your cell to go.\n'),
        write('  >Row     '),
        read(RowLetter2),
        validateRow(RowLetter2, Row2),
        write('  > Column '),
        read(ColumnLetter2),
        validateColumn(ColumnLetter2,Column2),
        write('\n'),
        ColumnIndex2 is Column2-1,
        RowIndex2 is Row2-1,

        write(Value),
        %write('ERROR: No piece in that position.\n\n').

        getValueFromMatrix(Board,RowIndex,ColumnIndex,Value),
        indexOf(Value, Peace_color1, 0),
        
        getValueFromMatrix(Board,RowIndex2,ColumnIndex2,Value2),
        indexOf(Value2, Peace_color2, 0),
        conditioneq(Peace_color1, Peace_color2, white,Board, NewBoard, Column, Column2, Row, Row2, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value).

checkrow(A, B,black, Board, NewBoard) :- A < B , !, write('ERROR: You dont have enough white pins'),nl, mexer( Board,black, NewBoard,'P').
checkrow(A, B,white,Board, NewBoard) :- A < B , !, write('ERROR: You dont have enough white pins'),nl, mexer( Board,white, NewBoard,'P').
checkrow(_, _,_, _, _).

checkcol(A, B,white, Board, NewBoard, _, _, _, _, _) :- A < B , !, write('ERROR: You dont have enough blacks pins'),nl, mexer( Board, white,NewBoard,'P').
checkcol(A, B,black, Board, NewBoard, _, _, _, _, _) :- A < B , !, write('ERROR: You dont have enough blacks pins'),nl, mexer( Board, black,NewBoard,'P').
checkcol(_, _,_, Board, NewBoard, RowIndex, ColumnIndex, RowIndex2, ColumnIndex2, Value) :-
        replaceInMatrix(Board,RowIndex2,ColumnIndex2,Value,FinalBoard),
        replaceInMatrix(FinalBoard, RowIndex,ColumnIndex,[empty],NewBoard).


move_piece_white(A, A, Board, NewBoard, _, _, _, _):- !, write('you are not that pieces'), mexer(Board, white, NewBoard, 'P').
move_piece_white(Peace_color1, _,Board, NewBoard, Column, Row, RowIndex, ColumnIndex):-
        checkempty(Peace_color1,white, Board, NewBoard, Column, Row, RowIndex, ColumnIndex).

move_piece_black(A, A, Board, NewBoard, _, _, _, _):- !, write('you are not that pieces'), mexer(Board, black, NewBoard, 'P').
move_piece_black(Peace_color1, _,Board, NewBoard, Column, Row, RowIndex, ColumnIndex):-
        checkempty(Peace_color1,black, Board, NewBoard, Column, Row, RowIndex, ColumnIndex).

mexer(Board,white,NewBoard,'P'):-
        write('\n2.Choose your cell to move the piece.\n'),
        write('  >Row     '),
        read(RowLetter),
        validateRow(RowLetter,Row),
        write('  > Column '),
        read(ColumnLetter),
        validateColumn(ColumnLetter,Column),
        write('\n'),
        ColumnIndex is Column-1,
        RowIndex is Row-1,
        getValueFromMatrix(Board,RowIndex,ColumnIndex,Value),
        indexOf(Value, Peace_color1, 0),
        move_piece_white(Peace_color1, black, Board, NewBoard, Column, Row, RowIndex, ColumnIndex).
        
mexer(Board,black,NewBoard,'P'):-
        write('\n2.Choose your cell to move the piece.\n'),
        write('  >Row     '),
        read(RowLetter),
        validateRow(RowLetter,Row),
        write('  > Column '),
        read(ColumnLetter),
        validateColumn(ColumnLetter,Column),
        write('\n'),
        ColumnIndex is Column-1,
        RowIndex is Row-1,
        getValueFromMatrix(Board,RowIndex,ColumnIndex,Value),
        indexOf(Value, Peace_color1, 0),
         move_piece_black(Peace_color1, white, Board, NewBoard, Column, Row, RowIndex, ColumnIndex).
       
mexer(Board,black,NewBoard,'C'):-
        getCoordinatesBlackMatrix(Board,0,0,Res),
        indexOf(Res,X,0),
        indexOf(Res,Y,1),
        getValueFromMatrix(Board,X,Y,V),
        indexOf(V, Value_Pino_Row, 1),  
        indexOf(V, Value_Pino_Column, 2),
        VP1 is Value_Pino_Row+1,
        VC1 is Value_Pino_Column+1,
        ((VP1==1,VC1==1),!,addPino(Board,black,'Y',NewBoard,'C');
        random(0,VP1,Xf), %valor do pino random
        random(0,VC1,Yf), %valor do pino random
        random(0,2,Rs),
        random(0,2,Cs),
        (Cs==0,!,ColumnIndex2 is Y-Yf;ColumnIndex2 is Y+Yf),
        (Rs==0,!,RowIndex2 is X-Xf;RowIndex2 is X+Xf),
        replaceInMatrix(Board,RowIndex2,ColumnIndex2,V,FinalBoard),
        replaceInMatrix(FinalBoard, X,Y,[empty],NewBoard)).

mexer(Board,white,NewBoard,'C'):-
        getCoordinatesWhiteMatrix(Board,0,0,Res),
        indexOf(Res,X,0),
        indexOf(Res,Y,1),
        getValueFromMatrix(Board,X,Y,V),
        indexOf(V, Value_Pino_Row, 1),  
        indexOf(V, Value_Pino_Column, 2),
        VP1 is Value_Pino_Row+1,
        VC1 is Value_Pino_Column+1,
        ((VP1==1,VC1==1),!,addPino(Board,white,'Y',NewBoard,'C');
        random(0,VP1,Xf), %valor do pino random
        random(0,VC1,Yf), %valor do pino random
        random(0,2,Rs),
        random(0,2,Cs),
        (Cs==0,!,ColumnIndex2 is Y-Yf;ColumnIndex2 is Y+Yf),
        (Rs==0,!,RowIndex2 is X-Xf;RowIndex2 is X+Xf),
        replaceInMatrix(Board,RowIndex2,ColumnIndex2,V,FinalBoard),
        replaceInMatrix(FinalBoard, X,Y,[empty],NewBoard)).

gameLoopPC(Board1):-
        whitePlayerTurn(Board1,Board2,'C'),
        blackPlayerTurn(Board2,Board3,'P'),
        brancoVencedor(Board3),
        pretoVencedor(Board3),
        gameLoopPC(Board3).

gameLoopCC(Board1):-
        whitePlayerTurn(Board1,Board2,'C'),
        blackPlayerTurn(Board2,Board3,'C'),
        brancoVencedor(Board3),
        pretoVencedor(Board3),
        %printBoard(Board3),
        gameLoopCC(Board3).
        
gameLoop(Board1):-
        whitePlayerTurn(Board1,Board2,'P'),
        blackPlayerTurn(Board2,Board3,'P'),
        brancoVencedor(Board3),
        pretoVencedor(Board3),
        gameLoop(Board3).

whitePlayerTurn(Board,NewBoard,'P'):-
         write('\n------------------ WHITE -------------------\n\n'),
         write('1. Do you want to add a pin? (Y/N)'),
         read(RespBoolX),
         addPino(Board,white,RespBoolX,NewBoard,'P'),
         printBoard(NewBoard).

whitePlayerTurn(Board,NewBoard,'C'):-
         write('\n------------------ WHITE -------------------\n\n'),
         write('1. Do you want to add a pin? (Y/N)'),
         %read(RespBoolX),
         getRandomYN(['Y','N'],Res),
         addPino(Board,white,Res,NewBoard,'C'),
         printBoard(NewBoard).

blackPlayerTurn(Board,NewBoard,'P'):-
        write('\n------------------ BLACK -------------------\n\n'),
        write('1. Do you want to add a pin? (Y/N)'),
        read(RespBoolO),
        addPino(Board,black,RespBoolO,NewBoard,'P'),
        printBoard(NewBoard).

blackPlayerTurn(Board,NewBoard,'C'):-
        write('\n------------------ BLACK -------------------\n\n'),
        write('1. Do you want to add a pin? (Y/N)'),
        %read(RespBoolO),
        getRandomYN(['Y','N'],Res),
        addPino(Board,black,Res,NewBoard,'C'),
        printBoard(NewBoard).
                 
stltsPP:-
        initialBoard(InitialBoard),
        printBoard(InitialBoard),
        gameLoop(InitialBoard).        

stltsCC:-
        initialBoard(InitialBoard),
        printBoard(InitialBoard),
        gameLoopCC(InitialBoard).

stltsPC:-
        initialBoard(InitialBoard),
        printBoard(InitialBoard),
        gameLoopPC(InitialBoard).
   
indexOf([Element|_], Element, 0). % We found the element
indexOf([_|Tail], Element, Index):-
  indexOf(Tail, Element, Index1), % Check in the tail of the list
  Index is Index1+1.     
%indexOf(_,empty,0).
                    
get_row(Board, Y, Row):-
    nth0(Y, Board, Row).

count([],_,0).
count([X|T],X,Y):- count(T,X,Z), Y is 1+Z.
count([X1|T],X,Z):- X1\=X,count(T,X,Z).        
        

isElemPresent(X,[X|_]).


% The element belongs to the list if it is 
% in the head (another array) of the list.

isElemPresent(X,[A|_]):- isElemPresent(X,A).


% The element belongs to the list if it is
% in the queue of the list.

isElemPresent(X,[_|R]):- isElemPresent(X,R).        

blackPresent(Board,Res):-
        (isElemPresent([black,0,0],Board),Res is 1);
        (isElemPresent([black,1,0],Board),Res is 1);
        (isElemPresent([black,2,0],Board),Res is 1);
        (isElemPresent([black,3,0],Board),Res is 1);
        (isElemPresent([black,4,0],Board),Res is 1);
        (isElemPresent([black,5,0],Board),Res is 1);
        (isElemPresent([black,6,0],Board),Res is 1);
        (isElemPresent([black,0,1],Board),Res is 1);
        (isElemPresent([black,1,1],Board),Res is 1);
        (isElemPresent([black,2,1],Board),Res is 1);
        (isElemPresent([black,3,1],Board),Res is 1);
        (isElemPresent([black,4,1],Board),Res is 1);
        (isElemPresent([black,5,1],Board),Res is 1);
        (isElemPresent([black,6,1],Board),Res is 1);
        (isElemPresent([black,0,2],Board),Res is 1);
        (isElemPresent([black,1,2],Board),Res is 1);
        (isElemPresent([black,2,2],Board),Res is 1);
        (isElemPresent([black,3,2],Board),Res is 1);
        (isElemPresent([black,4,2],Board),Res is 1);
        (isElemPresent([black,5,2],Board),Res is 1);
        (isElemPresent([black,6,2],Board),Res is 1);
        (isElemPresent([black,0,3],Board),Res is 1);
        (isElemPresent([black,1,3],Board),Res is 1);
        (isElemPresent([black,2,3],Board),Res is 1);
        (isElemPresent([black,3,3],Board),Res is 1);
        (isElemPresent([black,4,3],Board),Res is 1);
        (isElemPresent([black,5,3],Board),Res is 1);
        (isElemPresent([black,6,3],Board),Res is 1);
        (isElemPresent([black,0,4],Board),Res is 1);
        (isElemPresent([black,1,4],Board),Res is 1);
        (isElemPresent([black,2,4],Board),Res is 1);
        (isElemPresent([black,3,4],Board),Res is 1);
        (isElemPresent([black,4,4],Board),Res is 1);
        (isElemPresent([black,5,4],Board),Res is 1);
        (isElemPresent([black,6,4],Board),Res is 1);
        (isElemPresent([black,0,5],Board),Res is 1);
        (isElemPresent([black,1,5],Board),Res is 1);
        (isElemPresent([black,2,5],Board),Res is 1);
        (isElemPresent([black,3,5],Board),Res is 1);
        (isElemPresent([black,4,5],Board),Res is 1);
        (isElemPresent([black,5,5],Board),Res is 1);
        (isElemPresent([black,6,5],Board),Res is 1);
        (isElemPresent([black,0,6],Board),Res is 1);
        (isElemPresent([black,1,6],Board),Res is 1);
        (isElemPresent([black,2,6],Board),Res is 1);
        (isElemPresent([black,3,6],Board),Res is 1);
        (isElemPresent([black,4,6],Board),Res is 1);
        (isElemPresent([black,5,6],Board),Res is 1);
        (isElemPresent([black,6,6],Board),Res is 1);
        Res is 0.

whitePresent(Board,Res):-
        (isElemPresent([white,0,0],Board),Res is 1);
        (isElemPresent([white,1,0],Board),Res is 1);
        (isElemPresent([white,2,0],Board),Res is 1);
        (isElemPresent([white,3,0],Board),Res is 1);
        (isElemPresent([white,4,0],Board),Res is 1);
        (isElemPresent([white,5,0],Board),Res is 1);
        (isElemPresent([white,6,0],Board),Res is 1);
        (isElemPresent([white,0,1],Board),Res is 1);
        (isElemPresent([white,1,1],Board),Res is 1);
        (isElemPresent([white,2,1],Board),Res is 1);
        (isElemPresent([white,3,1],Board),Res is 1);
        (isElemPresent([white,4,1],Board),Res is 1);
        (isElemPresent([white,5,1],Board),Res is 1);
        (isElemPresent([white,6,1],Board),Res is 1);
        (isElemPresent([white,0,2],Board),Res is 1);
        (isElemPresent([white,1,2],Board),Res is 1);
        (isElemPresent([white,2,2],Board),Res is 1);
        (isElemPresent([white,3,2],Board),Res is 1);
        (isElemPresent([white,4,2],Board),Res is 1);
        (isElemPresent([white,5,2],Board),Res is 1);
        (isElemPresent([white,6,2],Board),Res is 1);
        (isElemPresent([white,0,3],Board),Res is 1);
        (isElemPresent([white,1,3],Board),Res is 1);
        (isElemPresent([white,2,3],Board),Res is 1);
        (isElemPresent([white,3,3],Board),Res is 1);
        (isElemPresent([white,4,3],Board),Res is 1);
        (isElemPresent([white,5,3],Board),Res is 1);
        (isElemPresent([white,6,3],Board),Res is 1);
        (isElemPresent([white,0,4],Board),Res is 1);
        (isElemPresent([white,1,4],Board),Res is 1);
        (isElemPresent([white,2,4],Board),Res is 1);
        (isElemPresent([white,3,4],Board),Res is 1);
        (isElemPresent([white,4,4],Board),Res is 1);
        (isElemPresent([white,5,4],Board),Res is 1);
        (isElemPresent([white,6,4],Board),Res is 1);
        (isElemPresent([white,0,5],Board),Res is 1);
        (isElemPresent([white,1,5],Board),Res is 1);
        (isElemPresent([white,2,5],Board),Res is 1);
        (isElemPresent([white,3,5],Board),Res is 1);
        (isElemPresent([white,4,5],Board),Res is 1);
        (isElemPresent([white,5,5],Board),Res is 1);
        (isElemPresent([white,6,5],Board),Res is 1);
        (isElemPresent([white,0,6],Board),Res is 1);
        (isElemPresent([white,1,6],Board),Res is 1);
        (isElemPresent([white,2,6],Board),Res is 1);
        (isElemPresent([white,3,6],Board),Res is 1);
        (isElemPresent([white,4,6],Board),Res is 1);
        (isElemPresent([white,5,6],Board),Res is 1);
        (isElemPresent([white,6,6],Board),Res is 1);
        Res is 0.

brancoVencedor(Board):-
        blackPresent(Board,R),
        (R==0->write('Branco Vencedor\n\n');
         write('\n')). 

pretoVencedor(Board):-
        whitePresent(Board,R),
        (R==0->write('Preto Vencedor\n\n');
         write('\n')).               

getRandomYN(List,Res):-
        random(0,2,R),
        indexOf(List,Res,R).

getRandomBW(List,Res):-
        random(0,2,R),
        indexOf(List,Res,R).

getCoordinatesBlackRow([],_,_,[]).
getCoordinatesBlackElement(Black,RowNumber,N,Res):-
        indexOf(Black,R,0),
        (R==black->Res=[RowNumber,N];
         Res=0).

getCoordinatesBlackLine([],_,_,[]).
getCoordinatesBlackLine([H|T],RowNumber,N,Res):-
        N<12,
        getCoordinatesBlackElement(H,RowNumber,N,R),
        N1 is N+1,
        (R==[RowNumber,N]->Res=R;
        getCoordinatesBlackLine(T,RowNumber,N1,Res)).

getCoordinatesBlackMatrix([H|T],RowNumber,N,Res):-
        RowNumber<12,
        getCoordinatesBlackLine(H,RowNumber,0,R),
        N1 is RowNumber+1,
        (R\=[]->Res=R;
         getCoordinatesBlackMatrix(T,N1,N,Res)).

getCoordinatesWhiteRow([],_,_,[]).
getCoordinatesWhiteElement(Black,RowNumber,N,Res):-
        indexOf(Black,R,0),
        (R==white->Res=[RowNumber,N];
         Res=0).

getCoordinatesWhiteLine([],_,_,[]).
getCoordinatesWhiteLine([H|T],RowNumber,N,Res):-
        N<12,
        getCoordinatesWhiteElement(H,RowNumber,N,R),
        N1 is N+1,
        (R==[RowNumber,N]->Res=R;
        getCoordinatesWhiteLine(T,RowNumber,N1,Res)).

getCoordinatesWhiteMatrix([H|T],RowNumber,N,Res):-
        RowNumber<12,
        getCoordinatesWhiteLine(H,RowNumber,0,R),
        N1 is RowNumber+1,
        (R\=[]->Res=R;
         getCoordinatesWhiteMatrix(T,N1,N,Res)).
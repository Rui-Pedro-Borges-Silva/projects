import pygame
import time


def minimax(board, depth, max_player):
    if depth == 0 or board.is_endgame():
        return board.evaluate(), board

    if max_player:
        maxEval = float('-inf')
        best_move = None
        for move in get_all_moves(board, 'p1'):
            evaluation = minimax(move[0], depth-1, False)[0]
            maxEval = max(maxEval, evaluation)
            if maxEval == evaluation:
                best_move = move[1]

        return maxEval, best_move
    else:
        minEval = float('inf')
        best_move = None
        for move in get_all_moves(board, 'p2'):
            evaluation = minimax(move[0], depth-1, True)[0]
            minEval = min(minEval, evaluation)
            if minEval == evaluation:
                best_move = move[1]

        return minEval, best_move


def simulate_move(piece, pos, board):
    board.move(piece, pos)
    board.change_turn()
    return board


def get_all_moves(board, player):
    moves = []

    for piece in board.get_all_pieces(player):
        valid_moves = board.get_moves(piece)
        for pos in valid_moves:
            temp_board = board.copy()
            temp_piece = temp_board.get_piece_from_pos((piece.x, piece.y))
            new_board = simulate_move(temp_piece, pos, temp_board)
            moves.append([new_board, [piece, pos]])

    return moves

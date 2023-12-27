import pygame
import time
from board import Board
from piece import Piece
from minimax.algorithm import minimax
pygame.init()

WINDOW_SIZE = (900, 500)
screen = pygame.display.set_mode(WINDOW_SIZE)

board = Board(WINDOW_SIZE[0], WINDOW_SIZE[1])


def draw(display):
    display.fill('white')
    board.draw(display)
    pygame.display.update()


if __name__ == '__main__':
    running = True
    setup = False
    ghosts = 0

    while not setup:
        mx, my = pygame.mouse.get_pos()
        for event in pygame.event.get():
            # Quit the game if the user presses the close button
            if event.type == pygame.QUIT:
                setup = True
                running = False
            elif event.type == pygame.MOUSEBUTTONDOWN:
                # If the mouse is clicked
                if event.button == 1:
                    ghosts = board.handle_click_place(mx, my, ghosts)
        if ghosts == 18:
            setup = True
        # Draw the board
        draw(screen)

    while running:
        if board.turn == 'p2':
            eval, move = minimax(board, 2, False)
            board.ai_move(move[0], move[1])
        else:
            mx, my = pygame.mouse.get_pos()
            for event in pygame.event.get():
                # Quit the game if the user presses the close button
                if event.type == pygame.QUIT:
                    running = False
                elif event.type == pygame.MOUSEBUTTONDOWN:
                    # If the mouse is clicked
                    if event.button == 1:
                        board.handle_click(mx, my)
        if board.is_endgame() == 'p1':  # If black is in checkmate
            print('Player 1 wins!')
            running = False
        elif board.is_endgame() == 'p2':  # If white is in checkmate
            print('Player 2 wins!')
            running = False
        # Draw the board
        draw(screen)

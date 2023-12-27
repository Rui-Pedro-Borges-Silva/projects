import pygame
import time
from board import Board
from piece import Piece
import random
from minimax.algorithm import minimax




        
class Game:
    def __init__(self, board, screen, mode):
        self.board = board
        self.screen = screen
        self.mode = mode
        self.running = True
        self.setup = False
        self.ghosts = 0
        self.depth=0
        
        self.setup_init(self.mode) 
        self.gameloop(self.mode)     

    
    def draw(self, display):
        display.fill('white')
        self.board.draw(display)
        pygame.display.update()
    
    def setup_init(self, mode):
        if self.mode == "1v1":
            while not self.setup:
                mx, my = pygame.mouse.get_pos()
                for event in pygame.event.get():
                    # Quit the game if the user presses the close button
                    if event.type == pygame.QUIT:
                        self.setup = True
                        self.running = False
                        pygame.quit()
                    elif event.type == pygame.MOUSEBUTTONDOWN:
                        # If the mouse is clicked
                        if (event.button == 1):
                            self.ghosts = self.board.handle_click_place(mx, my, self.ghosts)
                if self.ghosts == 18:
                    self.setup = True
                # Draw the board
                self.draw(self.screen)
        else:
            while not self.setup:
                mx, my = pygame.mouse.get_pos()
                for event in pygame.event.get():
                    # Quit the game if the user presses the close button
                    if event.type == pygame.QUIT:
                        self.setup = True
                        self.running = False
                        pygame.quit()
                    elif event.type == pygame.MOUSEBUTTONDOWN and self.board.turn == 'p1':
                        print("entreiiiii certoooooo")
                        # If the mouse is clicked
                        self.ghosts = self.board.handle_click_place(mx, my, self.ghosts)
                        
                    elif self.board.turn == 'p2':
                        print("turn =  ", self.board.turn)
                        print("gostsssssssssssss =  ", self.ghosts)
                        empty_squares = self.board.get_empty_squares()
                        random_index = random.randint(0, len(empty_squares)-1)
                        random_sq = empty_squares[random_index]
                        print("x ==== ",random_sq.x,"y ====== ",random_sq.y)
                        self.ghosts = self.board.handle_click_place(random_sq.x*self.board.tile_width, 
                                                                    random_sq.y*self.board.tile_height, 
                                                                    self.ghosts)
                        
                            
                if self.ghosts == 18:
                    self.setup = True
                # Draw the board
                self.draw(self.screen)
            
        
    def gameloop(self, mode):
        if mode == '1v1':
            while self.running:
                mx, my = pygame.mouse.get_pos()
                for event in pygame.event.get():
                    # Quit the game if the user presses the close button
                    if event.type == pygame.QUIT:
                        self.running = False
                        pygame.quit()
                    elif event.type == pygame.MOUSEBUTTONDOWN:
                        # If the mouse is clicked
                        if event.button == 1:
                            self.board.handle_click(mx, my)
                if self.board.is_endgame() == 'p1':  # If black is in checkmate
                    print('Player 1 wins!')
                    self.running = False
                elif self.board.is_endgame() == 'p2':  # If white is in checkmate
                    print('Player 2 wins!')
                    self.running = False
                # Draw the board
                self.draw(self.screen)
        elif mode == "ai_easy":
            self.depth = 2
        elif mode == "ai_medium":
            self.depth = 3
        elif mode == "ai_hard":
            self.depth = 5
        while self.running:
            if self.board.turn == 'p2':
                eval, move = minimax(self.board, self.depth, False)
                self.board.ai_move(move[0], move[1])
            else:
                mx, my = pygame.mouse.get_pos()
                for event in pygame.event.get():
                    # Quit the game if the user presses the close button
                    if event.type == pygame.QUIT:
                        self.running = False
                        pygame.quit()
                    elif event.type == pygame.MOUSEBUTTONDOWN:
                        # If the mouse is clicked
                        if event.button == 1:
                            self.board.handle_click(mx, my)
            if self.board.is_endgame() == 'p1':  # If black is in checkmate
                print('Player 1 wins!')
                self.running = False
            elif self.board.is_endgame() == 'p2':  # If white is in checkmate
                print('Player 2 wins!')
                self.running = False
            # Draw the board
            self.draw(self.screen)
                
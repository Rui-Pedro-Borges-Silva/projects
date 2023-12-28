import pygame
from square import Square
from portal import Portal
from piece import Piece
from dungeon import Dungeon
import copy


class Board:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.tile_width = width // 9
        self.tile_height = height // 5
        self.selected_piece = None
        self.turn = 'p1'
        self.turns = ['p2', 'p2', 'p1', 'p2',
                      'p1', 'p2', 'p1', 'p2',
                      'p1', 'p2', 'p1', 'p2',
                      'p1', 'p2', 'p1', 'p2',
                      'p1', 'p1']
        # p1red p1yellow p1blue p2red p2yellow p2blue
        self.ghosts = [0, 0, 0, 0, 0, 0]

        self.dungeon = Dungeon(self.tile_width, self.tile_height)
        self.out = []

        self.colors = [
            'blue', 'red', 'red', 'blue', 'red',
            'yellow', 'mirror', 'yellow', 'mirror', 'yellow',
            'red', 'blue', 'red', 'blue', 'yellow',
            'blue', 'mirror', 'yellow', 'mirror', 'red',
            'yellow', 'red', 'blue', 'blue', 'yellow']

        self.squares = self.generate_squares()
        self.place_portals()

    def generate_squares(self):
        output = []
        for y in range(5):
            for x in range(5):
                output.append(
                    Square(x, y, self.tile_width, self.tile_height, self.colors.pop(0)))
        return output

    def get_squares(self):
        return self.squares

    def get_square_from_pos(self, pos):
        for square in self.squares:
            if (square.x, square.y) == (pos[0], pos[1]):
                return square
        for square in self.dungeon.squares:
            if (square.x, square.y) == (pos[0], pos[1]):
                return square

    def get_piece_from_pos(self, pos):
        return self.get_square_from_pos(pos).occupying_piece

    def place_portals(self):
        square = self.get_square_from_pos((2, 0))
        square.occupying_piece = Portal(
            (2, 0), square.color,  self.tile_width, self.tile_height)
        square = self.get_square_from_pos((2, 4))
        square.occupying_piece = Portal(
            (2, 4), square.color,  self.tile_width, self.tile_height)
        square = self.get_square_from_pos((4, 2))
        square.occupying_piece = Portal(
            (4, 2), square.color,  self.tile_width, self.tile_height)

    def handle_click_place(self, mx, my, ghosts):
        x = mx // self.tile_width
        y = my // self.tile_height
        index = 0
        clicked_square = self.get_square_from_pos((x, y))
        if clicked_square.color != 'mirror' and clicked_square.color != 'gray':
            if clicked_square.occupying_piece == None:
                if self.turn == 'p2':
                    index += 3

                # if clicked_square.color == 'red':
                    # index += 0
                if clicked_square.color == 'yellow':
                    index += 1
                elif clicked_square.color == 'blue':
                    index += 2

                if self.ghosts[index] < 3:
                    self.ghosts[index] += 1
                    ghosts += 1
                    clicked_square.occupying_piece = Piece(
                        clicked_square.pos, self.turn, clicked_square.color, self.tile_width, self.tile_height)
                    self.turn = self.turns.pop(0)
        return ghosts

    def handle_click(self, mx, my):
        x = mx // self.tile_width
        y = my // self.tile_height
        clicked_square = self.get_square_from_pos((x, y))

        if self.selected_piece == None:
            if clicked_square.occupying_piece != None:
                if clicked_square.occupying_piece.player == self.turn:
                    self.selected_piece = clicked_square.occupying_piece

        elif self.selected_piece.x > 4:
            if self.move(self.selected_piece, clicked_square.pos):
                self.change_turn()

            elif clicked_square.occupying_piece != None:
                if clicked_square.occupying_piece.player == self.turn:
                    self.selected_piece = clicked_square.occupying_piece

        elif self.move(self.selected_piece, clicked_square.pos):
            self.change_turn()

        elif clicked_square.occupying_piece != None:
            if clicked_square.occupying_piece.player == self.turn:
                self.selected_piece = clicked_square.occupying_piece
        return self.turn

    def escape(self):
        self.get_piece_from_pos((2, 0)).escape(self)
        self.get_piece_from_pos((2, 4)).escape(self)
        self.get_piece_from_pos((4, 2)).escape(self)

    def get_empty_color_squares(self, color):
        output = []
        for square in self.squares:
            if square.color == color:
                if square.occupying_piece == None:
                    output.append(square)
        return output
    
    def get_empty_squares(self):
        output = []
        for square in self.squares:
            if square.occupying_piece == None:
                output.append(square)
        return output

    def is_endgame(self):
        output = False

        if set([1, 2, 3]) <= set(self.out):
            output = 'p1'
        elif set([-1, -2, -3]) <= set(self.out):
            output = 'p2'
        return output

    def draw(self, display):
        if self.selected_piece is not None:
            self.get_square_from_pos(self.selected_piece.pos).highlight = True
            for pos in self.get_moves(self.selected_piece):
                self.get_square_from_pos(pos).highlight = True

        for square in self.squares:
            square.draw(display)

        self.dungeon.draw(display)

    def change_turn(self):
        self.turn = 'p1' if self.turn == 'p2' else 'p2'
        self.escape()
        self.dungeon.organize()

# PIECE

    def wins_fight(self, challenger, old_piece):
        if challenger.color == 'red':
            if old_piece.color == 'blue':
                self.get_piece_from_pos((2, 4)).rotate()
                return True
            else:
                self.get_piece_from_pos((2, 0)).rotate()
                return False

        elif challenger.color == 'yellow':
            if old_piece.color == 'red':
                self.get_piece_from_pos((2, 0)).rotate()
                return True
            else:
                self.get_piece_from_pos((4, 2)).rotate()
                return False

        elif challenger.color == 'blue':
            if old_piece.color == 'yellow':
                self.get_piece_from_pos((4, 2)).rotate()
                return True
            else:
                self.get_piece_from_pos((2, 4)).rotate()
                return False

    def move(self, piece, pos):
        if piece != None:
            for square in self.squares:
                square.highlight = False
            self.get_square_from_pos(piece.pos).highlight = False
            self.selected_piece = None

            if pos in self.get_moves(piece):
                new_square = self.get_square_from_pos(pos)
                prev_square = self.get_square_from_pos(piece.pos)
                prev_square.occupying_piece = None
                piece.move(pos)

                if new_square.occupying_piece == None:
                    new_square.occupying_piece = piece

                elif new_square.occupying_piece != None:
                    if self.wins_fight(piece, new_square.occupying_piece):
                        self.dungeon.move(new_square.occupying_piece)
                        new_square.occupying_piece = piece
                    else:
                        self.dungeon.move(piece)
                return True
            return False
        return False

    def get_moves(self, piece):
        output = []
        for pos in piece.get_possible_moves():
            square = self.get_square_from_pos(pos)
            if square.occupying_piece == None:
                output.append(pos)
            else:
                if piece.x < 5 and square.occupying_piece.color != piece.color:
                    output.append(pos)
        return output

# evaluation
    def evaluate(self):
        value = 0

        if self.is_endgame() == 'p1':
            return 1000000
        elif self.is_endgame() == 'p2':
            return -1000000

        out_pieces = [0, 0, 0, 0, 0, 0]
        # Piece out
        for piece in self.out:
            if piece == 1:
                if out_pieces[0] == 0:
                    value += 100
                    out_pieces[0] = 1
                else:
                    value -= 10
            elif piece == 2:
                if out_pieces[1] == 0:
                    value += 100
                    out_pieces[1] = 1
                else:
                    value -= 10
            elif piece == 3:
                if out_pieces[2] == 0:
                    value += 100
                    out_pieces[2] = 1
                else:
                    value -= 10
            elif piece == -1:
                if out_pieces[3] == 0:
                    value -= 100
                    out_pieces[3] = 1
                else:
                    value += 10
            elif piece == -2:
                if out_pieces[4] == 0:
                    value -= 100
                    out_pieces[4] = 1
                else:
                    value += 10
            elif piece == -3:
                if out_pieces[5] == 0:
                    value -= 100
                    out_pieces[5] = 1
                else:
                    value += 10
        for square in self.squares:
            if square.occupying_piece != None:
                piece = square.occupying_piece
                if piece.player == 'p1':
                    # Move no value
                    value += len(self.get_moves(piece)) * 2
                    # Near portal value
                    if (square.pos in [(1, 0), (3, 0), (2, 1)] and piece.color == 'red') or \
                        (square.pos in [(1, 4), (3, 4), (2, 3)] and piece.color == 'blue') or \
                            (square.pos in [(4, 1), (3, 2), (4, 3)] and piece.color == 'yellow'):
                        value += 15
                elif piece.player == 'p2':
                    # Move no value
                    value -= len(self.get_moves(piece)) * 2
                    # Near portal value
                    if (square.pos in [(1, 0), (3, 0), (2, 1)] and piece.color == 'red') or \
                        (square.pos in [(1, 4), (3, 4), (2, 3)] and piece.color == 'blue') or \
                            (square.pos in [(4, 1), (3, 2), (4, 3)] and piece.color == 'yellow'):
                        value -= 15
        # Piece in dungeon
        for square in self.dungeon.squares:
            if square.occupying_piece != None:
                piece = square.occupying_piece
                if piece.player == 'p1':
                    value -= 8
                else:
                    value += 8
        return value

    def get_all_pieces(self, player):
        pieces = []
        for square in self.squares:
            if square.occupying_piece != None:
                if square.occupying_piece.player == player:
                    pieces.append(square.occupying_piece)
        for square in self.dungeon.squares:
            if square.occupying_piece != None:
                if square.occupying_piece.player == player:
                    pieces.append(square.occupying_piece)
            else:
                break
        return pieces

    def ai_move(self, piece, pos):
        self.move(piece, pos)
        self.change_turn()

    def copy(self):
        copyobj = Board(self.width, self.height)
        copyobj.dungeon = self.dungeon.copy()
        copyobj.out = copy.deepcopy(self.out)
        copy_squares = []
        for square in self.squares:
            copy_squares.append(square.copy())
        # print("copy squares: ", copy_squares)
        # print("copy dungeon: ", copyobj.dungeon)
        # print("copy out: ", copyobj.out)
        copyobj.turn = self.turn
        return copyobj

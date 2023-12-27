from square import Square
from copy import copy



class Dungeon:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.squares = self.generate_squares(width, height)

    def generate_squares(self, width, height):
        output = []
        for y in range(5):
            for x in range(4):
                output.append(
                    Square(x+5, y, width, height, 'gray'))
        return output

    def draw(self, display):
        for square in self.squares:
            square.draw(display)

    def move(self, piece):
        for square in self.squares:
            if square.occupying_piece == None:
                piece.move(square.pos)
                square.occupying_piece = piece
                return

    def organize(self):
        for square in self.squares:
            if square.occupying_piece != None:
                piece = square.occupying_piece
                square.occupying_piece = None
                self.move(piece)

    def copy(self):
        copy = Dungeon(self.width, self.height)
        for square in self.squares:
            if square.occupying_piece != None:
                copy.move(square.occupying_piece)
            else:
                break
        return copy

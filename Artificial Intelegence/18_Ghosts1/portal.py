import pygame
from copy import copy


class Portal:
    def __init__(self, pos, color, width, height):
        self.pos = pos
        self.x = pos[0]
        self.y = pos[1]
        self.color = color
        self.player = None
        self.width = width
        self.height = height

        img_path = 'img/' + color + '.png'
        self.img = pygame.image.load(img_path)
        self.img = pygame.transform.scale(
            self.img, (self.width, self.height))

        if color == 'red':
            self.dir = 'n'
        elif color == 'yellow':
            self.dir = 'e'
        else:
            self.dir = 's'

    def escape(self, board):
        square = None
        if self.dir == 'n' and self.y != 0:
            square = board.get_square_from_pos((self.x, self.y-1))
        elif self.dir == 'e' and self.x != 4:
            square = board.get_square_from_pos((self.x+1, self.y))
        elif self.dir == 's' and self.y != 4:
            square = board.get_square_from_pos((self.x, self.y+1))
        elif self.dir == 'w':
            square = board.get_square_from_pos((self.x-1, self.y))

        if square is not None:
            if square.occupying_piece != None:
                if square.occupying_piece.color == self.color:
                    ghost = 0
                    if self.color == 'yellow':
                        ghost += 1
                    elif self.color == 'red':
                        ghost += 2
                    else:
                        ghost += 3
                    if square.occupying_piece.player == 'p2':
                        ghost *= -1
                    board.out.append(ghost)
                    square.occupying_piece = None
                    return True
        return False

    def rotate(self):
        if self.dir == 'n':
            self.dir = 'e'
        elif self.dir == 'e':
            self.dir = 's'
        elif self.dir == 's':
            self.dir = 'w'
        elif self.dir == 'w':
            self.dir = 'n'

        self.img = pygame.transform.rotate(self.img, 270)

    def copy(self):
        copy = Portal(self.pos, self.color, self.width, self.height)
        copy.dir = self.dir
        return copy

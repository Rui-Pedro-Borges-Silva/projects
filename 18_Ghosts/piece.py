import pygame
from copy import copy


class Piece:
    def __init__(self, pos, player, color, width, height):
        self.pos = pos
        self.x = pos[0]
        self.y = pos[1]
        self.player = player
        self.color = color
        self.width = width
        self.height = height

        img_path = 'img/' + color + '_' + self.player + '.png'
        self.img = pygame.image.load(img_path)
        self.img = pygame.transform.scale(
            self.img, (self.width - 20, self.height - 20))

    def get_pos(self):
        return self.pos

    def move(self, pos):
        self.pos, self.x, self.y = pos, pos[0], pos[1]

    def get_possible_moves(self):
        output = []

        if self.x > 4:
            if self.color == 'blue':
                output += [(0, 0), (3, 0), (1, 2), (3, 2), (0, 3), (3, 4)]
            elif self.color == 'yellow':
                output += [(0, 1), (2, 1), (4, 1), (2, 3), (0, 4), (4, 4)]
            elif self.color == 'red':
                output += [(1, 0), (4, 0), (0, 2), (2, 2), (4, 3), (1, 4)]
        else:
            moves = [
                (0, -1),  # n
                (1, 0),  # e
                (0, 1),  # s
                (-1, 0),  # w
            ]
            for move in moves:
                new_pos = (self.x + move[0], self.y + move[1])
                if (
                    # out of bounds
                    new_pos[0] < 5
                    and new_pos[0] >= 0
                    and new_pos[1] < 5
                    and new_pos[1] >= 0
                    # portals
                    and not new_pos == (2, 0)
                    and not new_pos == (4, 2)
                    and not new_pos == (2, 4)
                ):
                    output.append(new_pos)
        # mirrors
            if self.pos == (1, 1):
                output += [(1, 3), (3, 1), (3, 3)]
            elif self.pos == (3, 1):
                output += [(1, 1), (1, 3), (3, 3)]
            elif self.pos == (1, 3):
                output += [(1, 1), (3, 1), (3, 3)]
            elif self.pos == (3, 3):
                output += [(1, 1), (1, 3), (3, 1)]

        return output

    def copy(self):
        copy = Piece(self.pos, self.player, self.color,
                     self.width, self.height)
        return copy

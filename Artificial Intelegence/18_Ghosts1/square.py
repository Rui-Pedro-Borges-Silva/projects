import pygame
from copy import copy


class Square:
    def __init__(self, x, y, width, height, color):
        self.x = x
        self.y = y
        self.pos = (x, y)
        self.width = width
        self.height = height
        self.color = color
        self.type = type

        self.abs_x = x * width
        self.abs_y = y * height
        self.abs_pos = (self.abs_x, self.abs_y)

        if self.color == 'red':
            self.draw_color = (220, 20, 60)
        elif self.color == 'yellow':
            self.draw_color = (255, 255, 200)
        elif self.color == 'blue':
            self.draw_color = (65, 105, 225)
        elif self.color == 'gray':
            self.draw_color = (150, 150, 150)
        else:  # mirror
            img_path = 'img/' + color + '.png'
            self.img = pygame.image.load(img_path)
            self.img = pygame.transform.scale(
                self.img, (width, height))

        if self.color == 'red':
            self.highlight_color = (140, 5, 30)
        elif self.color == 'yellow':
            self.highlight_color = (200, 180, 130)
        elif self.color == 'blue':
            self.highlight_color = (20, 45, 160)
        elif self.color == 'gray':
            self.highlight_color = (100, 100, 100)
        else:
            dark_img_path = 'img/' + color + '.png'
            self.dark_img = pygame.image.load(dark_img_path)
            self.dark_img = pygame.transform.scale(
                self.dark_img, (width, height))
            dark = pygame.Surface(
                (self.dark_img.get_width(), self.dark_img.get_height()), flags=pygame.SRCALPHA)
            dark.fill((80, 80, 80, 0))
            self.dark_img.blit(
                dark, (0, 0), special_flags=pygame.BLEND_RGBA_SUB)

        self.occupying_piece = None
        self.highlight = False

        self.rect = pygame.Rect(
            self.abs_x,
            self.abs_y,
            self.width,
            self.height
        )

    def draw(self, display):
        if self.color == 'mirror':
            if self.highlight:
                centering_rect = self.dark_img.get_rect()
                centering_rect.center = self.rect.center
                display.blit(self.dark_img, centering_rect.topleft)
            else:
                centering_rect = self.img.get_rect()
                centering_rect.center = self.rect.center
                display.blit(self.img, centering_rect.topleft)
        elif self.highlight:
            pygame.draw.rect(display, self.highlight_color, self.rect)
        else:
            pygame.draw.rect(display, self.draw_color, self.rect)

        if self.occupying_piece != None:
            centering_rect = self.occupying_piece.img.get_rect()
            centering_rect.center = self.rect.center
            display.blit(self.occupying_piece.img, centering_rect.topleft)

    def copy(self):
        copy = Square(self.x, self.y, self.width, self.height, self.color)
        if self.occupying_piece != None:
            copy.occupying_piece = self.occupying_piece.copy()
        return copy

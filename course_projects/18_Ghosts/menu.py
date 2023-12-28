# python3 menu.py
import pygame
import sys
import game
from board import Board
from button import Button

WINDOW_SIZE = (900, 500)

def get_font(size):  # Returns Press-Start-2P in the desired size
    return pygame.font.Font("img/font.ttf", size)


class Menu:
    def __init__(self):
        pygame.init()
        self.screen = pygame.display.set_mode((1000, 800))
        pygame.display.set_caption('18 ghosts')
        self.board = Board(WINDOW_SIZE[0], WINDOW_SIZE[1])

    def play(self):
        while True:
            play_mouse_pos = pygame.mouse.get_pos()

            self.screen.fill("black")

            pvp_button = Button(image=pygame.image.load("img/SmallRect.png"), pos=(1000/2, 350),
                                text_input="PVP", font=get_font(75), base_color="#d7fcd4", hovering_color="White")
            pve_button = Button(image=pygame.image.load("img/SmallRect.png"), pos=(1000/2, 500),
                                text_input="PVE", font=get_font(75), base_color="#d7fcd4", hovering_color="White")
            back_button = Button(image=pygame.image.load("img/SmallRect.png"), pos=(1000/2, 650),
                                 text_input="BACK", font=get_font(75), base_color="#d7fcd4", hovering_color="White")

            play_text = get_font(45).render(
                "Choose the game mode", True, "White")
            play_rect = play_text.get_rect(center=(1000/2, 100))
            self.screen.blit(play_text, play_rect)

            for button in [pvp_button, pve_button, back_button]:
                button.changeColor(play_mouse_pos)
                button.update(self.screen)

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if pvp_button.checkForInput(play_mouse_pos):
                        game.Game(self.board, self.screen, "1v1")
                    if pve_button.checkForInput(play_mouse_pos):
                        self.play_ai()
                    if back_button.checkForInput(play_mouse_pos):
                        self.main_menu()

            pygame.display.update()

    def play_ai(self):
        while True:
            play_ai_mouse_pos = pygame.mouse.get_pos()

            self.screen.fill("black")

            easy_button = Button(image=pygame.image.load("img/SmallRect.png"), pos=(1000/2, 250),
                                 text_input="Easy", font=get_font(75), base_color="#d7fcd4", hovering_color="White")
            medium_button = Button(image=pygame.image.load("img/MediumRect.png"), pos=(1000/2, 400),
                                   text_input="Medium", font=get_font(75), base_color="#d7fcd4", hovering_color="White")
            hard_button = Button(image=pygame.image.load("img/SmallRect.png"), pos=(1000/2, 550),
                                 text_input="Hard", font=get_font(75), base_color="#d7fcd4", hovering_color="White")
            back_button = Button(image=pygame.image.load("img/SmallRect.png"), pos=(1000/2, 700),
                                 text_input="BACK", font=get_font(75), base_color="#d7fcd4", hovering_color="White")

            play_text = get_font(45).render(
                "Choose the game mode", True, "White")
            play_rect = play_text.get_rect(center=(1000/2, 100))
            self.screen.blit(play_text, play_rect)

            for button in [easy_button, medium_button, hard_button, back_button]:
                button.changeColor(play_ai_mouse_pos)
                button.update(self.screen)

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if easy_button.checkForInput(play_ai_mouse_pos):
                        game.Game(self.board, self.screen, "ai_easy")
                    if medium_button.checkForInput(play_ai_mouse_pos):
                        game.Game(self.board, self.screen, "ai_medium")
                    if hard_button.checkForInput(play_ai_mouse_pos):
                        game.Game(self.board, self.screen, "ai_hard")
                    if back_button.checkForInput(play_ai_mouse_pos):
                        self.play()

            pygame.display.update()

   

    def main_menu(self):
        bg = pygame.image.load(f"img/ghosts_bg.png")
        bg = pygame.transform.scale(bg, (1200, 900))

        while True:
            self.screen.blit(bg, (0, 0))

            menu_mouse_pos = pygame.mouse.get_pos()

            menu_text = get_font(100).render("18 GHOSTS", True, "#b68f40")
            menu_rect = menu_text.get_rect(center=(1000/2, 170))

            play_button = Button(image=pygame.image.load("img/SmallRect.png"), pos=(1000/2, 350),
                                 text_input="PLAY", font=get_font(75), base_color="#d7fcd4", hovering_color="White")
            quit_button = Button(image=pygame.image.load("img/SmallRect.png"), pos=(1000/2, 550),
                                 text_input="QUIT", font=get_font(75), base_color="#d7fcd4", hovering_color="White")

            self.screen.blit(menu_text, menu_rect)

            for button in [play_button, quit_button]:
                button.changeColor(menu_mouse_pos)
                button.update(self.screen)

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()
                if event.type == pygame.MOUSEBUTTONDOWN:
                    if play_button.checkForInput(menu_mouse_pos):
                        self.play()
                    if quit_button.checkForInput(menu_mouse_pos):
                        pygame.quit()
                        sys.exit()

            pygame.display.update()


main = Menu()
main.main_menu()

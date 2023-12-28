# Project-l10gr07- SNAKE


## Description
O nosso jogo é baseado num jogo chamado "Snake", desenvolvido e publicado por Gremlin em 1976.O objetivo deste jogo consiste em controlar a cobra e ir apanhando as frutas que estão na arena,ficando a cobra maior, sofrendo algumas alterações dependendo do tipo de fruta coletada, e ganhando pontuação a cada fruta coletada.Vai ser tabelado e guardado cada score dos players para poder comparar pontuações e haver mais competividade.

Projeto a ser desenvolvido por Rúben Pereira(up202006195) e Rui Silva (up202005661).

## Features Planeadas:

-Menu: o utilizador vai ser capaz de navegar por diferentes menus(Play,Score,Rules);

![image](https://user-images.githubusercontent.com/93789624/203621989-2a9ead0f-a3e9-4de9-8bef-a412f6fa0cdf.png)

![image](https://user-images.githubusercontent.com/93789624/203622685-954c3e8c-1795-4d73-b75c-93d9d7d3d77e.png)



-Botões: butões funcionais durante o correr do jogo;

-Player Control: movimentação da cobra;

-Score: quando o jogador coleta as frutas o score vai aumentando;

-Music: Musica de fundo;

-Colisões: detetar a colisão com objetos(frutas,cobra,walls);

-GameOver: quando a snake colidir consigo mesma,irá surgir esta interface GameOver 

![image](https://user-images.githubusercontent.com/93789624/203622212-2d661374-c42a-4879-8225-e40d547f5b28.png)



## GAME: 

![image](https://user-images.githubusercontent.com/93789624/203622257-950ede44-f67c-4896-9982-acf3b3a9480c.png)


## Design

Problemas em contexto:

1º - Como estruturar o nosso programa?

2º - Como desenhar componentes de diferentes formas?

3º - Como guardar agrupadamente os elementos do jogo?

4º - Como tratar os inputs dos utilizadores?

## Patterns

Para o primeiro problema pensámos em utilizar o Model-View-Controller(MVC) com o intuito de interligar os models,as views e os controllers.

- [Model](https://github.com/FEUP-LDTS-2022/project-l10gr07/tree/master/src/main/java/Model)
- [View](https://github.com/FEUP-LDTS-2022/project-l10gr07/tree/master/src/main/java/View)
- [Controller](https://github.com/FEUP-LDTS-2022/project-l10gr07/tree/master/src/main/java/Controller)

Para o segundo problema tencionamos usar o Command Pattern já que este permite representar todos os elementos através do uso da mesma função.

Para o terceiro problema tencionamos usar o Composite visto que é um conjunto com partes e todos.

- [Componentes](https://github.com/FEUP-LDTS-2022/project-l10gr07/tree/master/src/main/java/Model/components)

Para o quarto problema tencionamos utilizar o Observer para o programa obter informação das teclas pressionadas e as processar.

- [Controller](https://github.com/FEUP-LDTS-2022/project-l10gr07/tree/master/src/main/java/Controller)














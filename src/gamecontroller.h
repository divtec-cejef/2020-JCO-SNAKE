/**
  \file
  \brief    Déclaration de la classe GameController.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#ifndef GAMECONTROLLER_H
#define GAMECONTROLLER_H

#include <QObject>
#include <QTimer>
#include <QAction>
#include "gamewindow.h"
class QGraphicsScene;
class QKeyEvent;

class Snake;
class Food;
class Wall;

class GameController : public QObject
{
    Q_OBJECT
public:
    GameController(QGraphicsScene &scene, QObject *parent = nullptr);
    ~GameController();

    void snakeAteFood(Food *food);
//    void snakeHitWall(Snake *snake, Wall *wall);
    void snakeAteItself();
public slots:
    void pause();
    void resume();
    void gameOver();

private:
    void addNewFood();
    Snake *snake;
};

#endif // GAMECONTROLLER_H

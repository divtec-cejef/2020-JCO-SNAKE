/**
  \file
  \brief    Déclaration de la classe Snake.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#ifndef SNAKE_H
#define SNAKE_H

#include <QGraphicsItem>
#include <QRectF>

class GameController;

class Snake : public QGraphicsItem
{
public:
    enum Direction {
        NoMove,
        MoveLeft,
        MoveRight,
        MoveUp,
        MoveDown
    };

    Snake(GameController & controller);

private:

    QPointF        head;
    int            growing;
    int            speed;
    QList<QPointF> tail;
    int            tickCounter;
    Direction      moveDirection;
    GameController &controller;
};

#endif // SNAKE_H

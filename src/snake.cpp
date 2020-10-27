/**
  \file
  \brief    Déclaration de la classe Snake.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#include <QPainter>

#include "constants.h"
#include "gamecontroller.h"
#include "snake.h"

static const qreal SNAKE_SIZE = TILE_SIZE;

Snake::Snake(GameController &controller) :
    head(0, 0),
    growing(7),
    speed(5),
    moveDirection(NoMove),
    controller(controller)
{
}

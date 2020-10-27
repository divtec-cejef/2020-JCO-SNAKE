/**
  \file
  \brief    Déclaration de la classe food.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#include <QPainter>

#include "constants.h"
#include "food.h"

static const qreal FOOD_RADIUS = 3.0;

Food::Food(qreal x, qreal y)
{
    setPos(x, y);
    setData(GD_Type, GO_Food);
}


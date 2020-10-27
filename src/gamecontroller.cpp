/**
  \file
  \brief    Déclaration de la classe GameController.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#include <QEvent>
#include <QGraphicsScene>
#include <QKeyEvent>
#include <QMessageBox>
#include <QAction>
#include <iostream>

#include "gamecontroller.h"
#include "food.h"
#include "snake.h"
#include "gamewindow.h"

GameController::GameController(QGraphicsScene &scene, QObject *parent) :
    QObject(parent)
{

    scene.addItem(snake);
    scene.installEventFilter(this);
    //resume();

}

GameController::~GameController()
{
}

void GameController::snakeAteFood(Food *food)
{

}

//void GameController::snakeHitWall(Snake *snake, Wall *wall)
//{
//}

void GameController::snakeAteItself()
{

}

void GameController::addNewFood()
{

}

void GameController::gameOver()
{

}

void GameController::pause()
{

}

void GameController::resume()
{

}

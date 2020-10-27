/**
  \file
  \brief    Déclaration de la classe GameWindow.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#include <QGraphicsView>
#include <QTimer>
#include <qaction.h>
#include <qmenubar.h>
#include <qapplication.h>
#include <qmessagebox.h>

#include "constants.h"
#include "gamecontroller.h"
#include "gamewindow.h"
#include <QIcon>

GameWindow::GameWindow(QWidget *parent)
    : QMainWindow(parent),
      scene(new QGraphicsScene(this)),
      view(new QGraphicsView(scene, this)),
      game(new GameController(*scene, this))
{
    setCentralWidget(view);
    setFixedSize(600, 600);
    initScene();
    initSceneBackground();

}

GameWindow::~GameWindow()
{
    
}

void GameWindow::initScene()
{
    scene->setSceneRect(-100, -100, 200, 200);
}

void GameWindow::initSceneBackground()
{
    QPixmap bg(TILE_SIZE, TILE_SIZE);
    QPainter p(&bg);
    p.setBrush(QBrush(Qt::gray));
    p.drawRect(0, 0, TILE_SIZE, TILE_SIZE);

    view->setBackgroundBrush(QBrush(bg));
}

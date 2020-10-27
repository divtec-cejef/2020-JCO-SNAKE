/**
  \file
  \brief    Déclaration de la classe GameWindow.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#ifndef GAMEWINDOW_H
#define GAMEWINDOW_H

#include <QMainWindow>

class QGraphicsScene;
class QGraphicsView;

class GameController;

class GameWindow : public QMainWindow
{
    Q_OBJECT
public:
    GameWindow(QWidget *parent = nullptr);
    ~GameWindow();

private:
    void initScene();
    void initSceneBackground();


    QGraphicsScene *scene;
    QGraphicsView *view;

    GameController *game;
};

#endif // GAMEWINDOW_H

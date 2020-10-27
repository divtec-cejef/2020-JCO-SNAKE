/**
  \file
  \brief    Déclaration de la classe MenuWindow.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#ifndef MENUWINDOW_H
#define MENUWINDOW_H

#include <QMainWindow>
#include "gamewindow.h"

namespace Ui {
class MenuWindow;
}

class MenuWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MenuWindow(QWidget *parent = nullptr);
    ~MenuWindow();

private slots:
    void on_bt_solo_clicked();

private:
    Ui::MenuWindow *ui;
    GameWindow* gameWindow;

};

#endif // MENUWINDOW_H

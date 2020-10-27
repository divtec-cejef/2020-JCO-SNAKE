/**
  \file
  \brief    Déclaration de la classe MenuWindow.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#include "menuwindow.h"
#include "ui_menuwindow.h"
#include "gamewindow.h"

MenuWindow::MenuWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MenuWindow)
{
    ui->setupUi(this);
}

MenuWindow::~MenuWindow()
{
    delete ui;
}

void MenuWindow::on_bt_solo_clicked()
{
    this->close();

    GameWindow gameWindow;
    gameWindow.show();
}

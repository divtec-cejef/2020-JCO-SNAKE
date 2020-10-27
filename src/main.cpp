/**
  \file
  \brief    Déclaration de la classe main.
  \author   Thomas Amstutz
  \date     Octobre 2020
*/
#include "gamewindow.h"
#include "menuwindow.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    MenuWindow menu;

    menu.show();
    
    return a.exec();
}

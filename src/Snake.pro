#-------------------------------------------------
#
# Project created by QtCreator 2014-01-31T13:38:06
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = snake
TEMPLATE = app


SOURCES += main.cpp\
    food.cpp \
    gamecontroller.cpp \
    gamewindow.cpp \
    menuwindow.cpp \
    snake.cpp

HEADERS  += \
    food.h \
    gamecontroller.h \
    constants.h \
    gamewindow.h \
    menuwindow.h \
    snake.h

FORMS += \
    menuwindow.ui

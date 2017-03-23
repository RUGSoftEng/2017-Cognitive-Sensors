# 2017-Cognitive-Sensors
Team Wander’s aim is to create a cognitive testing game which will use existing testing concepts used by academic researchers. With integrated self-report questionnaires regarding depression symptoms and could therefore be used to provide information about depression symptoms over time in the user. Self-report questionnaires often have problems with bias, but it is hoped that integrating these questionnaires into the cognitive testing could limit this.

Team Wander aims to create an app designed for scientific research on cognitive activity. To do this, the app will contain game(s) that collect data on the user’s tendency to mind-wander. The primary game intended to be used in the app is a game that displays a stream of digits to the user. When a user sees a digit they are supposed to tap unless it is a certain predefined digit, in which case they have to ignore it. This data can hopefully be used to give insight to when people are more active and alert.

## App
The Android application files are located in /Wander. The application was developed and can be built using Android Studio.

## Server
For the server side, a Google Web App is used. This document can be found [here](https://docs.google.com/spreadsheets/d/11B4swCBJJOPQxJuCC7GZ-atJyUtk2wHHVclxLwEtkOI/edit#gid=978512187). To send requests and data, [this URL](https://script.google.com/macros/s/AKfycbxvbf-dg4ZYc-vFpCCygBgsPpcHl7G31kMmouhhbA6pO-2luQk/exec) should be used. The structure follows the ORM design file located in /Server.

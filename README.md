# Tic-Tac-Toe
A Tic-Tac-Toe game client and server written in Java with GUI.

# How to play
## 1. Start the server
### With jar
Download the server.jar from [here](https://github.com/adlsdztony/Tic-Tac-Toe-JavaGUI/releases/latest/download/server.jar)
```bash
java -jar server.jar
```
### With docker
```bash
git clone https://github.com/adlsdztony/Tic-Tac-Toe-JavaGUI.git
cd Tic-Tac-Toe-JavaGUI
docker build .
docker run -d -p 12396:12396 server:v1
```
### With source code
```bash
git clone https://github.com/adlsdztony/Tic-Tac-Toe-JavaGUI.git
cd Tic-Tac-Toe-JavaGUI
javac -d . src/server/*.java src/game/*.java
java server.Server
```
## 2. Start the client
### With jar
Download the client.jar from [here](https://github.com/adlsdztony/Tic-Tac-Toe-JavaGUI/releases/latest/download/client.jar)
```bash
java -jar client.jar
```
### With source code
```bash
git clone https://github.com/adlsdztony/Tic-Tac-Toe-JavaGUI.git
cd Tic-Tac-Toe-JavaGUI
javac -d . src/client/*.java src/game/*.java
java client.Client
```



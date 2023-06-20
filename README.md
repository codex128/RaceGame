# RaceGame
Multiplayer physics-based racing game built with JMonkeyEngine3.

![racing](https://github.com/codex128/RaceGame/blob/master/assets/Textures/RaceGame1.png)

# Controls
**P1**: Arrow keys to drive/steer and left-control.<br>
**P2**: WASD to drive/steer and F to flip<br>
**P3**: IJKL and ;<br>
**P4**: Joystick button 1 and 2 for drive and reverse, tilt to steer, button 11 to flip<br>
*The first group is forward, reverse, steer left/right. The second is for flipping the car in case it flips over.*

# Options
Options are in the code.
* To change the number of racers, find `numPlayers` in `Main.class` (between 1 and 4, inclusive).
* To change the number of laps to win, find `laps` in `Main.class` (greater than 0).
* Car options are in `Driver.class`.

# Dependencies
* [JMonkeyEngine 3.6+](https://github.com/jMonkeyEngine/jmonkeyengine)
* [Minie 4.6+](https://github.com/stephengold/Minie)
* [Lemur 1.16+](https://github.com/jMonkeyEngine-Contributions/Lemur)
* [J3map (latest)](https://github.com/codex128/J3map)
* [JmeUtilityLibrary (latest)](https://github.com/codex128/JmeUtilityLibrary)

# RaceGame
Multiplayer physics-based racing game built with JMonkeyEngine3.

![racing](https://github.com/codex128/RaceGame/blob/master/assets/Textures/RaceGame1.png)

# Controls
**Red**: Arrow keys and left control.<br>
**Blue**: WASD and F<br>
**Green**: IJKL and ;<br>
**Yellow**: Numpad 8 4 5 6 and Numpad Enter (make sure num-lock is on)<br>
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
* [J3map](https://github.com/codex128/J3map)
* [JmeUtilityLibrary](https://github.com/codex128/JmeUtilityLibrary)

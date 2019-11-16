# ProjectileProjectNew

Hi Austin (or whoever else might be viewing this),

This is a Github repository for my projectile motion project. This repository is just a copy of my IntelliJ Idea project folder, so you should be able to open it as a project. I added the dependencies through IntelliJ, not Maven, so the dependencies will not show up in `pom.xml`. I believe the dependency information is in `./.idea/libraries`, and IntelliJ should (probably) install them for you automatically. If for some reason you cannot run the code directly from IntelliJ, I have included a jar file in the `./out` folder that will run just fine.

## Running Instructions

**FYI: There is no Java GUI in this program. You will need to have some sort of terminal view open.** If you are running from IntelliJ, please run `ProjectileLauncher.class`. You can skip the rest of this paragraph, as it has a built-in console view. If you are running from the jar file, open a terminal window and run the jar using `java -jar ProjectileProjectNew.jar`.

Once you start the program, the server will start on port 3000. You can access the page at `http://localhost:3000`. You can then use the web interface to interact with the program.

## Acknowledgements

**If you're not Austin, feel free to skip this section.**

Whenever I used an outside source (I didn't use very many), I made sure to cite it with a comment. The only library I used was [Sparkjava](http://sparkjava.com/), an HTTP micro framework for Java. If you have any questions regarding my sources or use of libraries, please let me know.
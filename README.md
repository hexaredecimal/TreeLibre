# TreeLibre
An interactive 2D fractal tree generator/simulator created in Java.
> It simulates organic tree growth using a recursive branching model influenced by adjustable physical parameters such as
> branch splitting ratios, angles, etc.
> Each branch dynamically adapts to simulated wind and regenerates into unique tree structures at the press of a key.

# Showcase

<img width="60%" alt="image" src="https://github.com/user-attachments/assets/f9150436-f681-4bbd-9df4-a58c1b1c1f08" />


# Features
- [X] Dynamic trees
- [X] Dynamic flowers
- [X] Dynamic leafs
- [X] Simulation Controls
- [X] Full UI built in Java

# How It Works
1. The `Tree` class holds a single root branch.  
2. Each `Branch` recursively spawns two sub-branches, splitting length and thickness based on:
- **Flux division factor** – how much “energy” goes into each branch
- **Branching angle** – how far branches diverge
- **Scaling factor** – how quickly branches thin out
3. Branches stop growing when their diameter falls below a **leaf cutoff threshold**.  
4. Wind forces perturb the direction of each branch recursively, creating smooth, realistic motion.
5. Flowers and leafs are drawn at the **leaf cutoff threshold**

# Inspirations:
Augustge's Tree animation: [https://github.com/augustge/augustge.github.io/Animations/TreeGenerator](https://github.com/augustge/augustge.github.io/tree/master/Animations/TreeGenerator)

TreeStudio: [https://www.pixarra.com/tree_studio.html](https://www.pixarra.com/tree_studio.html)

# Libraries:
- [Java Swing](https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/javax/swing/)
- [FlatLaf](https://www.formdev.com/flatlaf/)
- [JGif](https://github.com/cerus/JGif)

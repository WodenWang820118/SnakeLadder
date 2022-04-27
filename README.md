# Overview
The base package is a variant of the [Snake and Ladders application](https://aplu.ch/home/apluhomex.jsp?site=128).
The source code is from the SWEN30006, Software Design and Modeling, the University of Melbourne.

Given the code, the goal is to refactor the code in favor of future development and maintenance. By applying the [GRASP](https://en.wikipedia.org/wiki/GRASP_(object-oriented_design)) principle, the refactored code achieves the goal, and the required implementations verify the theory.

# MVC architecture
The refactor strategy follows the model-view-controller (MVC) architecture with GRASP principles. The architecture is favored because it separates concerns within a feature. For instance, the model handles the data according to the controller's request in the game pane feature. The view responds to the data updates and renders the change to the graphical user interface (GUI). The architecture maintains high cohesion and uses pure fabrication, i.e., model, to achieve low coupling. Thus, the first goal was to decouple the data operation from the game pane and navigation pane.

# Dependency injection
The idea comes from the source code, where the implementation uses the setter injection, which is one of the dependency injections, to couple two panes. After some research, The dependency pattern is utilized since it strongly lower the dependency between entities or components inside an entity. The dependency injection instantiates the object and constructs the necessary objects together later. The pattern removes the higher-level object's dependency on low-level objects. For example, in the new design, the cup class and die class objects are independent of the navigation pane. However, the cup class can inject the navigation pane by setter injection to access its methods.

# Design
The original design model diagram and the domain class diagram are inside the design folder. The new design is also in the folder, with GRASP principles' marked for comparison.
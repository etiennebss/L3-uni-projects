# Projet Blockworld en Java

Ce projet a été réalisé par **@etiennebss** et **@Lebonvieuxjack** dans le cadre de l'option *Aide à la décision et Intelligence Artificielle*.

## Description

Le projet Blockworld est une implémentation en Java d'un environnement de type "monde de blocs" (*blocks world*), utilisé pour explorer des concepts liés à la planification, la résolution de contraintes et l'exploration de données. Il inclut plusieurs fonctionnalités permettant de visualiser et d'analyser des solutions à des problèmes complexes.

## Fonctionnalités principales

- **Visualisation des piles de blocs** :  
    La classe `BWExecutable.java` permet de visualiser la mise en place de listes de piles et de vérifier si les contraintes de régularité et d'ascendance sont respectées.

- **Planification d'actions** :  
    La classe `BWPlannerExecutable.java` utilise un objet `BWView` pour montrer les différentes actions effectuées par les planificateurs afin d'atteindre un état final (*goal*) à partir d'un état initial.

- **Comparaison des solveurs** :  
    La classe `BWCpExecutable.java` compare les performances des solveurs, notamment `MACSolver` et `backtrack`, en utilisant différentes heuristiques.

- **Exploration de données** :  
    La classe `BWDatamining.java` permet de créer une base de données, d'identifier les items fréquents dans un ensemble de données et de dégager des règles d'association.

## Objectifs pédagogiques

Ce projet vise à :
- Appliquer des concepts de planification et de résolution de contraintes.
- Explorer des algorithmes d'intelligence artificielle pour résoudre des problèmes complexes.
- Expérimenter avec des techniques de fouille de données (*data mining*).

package com.space_shooter.game.core;

public class GameConstants {
    static public int NUM_TELEPORT_CIRCLES = 10;
    static public float TELEPORT_ANIMATION_DURATION = 0.4f;
    static public float MAX_TELEPORT_DISTANCE = 500f;
    // Paramétres de la classe PlayerShip
    static public float PLAYER_SHIP_RADIUS = 2f;
    static public int PLAYER_SHIP_HEALTH = 300;
    static public float PLAYER_SHIP_SPEED = 50f;
    static public float TELEPORT_COOLDOWN = 1f;
    static public float COLLISION_COOLDOWN = 1f;
    // Paramétres de la classe KamikazeShip
    static public int KAMIKAZE_SHIP_SCORE_VALUE = 50;
    static public float KAMIKAZE_SHIP_RADIUS = 1.5f;
    static public int KAMIKAZE_SHIP_HEALTH = 1;
    static public float KAMIKAZE_SHIP_MIN_SPEED = 10f;
    static public float KAMIKAZE_SHIP_MAX_SPEED = 20f;
    static public float KAMIKAZE_SHIP_CLOSE_DISTANCE = 2f;
    // Paramétres de la classe DistanceShooterShip
    static public int DISTANCE_SHOOTER_SCORE_VALUE = 100;
    static public float DISTANCE_SHOOTER_RADIUS = 2f;
    static public int DISTANCE_SHOOTER_HEALTH = 3;
    static public float DISTANCE_SHOOTER_MIN_SPEED = 10f;
    static public float DISTANCE_SHOOTER_MAX_SPEED = 20f;
    static public float DISTANCE_SHOOTER_SCREEN_MARGIN = 10f;
    // Paramétres pour les armes
    static public String LASER_NAME = "Laser";
    static public float LASER_BEAM_WIDTH = 0.5f;
    static public int LASER_BEAM_TIMEOUT = 100;
    static public String BASIC_NAME = "Basic";
}

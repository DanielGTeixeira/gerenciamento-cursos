package br.com.seuapp.utils;
public class Validador {
    public static boolean naoVazio(String s) { return s != null && !s.trim().isEmpty(); }
}
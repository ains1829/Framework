package main;

import reading.*;
import writing.Writing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import connexion.Connect;

public class Main {
    public static void main(String[] args) {
        new Generate().generate(args);
    }
}

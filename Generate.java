package main;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import writing.*;
import reading.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import connexion.Connect;
import copie.CopieTemplate;

public class Generate {
    public void generate(String[] args) {
        try {
            File xmlFile = new File("./Server.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList config = doc.getElementsByTagName("Server");
            Element conf = (Element) config.item(0);
            Element base = (Element) conf.getElementsByTagName("base").item(0);
            Element postgres = (Element) base.getElementsByTagName("postgres").item(0);
            String userPostgres = postgres.getElementsByTagName("user").item(0).getTextContent();
            String passwordPostgres = postgres.getElementsByTagName("password").item(0).getTextContent();
            Element mysql = (Element) base.getElementsByTagName("mysql").item(0);
            String userMysql = mysql.getElementsByTagName("user").item(0).getTextContent();
            String passwordMysql = mysql.getElementsByTagName("password").item(0).getTextContent();
            Element paths = (Element) conf.getElementsByTagName("paths").item(0);
            String path = paths.getElementsByTagName("path").item(0).getTextContent();
            String packaging = "all";
            String table = "*";
            String language = "JAVA";
            String serveur = "postgres";
            String database = "stock";
            boolean controller = false;
            boolean build_project = false;
            String name_project = "Api";
            String name_view = "VueAngular";
            for (String arg : args) {
                if (arg.matches("--\\w+=.*")) {
                    String[] parts = arg.split("=", 2);
                    String key = parts[0].substring(2);
                    String value = parts[1];
                    if (key.compareToIgnoreCase("packaging") == 0) {
                        packaging = value;
                    }
                    if (key.compareToIgnoreCase("table") == 0) {
                        table = value;
                    }
                    if (key.compareToIgnoreCase("language") == 0) {
                        language = value;
                    }
                    if (key.compareToIgnoreCase("serveur") == 0) {
                        serveur = value;
                    }
                    if (key.compareToIgnoreCase("database") == 0) {
                        database = value;
                    }
                    if (key.compareToIgnoreCase("controller") == 0) {
                        controller = Boolean.valueOf(value);
                    }
                    if (key.compareToIgnoreCase("build_project") == 0) {
                        build_project = Boolean.valueOf(value);
                    }
                    if (key.compareToIgnoreCase("nameproject") == 0) {
                        name_project = String.valueOf(value);
                    }
                    if (key.compareToIgnoreCase("view") == 0) {
                        name_view = String.valueOf(value);
                    }
                }
            }
            if (build_project == false) {
                if (controller == false) {
                    if (serveur.compareToIgnoreCase("postgres") == 0) {
                        Connection con = new Connect().Connecter(serveur, database, userPostgres, passwordPostgres);
                        if (table.compareTo("*") == 0) {
                            List<ReaderSql> alltables = new ReaderSql().allTable(language, packaging, con);
                            for (int i = 0; i < alltables.size(); i++) {
                                Writing writing = new Writing((ReaderSql) alltables.get(i));
                                writing.generateclasses(path, language);
                            }
                        } else {
                            ReaderSql read = new ReaderSql().aboutThisTable(language, packaging, table, con);
                            Writing writing = new Writing(read);
                            writing.generateclasses(path, language);
                        }
                        con.close();
                    } else {
                        Connection con = new Connect().Connecter(serveur, database, userMysql, passwordMysql);
                        if (table.compareTo("*") == 0) {
                            List<ReaderSql> alltables = new ReaderSql().allTable(language, packaging, con);
                            for (int i = 0; i < alltables.size(); i++) {
                                Writing writing = new Writing((ReaderSql) alltables.get(i));
                                writing.generateclasses(path, language);
                            }
                        } else {
                            ReaderSql read = new ReaderSql().aboutThisTable(language, packaging, table, con);
                            Writing writing = new Writing(read);
                            writing.generateclasses(path, language);
                        }
                        con.close();
                    }
                } else {
                    if (serveur.compareToIgnoreCase("postgres") == 0) {
                        Connection con = new Connect().Connecter(serveur, database, userPostgres, passwordPostgres);
                        if (table.compareTo("*") == 0) {
                            List<ReaderSql> alltables = new ReaderSql().allTable(language, packaging, con);
                            for (int i = 0; i < alltables.size(); i++) {
                                Writing writing = new Writing((ReaderSql) alltables.get(i));
                                writing.generateController(con, path, language, packaging, alltables.get(i));
                            }
                        } else {
                            ReaderSql read = new ReaderSql().aboutThisTable(language, packaging, table, con);
                            Writing writing = new Writing(read);
                            writing.generateController(con, path, language, packaging, read);
                        }
                        con.close();
                    } else {
                        Connection con = new Connect().Connecter(serveur, database, userMysql, passwordMysql);
                        if (table.compareTo("*") == 0) {
                            List<ReaderSql> alltables = new ReaderSql().allTable(language, packaging, con);
                            for (int i = 0; i < alltables.size(); i++) {
                                Writing writing = new Writing((ReaderSql) alltables.get(i));
                                writing.generateController(con, path, language, packaging, alltables.get(i));
                            }
                        } else {
                            ReaderSql read = new ReaderSql().aboutThisTable(language, packaging, table, con);
                            Writing writing = new Writing(read);
                            writing.generateController(con, path, language, packaging, read);
                        }
                        con.close();
                    }
                    System.out.println("generate controller");
                }
            } else {
                new CopieTemplate().Copying(name_project, path, "Templating_C#");
                new CopieTemplate().Copying(name_view, path, "View/Angular_template");
                String path_build = path + name_project + "//";
                String path_view = path + name_view + "//";
                if (serveur.compareToIgnoreCase("postgres") == 0) {
                    Connection all_con = new Connect().Connecter(serveur, database, userPostgres, passwordPostgres);
                    if (new ReaderSql().getTableAuth(all_con)
                            .equalsIgnoreCase("")) {
                        new ReaderSql().CreateAuth(all_con);
                    }
                    if (table.compareTo("*") == 0) {
                        List<ReaderSql> alltables = new ReaderSql().allTable(language, "Model", all_con);
                        for (int i = 0; i < alltables.size(); i++) {
                            Writing writing = new Writing((ReaderSql) alltables.get(i));
                            writing.generateclasses(path_build, language);
                        }
                        List<ReaderSql> alltables_controllers = new ReaderSql().allTable(language, "Controllers",
                                all_con);
                        for (int i = 0; i < alltables.size(); i++) {
                            Writing writing = new Writing((ReaderSql) alltables_controllers.get(i));
                            writing.generateController(all_con, path_build, language, name_project + ".Controllers",
                                    alltables_controllers.get(i));
                        }
                        new Writing().AllExecute(all_con, path_build, path_view, alltables, database, userPostgres,
                                passwordPostgres);

                    } else {
                        String table_auth = new ReaderSql().getTableAuth(all_con);
                        ReaderSql auth = new ReaderSql().aboutThisTable(language, "Model",
                                table_auth, all_con);
                        ReaderSql read = new ReaderSql().aboutThisTable(language, "Model",
                                table, all_con);
                        ReaderSql auth_controllers = new ReaderSql().aboutThisTable(language, "Controllers",
                                table_auth, all_con);
                        ReaderSql read_controllers = new ReaderSql().aboutThisTable(language, "Controllers",
                                table, all_con);
                        List<ReaderSql> alltables = new ArrayList<>();
                        alltables.add(auth);
                        alltables.add(read);
                        List<ReaderSql> alltables_controllers = new ArrayList<>();
                        alltables_controllers.add(auth_controllers);
                        alltables_controllers.add(read_controllers);
                        for (int i = 0; i < alltables.size(); i++) {
                            Writing writing = new Writing((ReaderSql) alltables.get(i));
                            writing.generateclasses(path_build, language);
                        }
                        for (int i = 0; i < alltables.size(); i++) {
                            Writing writing = new Writing((ReaderSql) alltables_controllers.get(i));
                            writing.generateController(all_con, path_build, language, name_project + ".Controllers",
                                    alltables_controllers.get(i));
                        }
                        new Writing().AllExecute(all_con, path_build, path_view, alltables, database, userPostgres,
                                passwordPostgres);
                    }
                    all_con.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

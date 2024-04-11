package writing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import copie.CopieTemplate;
import reading.ReaderSql;

public class Writing {
    ReaderSql sqlreading;

    public ReaderSql getSqlreading() {
        return sqlreading;
    }

    public void setSqlreading(ReaderSql sqlreading) {
        this.sqlreading = sqlreading;
    }

    public Writing(ReaderSql sqlreading) {
        this.sqlreading = sqlreading;
    }

    public Writing() {
    }

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public String affiche_template(String template) {
        String cheminFichier = "./data/" + template;
        String content_template = "";
        try {
            String ligne;
            FileReader fileReader = new FileReader(cheminFichier);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((ligne = bufferedReader.readLine()) != null) {
                content_template += ligne;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content_template;
    }

    public Document getMyDocument(String xml) throws Exception {
        File xmlFile = new File("./data/" + xml);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        return doc;
    }

    public String getPackaging(String techno) throws Exception {
        String packaging = "package";
        NodeList config = getMyDocument("cs.xml").getElementsByTagName("config");
        Element conf = (Element) config.item(0);
        Element configjava = (Element) conf.getElementsByTagName("java").item(0);
        String package_java = configjava.getElementsByTagName("package").item(0).getTextContent();
        Element config_csharp = (Element) conf.getElementsByTagName("csharp").item(0);
        String package_csharp = config_csharp.getElementsByTagName("package").item(0).getTextContent();

        if (techno.compareTo("C#") == 0) {
            packaging = package_csharp;
        } else {
            packaging = package_java;
        }
        return packaging;
    }

    public String getNameImportation(String techno) throws Exception {
        String importing = "package";
        NodeList config = getMyDocument("cs.xml").getElementsByTagName("config");
        Element conf = (Element) config.item(0);
        Element configjava = (Element) conf.getElementsByTagName("java").item(0);
        String import_java = configjava.getElementsByTagName("import").item(0).getTextContent();
        Element config_csharp = (Element) conf.getElementsByTagName("csharp").item(0);
        String import_csharp = config_csharp.getElementsByTagName("import").item(0).getTextContent();

        if (techno.compareTo("C#") == 0) {
            importing = import_csharp;
        } else {
            importing = import_java;
        }
        return importing;
    }

    public String NameFichier(String techno) {
        String nomFichier = "";
        if (techno.compareToIgnoreCase("C#") == 0) {
            nomFichier = capitalizeFirstLetter(this.sqlreading.getNomTable()) + ".cs";
        } else {
            nomFichier = capitalizeFirstLetter(this.sqlreading.getNomTable()) + ".java";
        }
        return nomFichier;
    }

    public String NameFichierController(String techno) {
        String nomFichier = "";
        if (techno.compareToIgnoreCase("C#") == 0) {
            nomFichier = capitalizeFirstLetter(this.sqlreading.getNomTable()) + "Controller.cs";
        } else {
            nomFichier = capitalizeFirstLetter(this.sqlreading.getNomTable()) + "Controller.java";
        }
        return nomFichier;
    }

    public File PathFichier(String path, String nomFichier) throws IOException {
        String folderPath = path + this.sqlreading.getPackage_table();
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File fichier = new File(folder, nomFichier);
        if (!fichier.exists()) {
            fichier.createNewFile();
        }
        return fichier;
    }

    public void CreateStructure(String path, List<ReaderSql> all_table) throws IOException {
        for (int i = 0; i < all_table.size(); i++) {
            new CopieTemplate().Copying(all_table.get(i).getNomTable(), path + "src/app/", "Temps");
        }
    }

    public String changepackage(String techno, String template) throws Exception {
        String packaging = "";
        packaging = template.replace("#packaging#", getPackaging(techno));
        return packaging;
    }

    public String Importation(String techno) throws Exception {
        String imports = "";
        int compteur = 0;
        for (int i = 0; i < this.sqlreading.getImportation().size(); i++) {
            if (this.sqlreading.getImportation().get(i).compareToIgnoreCase("") != 0) {
                if (compteur == 0) {
                    imports += getNameImportation(techno) + " " + this.sqlreading.getImportation().get(i) + " ; "
                            + "\n";
                } else {
                    imports += getNameImportation(techno) + " " + this.sqlreading.getImportation().get(i) + " ; "
                            + "\n";
                }
                compteur++;
            }
        }
        return imports;
    }

    public String[] getConfigAttribut() throws Exception {
        NodeList config = getMyDocument("cs.xml").getElementsByTagName("config");
        Element conf = (Element) config.item(0);
        Element fonction = (Element) conf.getElementsByTagName("function").item(0);
        String visible = fonction.getElementsByTagName("visible").item(0).getTextContent();
        String beginget = fonction.getElementsByTagName("beginget").item(0).getTextContent();
        String beginset = fonction.getElementsByTagName("beginset").item(0).getTextContent();
        String endfunction = fonction.getElementsByTagName("endfunction").item(0).getTextContent();
        String excepion = fonction.getElementsByTagName("exception").item(0).getTextContent();
        String csharp = fonction.getElementsByTagName("c").item(0).getTextContent();
        String dbset = fonction.getElementsByTagName("dbset").item(0).getTextContent();
        String entity = fonction.getElementsByTagName("entity").item(0).getTextContent();
        String configentity = fonction.getElementsByTagName("configentity").item(0).getTextContent();
        String privates = fonction.getElementsByTagName("private").item(0).getTextContent();
        String[] configs = { visible, beginget, beginset, endfunction, excepion, csharp, dbset, entity, configentity,
                privates };
        return configs;
    }

    public void Change_appjson(String path, String database, String username, String password) {
        try {
            String content_db = "";
            FileReader fileReader = new FileReader(path + "appsettings.json");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while (((line = bufferedReader.readLine()) != null)) {
                content_db += line;
            }
            bufferedReader.close();
            File fichier = new File(path + "appsettings.json");
            FileWriter writers = new FileWriter(fichier);
            String change_bd = content_db.replace("#database#", database);
            String changeuser = change_bd.replace("#username#", username);
            String changepassword = changeuser.replace("#password#", password);
            writers.write(changepassword);
            writers.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Change_config_service_view(Connection con, String path_view, List<ReaderSql> content_sql) {
        try {
            String content = config_view_service(con, content_sql);
            File fichier = new File(path_view + "src/app/services/main.service.ts");
            FileWriter writers = new FileWriter(fichier);
            writers.write(content);
            writers.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Change_config_routing_view(String path_view, List<ReaderSql> content_sql) {
        try {
            String content = config_view_routing(content_sql);
            File fichier = new File(path_view + "src/app/app-routing.module.ts");
            FileWriter writers = new FileWriter(fichier);
            writers.write(content);
            writers.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ChangeIntoApp(Connection con, String path, List<ReaderSql> content_sql, String database,
            String username,
            String password) {
        try {
            String[] configs = getConfigAttribut();
            String content_db = "";
            FileReader fileReader = new FileReader(path + "Model/AppDbContext.cs");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while (((line = bufferedReader.readLine()) != null)) {
                content_db += line;
                System.out.println(line);
            }
            bufferedReader.close();
            File fichier = new File(path + "Model/AppDbContext.cs");
            FileWriter writers = new FileWriter(fichier);
            String content_function = "";
            for (int i = 0; i < content_sql.size(); i++) {
                content_function += "   " + configs[0] + " "
                        + configs[6].replace("#nameclasses#", capitalizeFirstLetter(content_sql.get(i).getNomTable()))
                        + " " + content_sql.get(i).getNomTable() + " " + configs[5] + "\n";
            }
            String content_entity = "";
            for (int i = 0; i < content_sql.size(); i++) {
                String primarykey = content_sql.get(i).checkIfAttributeIsPrimaryKey(con);
                content_entity += "     "
                        + configs[7].replace("#nameclasses#", capitalizeFirstLetter(content_sql.get(i).getNomTable()))
                                .replace("#primary#", primarykey).replace("#syntaxe#", "=>")
                        + "\n";
            }
            String finals_change = content_db.replace("//content_config", content_function);
            String final_entity_primary = finals_change.replace("//Entity_frameworks_primary_key", content_entity);
            writers.write(final_entity_primary);
            writers.close();
            Change_appjson(path, database, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String giveattribut(String techno, String template_attribut) throws Exception {
        String type_variable = "";
        String[] configs = getConfigAttribut();
        String get_set = "";
        for (int i = 0; i < this.sqlreading.getType_column().size(); i++) {
            String remplace_type = template_attribut.replace("##type##", this.sqlreading.getType_column().get(i));
            String remplaca_name = remplace_type.replace("##name##", this.sqlreading.getName_column().get(i));
            String remplace_visible = remplaca_name.replace("##visible##", configs[0]);
            String remplace_retourget = remplace_visible.replace("##typeretourget##",
                    this.sqlreading.getType_column().get(i));
            String remplace_retourset = remplace_retourget.replace("##typeretourset##", "void");
            String remplace_beginget = remplace_retourset.replace("##beginget##", configs[1]);
            String remplace_beginset = remplace_beginget.replace("##beginset##", configs[2]);
            String name_capital = remplace_beginset.replace("##nameCa##",
                    capitalizeFirstLetter(this.sqlreading.getName_column().get(i)));
            String finals = name_capital.replace("##endfunction##", configs[3] + "\n");
            String css = finals.replace("#down#", "\n");
            String css_final = css.replace("#tab#", "    ");
            if (techno.compareToIgnoreCase("JAVA") == 0) {
                get_set = css_final.replace("##ca_depend##", configs[3]);
            } else {
                get_set = css_final.replace("##ca_depend##", configs[5]);
            }
            type_variable += get_set;
        }
        return type_variable;
    }

    public void generateclasses(String path, String techno) throws Exception {
        String nomFichier = NameFichier(techno);
        String template = affiche_template("template.txt");
        String attribut_template = affiche_template("Attribut.txt");
        try {
            File fichier = PathFichier(path, nomFichier);
            FileWriter writers = new FileWriter(fichier);
            String pakage = changepackage(techno, template).replace("#templatepackage;#",
                    (this.sqlreading.getPackage_table() + ";\n"));
            String lib = pakage.replace("#importing#", Importation(techno));
            String name_classes = lib.replace("#template{#",
                    capitalizeFirstLetter(this.sqlreading.getNomTable()) + "{\n");
            String contru = name_classes.replace("#constructor#(){}",
                    capitalizeFirstLetter(this.sqlreading.getNomTable()) + "(){}\n");
            String attriubt = contru.replace("#attributes#", giveattribut(techno, attribut_template));
            writers.write(attriubt);
            writers.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Give_attribute_view(String template_attributs, List<String> column, List<String> type) {
        String attribut = "";
        for (int i = 0; i < column.size(); i++) {
            String template = template_attributs;
            if (type.get(i).equalsIgnoreCase("INT") || type.get(i).equalsIgnoreCase("INTEGER")) {
                attribut += template.replace("#column#", column.get(i)).replace("#type#", "number");
            } else {
                attribut += template.replace("#column#", column.get(i)).replace("#type#", "string");
            }
            if (i != column.size() - 1) {
                attribut += ",";
            }
        }
        return attribut;
    }

    public String Give_attribut_InsertTs(String template_attribut, List<String> column, List<String> type) {
        String attribut = "";
        for (int i = 0; i < column.size(); i++) {
            String template = template_attribut;
            if (type.get(i).equalsIgnoreCase("INT") || type.get(i).equalsIgnoreCase("INTEGER")) {
                attribut += template.replace("#attribut#", column.get(i)).replace("#type#", "number");
            } else {
                attribut += template.replace("#attribut#", column.get(i)).replace("#type#", "string");
            }
        }
        return attribut;
    }

    public String Give_Argument_InsertTs(String template_fonction, List<String> column) {
        String argument_fonction = "";
        for (int i = 0; i < column.size(); i++) {
            if (i != column.size() - 1) {
                argument_fonction += template_fonction.replace("#at#", column.get(i)) + " ,";
            } else {
                argument_fonction += template_fonction.replace("#at#", column.get(i));
            }
        }
        return argument_fonction;
    }

    public String Change_content_classes(String template_classes, List<ReaderSql> alltables) {
        String classes = "";
        for (int i = 0; i < alltables.size(); i++) {
            String templates = template_classes;
            classes += templates.replace("#classe#", alltables.get(i).getNomTable());
        }
        return classes;
    }

    public String Change_content_columns(String template_column, List<String> columns) {
        String classes = "";
        for (int i = 0; i < columns.size(); i++) {
            String templates = template_column;
            classes += templates.replace("#column#", columns.get(i));
            if (i != columns.size() - 1) {
                classes += " , ";
            }
        }
        return classes;
    }

    public String Change_content_columns_save(Connection con, ReaderSql name, String template_column,
            List<String> columns) {
        String classes = "";
        if (name.TypePrimaryKey(con).equalsIgnoreCase("string")) {
            classes += template_column.replace(":#column# ", ": \"\" ,").replace("#column#",
                    name.checkIfAttributeIsPrimaryKey(con));
        }
        for (int i = 0; i < columns.size(); i++) {
            String templates = template_column;
            classes += templates.replace("#column#", columns.get(i));
            if (i != columns.size() - 1) {
                classes += " , ";
            }
        }
        return classes;
    }

    public String config_view_routing(List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("ionic_routing.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        String fonctions = "";
        for (int i = 0; i < alltables.size(); i++) {
            for (int j = 0; j < ionic.getElementsByTagName("fonction").getLength(); j++) {
                String fonction = ionic.getElementsByTagName("fonction").item(j).getTextContent().replace("#classe#",
                        alltables.get(i).getNomTable());
                fonctions += fonction;
            }
        }
        return content.replace("//routing//", fonctions);
    }

    public String Content_sidebar(List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("sidebar.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        String content_sidebar = "";
        for (int i = 0; i < alltables.size(); i++) {
            String sidebar = ionic.getElementsByTagName("sidebar").item(0).getTextContent();
            content_sidebar += sidebar.replace("#classe#", alltables.get(i).getNomTable());
        }
        return content.replace("#routing#", content_sidebar);
    }

    public void Home_html(Connection con, String path_view, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("HomeHtml.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        for (int i = 0; i < alltables.size(); i++) {
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable() + "/home/home.page.html";
            String label = alltables.get(i).getColumnShow(con, "label");
            String change_home_html = content.replace("#classe#", alltables.get(i).getNomTable()).replace("#Classe#",
                    capitalizeFirstLetter(alltables.get(i).getNomTable())).replace("#label#", label);
            Write(reel_path, change_home_html);
        }
    }

    public String Update_atribut(String template, List<String> column) {
        String content = "";
        for (int i = 0; i < column.size(); i++) {
            content += template.replace("#attribut#", column.get(i));
        }
        return content;
    }

    public String Update_Value(String template, List<String> column_not_primary) {
        String content = "";
        for (int i = 0; i < column_not_primary.size(); i++) {
            content += template.replace("#attribut#", column_not_primary.get(i));
        }
        return content;
    }

    public String Argument_update(String template, List<String> column) {
        String content = "";
        for (int i = 0; i < column.size(); i++) {
            if (i != column.size() - 1) {
                content += template.replace("#attribut#", column.get(i)) + " , ";
            } else {
                content += template.replace("#attribut#", column.get(i));
            }
        }
        return content;
    }

    public void UpdateTs(Connection con, String path_view, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("UpdateTs.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        String attribut = ionic.getElementsByTagName("attribut").item(0).getTextContent();
        String change_val = ionic.getElementsByTagName("change_val").item(0).getTextContent();
        String arguments = ionic.getElementsByTagName("arguments").item(0).getTextContent();
        String content_classe = ionic.getElementsByTagName("classe").item(0).getTextContent();
        String content_api = ionic.getElementsByTagName("callapi").item(0).getTextContent();
        String inits = ionic.getElementsByTagName("initapi").item(0).getTextContent();
        for (int i = 0; i < alltables.size(); i++) {
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable()
                    + "/formulaire/modifier/modifier.page.ts";
            List<String> column_not_primary = alltables.get(i).Column_not_primary(con);
            String content_table_referenced = Content_tableReferenced(con, content_classe,
                    alltables.get(i));
            String content_call_api = Content_CallApi(con, content_api, alltables.get(i));
            String content_inits = Init_CallApi(con, inits, alltables.get(i));
            String change_updatets = content.replace("#Classe#", capitalizeFirstLetter(alltables.get(i).getNomTable()))
                    .replace("#classe#", alltables.get(i).getNomTable())
                    .replace("#attributs#", Update_atribut(attribut, alltables.get(i).getName_column()))
                    .replace("#change_vals#", Update_Value(change_val, column_not_primary))
                    .replace("#arguments#", Argument_update(arguments, alltables.get(i).getName_column()))
                    .replace("#classe_referenced#", content_table_referenced)
                    .replace("#init#", content_inits)
                    .replace("#call_api#", content_call_api);
            Write(reel_path, change_updatets);
        }
    }

    public void UpdateHtml(Connection con, String path_view, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("UpdateHtml.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String template_input = ionic.getElementsByTagName("input").item(0).getTextContent();
        String template_select = ionic.getElementsByTagName("select").item(0).getTextContent();
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        for (int i = 0; i < alltables.size(); i++) {
            String label = alltables.get(i).getColumnShow(con, "label");
            List<String> columns_not_primary = alltables.get(i).Column_not_primary(con);
            String contents = content
                    .replace("#inputs#",
                            ContentUpdate_input(con, template_input, template_select, columns_not_primary,
                                    alltables.get(i)))
                    .replace("#Classe#", capitalizeFirstLetter(alltables.get(i).getNomTable()))
                    .replace("#label#", label);
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable()
                    + "/formulaire/modifier/modifier.page.html";
            Write(reel_path, contents);
        }
    }

    public void Home_ts(String path_view, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("HomePage.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        for (int i = 0; i < alltables.size(); i++) {
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable() + "/home/home.page.ts";
            String change_home_ts = content.replace("#classe#", alltables.get(i).getNomTable()).replace("#Classe#",
                    capitalizeFirstLetter(alltables.get(i).getNomTable())).replace("and__", "&&");
            Write(reel_path, change_home_ts);
        }
    }

    public void Sidebar_acceuil(String path_view, List<ReaderSql> alltables) throws Exception {
        String reel_path = path_view + "src/app/acceuil/sidebar-acc/sidebar-acc.page.html";
        Write(reel_path, Content_sidebar(alltables));
    }

    public void Sidebar(String path_view, List<ReaderSql> alltables) throws Exception {
        for (int i = 0; i < alltables.size(); i++) {
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable()
                    + "/home/sidebar/sidebar.page.html";
            Write(reel_path, Content_sidebar(alltables));
        }
    }

    public void Write(String path, String context) throws Exception {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        writer.write(context);
        writer.close();
    }

    public String ContentInsert_input(Connection con, String template_input, String template_select,
            List<String> column, ReaderSql table) {
        String content = "";
        for (int i = 0; i < column.size(); i++) {
            String content_table_referenced = table.getNameTableReferenced(con, column.get(i));
            List<String> type_column = table.Column_type(con);
            if (content_table_referenced.equalsIgnoreCase("")) {
                String temp_template = template_input;
                if (!type_column.get(i).equalsIgnoreCase("DateTime")) {
                    content += temp_template.replace("#attribut#", column.get(i));
                } else {
                    content += temp_template.replace("#attribut#", column.get(i))
                            .replace("ion-input", "ion-datetime")
                            .replace("[clearInput]=\"true\"", "displayFormat=\"YYYY-MM-DD\"");
                }
            } else {
                ReaderSql myReader = new ReaderSql().aboutThisTable("C#", "",
                        content_table_referenced, con);
                String primarykeys = myReader.checkIfAttributeIsPrimaryKey(con);
                String label = myReader.getColumnShow(con, "label");
                String temp_template = template_select;
                content += temp_template.replace("#table_referenced#", content_table_referenced)
                        .replace("#attribut#", column.get(i))
                        .replace("#primarykeys#", primarykeys)
                        .replace("#label#", label);
            }
        }
        return content;
    }

    public String ContentUpdate_input(Connection con, String template_input, String template_select,
            List<String> column,
            ReaderSql table) {
        String content = "";
        for (int i = 0; i < column.size(); i++) {
            String content_table_referenced = table.getNameTableReferenced(con, column.get(i));
            List<String> type_column = table.Column_type(con);
            if (content_table_referenced.equalsIgnoreCase("")) {
                String temp_template = template_input;
                if (!type_column.get(i).equalsIgnoreCase("DateTime")) {
                    content += temp_template.replace("#attribut#", column.get(i));
                } else {
                    content += temp_template.replace("#attribut#", column.get(i))
                            .replace("ion-input", "ion-datetime")
                            .replace("[clearInput]=\"true\"", "displayFormat=\"YYYY-MM-DD\"");
                }
            } else {
                ReaderSql myReader = new ReaderSql().aboutThisTable("C#", "",
                        content_table_referenced, con);
                String primarykeys = myReader.checkIfAttributeIsPrimaryKey(con);
                String label = myReader.getColumnShow(con, "label");
                String temp_template = template_select;
                content += temp_template.replace("#table_referenced#", content_table_referenced)
                        .replace("#attribut#", column.get(i))
                        .replace("#primarykeys#", primarykeys)
                        .replace("#label#", label);
            }
        }
        return content;
    }

    public String Content_Modallabel(Connection con, String template, List<String> column, ReaderSql table) {
        String contents = "";
        for (int i = 0; i < column.size(); i++) {
            String table_referenced = table.getNameTableReferenced(con,
                    column.get(i));
            if (!table_referenced.equalsIgnoreCase("")) {
                ReaderSql myReader = new ReaderSql().aboutThisTable("C#", "",
                        table_referenced, con);
                String label = myReader.getColumnShow(con, "label");
                contents += template
                        .replace("#Attribut#", capitalizeFirstLetter(label))
                        .replace("item.#attribut#", label);
            } else {
                contents += template.replace("#Attribut#", capitalizeFirstLetter(column.get(i))).replace("#attribut#",
                        column.get(i));
            }
        }
        return contents;
    }

    public void ModalHtml(Connection con, String path_view, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("ModalHtml.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String label = ionic.getElementsByTagName("label").item(0).getTextContent();
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        for (int i = 0; i < alltables.size(); i++) {
            String label_show = alltables.get(i).getColumnShow(con, "label");
            String contents = content
                    .replace("#label#",
                            Content_Modallabel(con, label, alltables.get(i).getName_column(), alltables.get(i)))
                    .replace("#Classe#", capitalizeFirstLetter(alltables.get(i).getNomTable()))
                    .replace("#primarykeys#", label_show);
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable()
                    + "/formulaire/modal/modal.page.html";
            Write(reel_path, contents);
        }
    }

    public String Content_label_referenced(Connection con, String template_label, List<String> column_not_primary,
            ReaderSql table) {
        String content = "";
        for (int i = 0; i < column_not_primary.size(); i++) {
            String Table_referenced = table.getNameTableReferenced(con,
                    column_not_primary.get(i));
            if (!Table_referenced.equalsIgnoreCase("")) {
                ReaderSql myReader = new ReaderSql().aboutThisTable("C#", "",
                        Table_referenced, con);
                String label = myReader.getColumnShow(con, "label");
                content += template_label.replace("#label#", label);
            }
        }
        return content;
    }

    public String[] Content_call_api_find(Connection con, String template_init, String template_call_api,
            List<String> column_not_primary, ReaderSql table) {
        String content = "";
        String content_init = "";
        for (int i = 0; i < column_not_primary.size(); i++) {
            String Table_referenced = table.getNameTableReferenced(con, column_not_primary.get(i));
            if (!Table_referenced.equalsIgnoreCase("")) {
                ReaderSql myReader = new ReaderSql().aboutThisTable("C#", "",
                        Table_referenced, con);
                String label = myReader.getColumnShow(con, "label");
                content += template_call_api
                        .replace("#label#", label)
                        .replace("#TableReferenced#", capitalizeFirstLetter(Table_referenced))
                        .replace("#tableReferenced#", Table_referenced)
                        .replace("#colonne_referenced#", column_not_primary.get(i));
                content_init += template_init
                        .replace("#TableReferenced#", capitalizeFirstLetter(Table_referenced));
            }
        }
        String[] api = { content, content_init };
        return api;
    }

    public void ModalTs(Connection con, String path_view, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("ModalTs.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        String content_init = ionic.getElementsByTagName("init").item(0).getTextContent();
        String call_api = ionic.getElementsByTagName("call_api").item(0).getTextContent();
        String attribut_reference = ionic.getElementsByTagName("attribut-reference").item(0).getTextContent();
        for (int i = 0; i < alltables.size(); i++) {
            String primarykeys = alltables.get(i).checkIfAttributeIsPrimaryKey(con);
            List<String> column_not_primary = alltables.get(i).Column_not_primary(con);
            String[] api_content = Content_call_api_find(con, content_init, call_api, column_not_primary,
                    alltables.get(i));
            String attribut_refer = Content_label_referenced(con, attribut_reference, column_not_primary,
                    alltables.get(i));
            String contents = content.replace("#primarykeys#", primarykeys)
                    .replace("#Classe#", capitalizeFirstLetter(alltables.get(i).getNomTable()))
                    .replace("#classe#", alltables.get(i).getNomTable())
                    .replace("#attribut_label_referenced#", attribut_refer)
                    .replace("#init_call_api#", api_content[1])
                    .replace("#call_api#", api_content[0]);
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable()
                    + "/formulaire/modal/modal.page.ts";
            Write(reel_path, contents);
        }
    }

    public String Content_tableReferenced(Connection con, String template, ReaderSql table) {
        List<String> column = table.getName_column();
        String contents_table_referenced = "";
        for (int i = 0; i < column.size(); i++) {
            String Table_referenced = table.getNameTableReferenced(con, column.get(i));
            if (!Table_referenced.equalsIgnoreCase("")) {
                contents_table_referenced += template.replace(" #table_referenced#", Table_referenced);
            }
        }
        return contents_table_referenced;
    }

    public String Content_CallApi(Connection con, String template, ReaderSql table) {
        List<String> column = table.getName_column();
        String content_call_api = "";
        for (int i = 0; i < column.size(); i++) {
            String Table_referenced = table.getNameTableReferenced(con, column.get(i));
            if (!Table_referenced.equalsIgnoreCase("")) {
                content_call_api += template.replace("#classe#", Table_referenced).replace("#Classe#",
                        capitalizeFirstLetter(Table_referenced));
            }
        }
        return content_call_api;
    }

    public String Init_CallApi(Connection con, String template, ReaderSql table) {
        List<String> column = table.getName_column();
        String content_call_api = "";
        for (int i = 0; i < column.size(); i++) {
            String Table_referenced = table.getNameTableReferenced(con, column.get(i));
            if (!Table_referenced.equalsIgnoreCase("")) {
                content_call_api += template.replace("#Classe#",
                        capitalizeFirstLetter(Table_referenced));
            }
        }
        return content_call_api;
    }

    public void InsertHtml(Connection con, String path_view, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("InsertHtml.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String template_input = ionic.getElementsByTagName("input").item(0).getTextContent();
        String template_select = ionic.getElementsByTagName("select").item(0).getTextContent();
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        for (int i = 0; i < alltables.size(); i++) {
            List<String> columns_not_primary = alltables.get(i).Column_not_primary(con);
            String contents = content
                    .replace("#inputs#",
                            ContentInsert_input(con, template_input, template_select, columns_not_primary,
                                    alltables.get(i)))
                    .replace("#Classe#", capitalizeFirstLetter(alltables.get(i).getNomTable()));
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable()
                    + "/formulaire/insert/insert.page.html";
            Write(reel_path, contents);
        }
    }

    public void InsertTs(Connection con, String path_view, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("InsertTs.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String attribut = ionic.getElementsByTagName("attribut").item(0).getTextContent();
        String attribut_fonction = ionic.getElementsByTagName("attribut_fonction").item(0).getTextContent();
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        String content_classe = ionic.getElementsByTagName("classe").item(0).getTextContent();
        String content_api = ionic.getElementsByTagName("callapi").item(0).getTextContent();
        String inits = ionic.getElementsByTagName("initapi").item(0).getTextContent();
        for (int i = 0; i < alltables.size(); i++) {
            String at_temps = attribut;
            String argument = attribut_fonction;
            List<String> columns_not_primary = alltables.get(i).Column_not_primary(con);
            String content_table_referenced = Content_tableReferenced(con, content_classe, alltables.get(i));
            String content_call_api = Content_CallApi(con, content_api, alltables.get(i));
            String content_inits = Init_CallApi(con, inits, alltables.get(i));
            String contents = content.replace("#attributs#", Give_attribut_InsertTs(attribut,
                    alltables.get(i).getName_column(), alltables.get(i).getType_column()))
                    .replace("#attribut_fonction#", Give_Argument_InsertTs(
                            argument, columns_not_primary))
                    .replace("#Classe#", capitalizeFirstLetter(alltables.get(i).getNomTable()))
                    .replace("#classe#", alltables.get(i).getNomTable())
                    .replace("#classe_referenced#", content_table_referenced)
                    .replace("#init#", content_inits)
                    .replace("#call_api#", content_call_api);
            String reel_path = path_view + "src/app/" + alltables.get(i).getNomTable()
                    + "/formulaire/insert/insert.page.ts";
            Write(reel_path, contents);
        }
    }

    public String config_view_service(Connection con, List<ReaderSql> alltables) throws Exception {
        NodeList config_ionic = getMyDocument("ionic.xml").getElementsByTagName("ionic");
        Element ionic = (Element) config_ionic.item(0);
        String content = ionic.getElementsByTagName("content").item(0).getTextContent();
        String classes = ionic.getElementsByTagName("classes").item(0).getTextContent();
        String attribut = ionic.getElementsByTagName("attribut").item(0).getTextContent();
        String columns = ionic.getElementsByTagName("column").item(0).getTextContent();
        String fonctions = "";
        for (int u = 0; u < alltables.size(); u++) {
            int begin = 0;
            if (!alltables.get(u).getNomTable().equalsIgnoreCase(new ReaderSql().getTableAuth(con))) {
                begin = 1;
            }
            for (int i = begin; i < ionic.getElementsByTagName("fonction").getLength(); i++) {
                List<String> columns_not_primary = alltables.get(u).Column_not_primary(con);
                List<String> columns_type = alltables.get(u).Column_type(con);
                String primaryKes = alltables.get(u).checkIfAttributeIsPrimaryKey(con);
                String fonction = ionic.getElementsByTagName("fonction").item(i).getTextContent()
                        .replace("#classe#",
                                alltables.get(u).getNomTable())
                        .replace("et__", "&&")
                        .replace("#columns#",
                                Change_content_columns_save(con, alltables.get(u), columns, columns_not_primary))
                        .replace("##attribut##", Give_attribute_view(attribut, columns_not_primary, columns_type))
                        .replace("##attribut_update##",
                                Give_attribute_view(attribut, alltables.get(u).getName_column(),
                                        alltables.get(u).getType_column()))
                        .replace("#columns_update#", Change_content_columns(columns, alltables.get(u)
                                .getName_column()))
                        .replace("#primarykey#", primaryKes)
                        .replace("#label#", alltables.get(u).getColumnShow(con, "label"))
                        .replace("#password#", alltables.get(u).getColumnShow(con, "password"));
                fonctions += fonction;
            }
        }
        String classes_ = Change_content_classes(classes, alltables);
        String final_config_service = content.replace("##classes##", classes_)
                .replace("##fonction##", fonctions);
        return final_config_service;
    }

    public String[] Config(Connection con, String techno, String template_attributs) throws Exception {
        String[] configs = getConfigAttribut();
        NodeList config = getMyDocument("config.xml").getElementsByTagName("config");
        Element conf = (Element) config.item(0);
        Element spring = null;
        Element controller_config = (Element) conf.getElementsByTagName("ControllerConfig").item(0);
        if (techno.compareToIgnoreCase("JAVA") == 0) {
            spring = (Element) controller_config.getElementsByTagName("Spring").item(0);
        } else if (techno.compareToIgnoreCase("Framework") == 0) {
            spring = (Element) controller_config.getElementsByTagName("framework").item(0);
        } else {
            spring = (Element) controller_config.getElementsByTagName("Apicsharp").item(0);
        }
        Element pack = (Element) spring.getElementsByTagName("pack").item(0);
        String importations = "";
        for (int i = 0; i < pack.getElementsByTagName("package").getLength(); i++) {
            importations += getNameImportation(techno) + ""
                    + spring.getElementsByTagName("package").item(i).getTextContent() + ";\n";
        }
        String conf_header = "";
        Element header = (Element) spring.getElementsByTagName("config-controller").item(0);
        for (int i = 0; i < header.getElementsByTagName("header").getLength(); i++) {
            String remplace_name_controller = header.getElementsByTagName("header").item(i).getTextContent().trim()
                    .replace("#nameController#", this.sqlreading.getNomTable());
            conf_header += remplace_name_controller + "\n";
        }
        Element standard = (Element) spring.getElementsByTagName("standard").item(0);
        int begin = 0;
        String fonction_standard = "";
        if (!this.sqlreading.getNomTable()
                .equalsIgnoreCase(new ReaderSql().getTableAuth(con))) {
            begin = 1;
        }
        String template_attribut_insert = standard.getElementsByTagName("attribut").item(0).getTextContent();
        String template_key_default = standard.getElementsByTagName("primarykey").item(0).getTextContent();
        for (int i = begin; i < standard.getElementsByTagName("fonction").getLength(); i++) {
            String template_attribut = affiche_template(template_attributs);
            Element fonction_ = (Element) standard.getElementsByTagName("fonction").item(i);
            String key = fonction_.getElementsByTagName("key").item(0).getTextContent().trim();
            String map = fonction_.getElementsByTagName("map").item(0).getTextContent().trim();
            String change_map = "";
            String change = "";
            if (map.equalsIgnoreCase("IsPrivate")) {
                change_map = template_attribut.replace("##configApi##", "");
                change = change_map.replace("##visible##", configs[9]);
            } else {
                change_map = template_attribut.replace("##configApi##", map);
                change = change_map.replace("##visible##", configs[0]);
            }
            String content = fonction_.getElementsByTagName("content").item(0).getTextContent().trim();
            String content_id_default = "";
            if (sqlreading.TypePrimaryKey(con).equalsIgnoreCase("string")) {
                content_id_default = content.replace("#generate_id#", template_key_default);
            } else {
                content_id_default = content.replace("#generate_id#", "");
            }
            String tab_ = change.replace("#tab#", "     ");
            String mapping = tab_.replace("##Mapping##",
                    fonction_.getElementsByTagName("type").item(0).getTextContent().trim().replace("#nameController#",
                            capitalizeFirstLetter(this.sqlreading.getNomTable())));
            String finals = mapping.replace("##nameCa##",
                    fonction_.getElementsByTagName("value").item(0).getTextContent().trim().replace("#nameController#",
                            capitalizeFirstLetter(
                                    this.sqlreading.getNomTable())));
            String css_finals = finals.replace("#down#", "\n");
            String content_fonction = css_finals.replace("##content_fonction##", content_id_default)
                    .replace("#nameController#",
                            capitalizeFirstLetter(this.sqlreading.getNomTable()))
                    .replace("#name#",
                            this.sqlreading.getNomTable());
            fonction_standard += content_fonction.replace("#attribut_insert#",
                    InsertChange(con, sqlreading, template_attribut_insert));
        }
        String[] allconfig = { importations, conf_header, fonction_standard };
        return allconfig;
    }

    public String InsertChange(Connection connection, ReaderSql name, String template) {
        String change = "";
        try {
            List<String> columns_not_primary = name.Column_not_primary(connection);
            if (name.TypePrimaryKey(connection).equalsIgnoreCase("string")) {
                change += template.replace("entity.#column#", "\"\" + resulat").replace("#column#",
                        name.checkIfAttributeIsPrimaryKey(connection));
            }
            for (int i = 0; i < columns_not_primary.size(); i++) {
                if (i != columns_not_primary.size() - 1) {
                    change += template.replace("#column#", columns_not_primary.get(i));
                } else {
                    change += template.replace("#column#", columns_not_primary.get(i)).replace(",", "");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return change;
    }

    public void generateController(Connection con, String path, String techno, String packages, ReaderSql name)
            throws Exception {
        String template_controller = affiche_template("templatecontroller.txt");
        String[] configs_xml = getConfigAttribut();
        String nomfichier = NameFichierController(techno);
        String names = "";
        String config_entity = "";
        if (techno.compareToIgnoreCase("JAVA") == 0 || techno.compareToIgnoreCase("Framework") == 0) {
            names = this.sqlreading.getNomTable() + "Controller";
            config_entity = template_controller.replace("##configEntity##", "");
        } else {
            names = this.sqlreading.getNomTable() + "Controller : ControllerBase";
            config_entity = template_controller.replace("##configEntity##", configs_xml[8]).replace("#nameclasses#",
                    capitalizeFirstLetter(name.getNomTable()));
        }
        try {
            File fichier = PathFichier(path, nomfichier);
            FileWriter writers = new FileWriter(fichier);
            String pakage = changepackage(techno, config_entity).replace("#namepackage#", (packages));
            String lib = pakage.replace("#importing#",
                    Config(con, techno, "addmapping.txt")[0]);
            String configs = lib.replace("#configController#",
                    Config(con, techno, "addmapping.txt")[1]);
            String name_classes = configs.replace("#NameController#", capitalizeFirstLetter(names));
            String finals = name_classes.replace("#MethodeAppeler#",
                    Config(con, techno, "addmapping.txt")[2]);
            String chfinals = finals.replace("##down##", "\n");
            String enfunction = chfinals.replace("##endfunction##", configs_xml[3]);
            String visbling = enfunction.replace("#visible#", configs_xml[0]);
            String final_change_update = visbling.replace("##change##",
                    name.updateChange(con))
                    .replace("#primamrykeys#",
                            name.checkIfAttributeIsPrimaryKey(con));
            String change_label = final_change_update.replace("#label#",
                    name.getColumnShow(con, "label"));
            String change_password = change_label.replace("#password#",
                    name.getColumnShow(con, "password")).replace("and__", "&&");
            String change_type_primary = change_password.replace("#type_primary#", name.TypePrimaryKey(con));
            String table_auth = name.getTableAuth(con);
            String final_controller = change_type_primary.replace("#AUTH#", capitalizeFirstLetter(table_auth))
                    .replace("#auth#", table_auth);
            writers.write(final_controller);
            writers.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AllExecute(Connection con, String path_build, String path_view, List<ReaderSql> readers,
            String database, String user,
            String password) {
        try {
            ChangeIntoApp(con, path_build, readers, database, user, password);
            CreateStructure(path_view, readers);
            Change_config_service_view(con, path_view, readers);
            Change_config_routing_view(path_view, readers);
            Sidebar_acceuil(path_view, readers);
            Sidebar(path_view, readers);
            Home_ts(path_view, readers);
            Home_html(con, path_view, readers);
            InsertTs(con, path_view, readers);
            InsertHtml(con, path_view, readers);
            ModalTs(con, path_view, readers);
            ModalHtml(con, path_view, readers);
            UpdateTs(con, path_view, readers);
            UpdateHtml(con, path_view, readers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

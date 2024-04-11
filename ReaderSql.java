package reading;

import java.sql.*;
import java.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import connexion.*;
import writing.Writing;

public class ReaderSql {
    String package_table;
    String nomTable;
    List<String> importation;
    List<String> name_column;
    List<String> type_column;

    public String getPackage_table() {
        return package_table;
    }

    public void setPackage_table(String package_table) {
        this.package_table = package_table;
    }

    public String getNomTable() {
        return nomTable;
    }

    public void setNomTable(String nomTable) {
        this.nomTable = nomTable;
    }

    public List<String> getName_column() {
        return name_column;
    }

    public void setName_column(List<String> name_column) {
        this.name_column = name_column;
    }

    public List<String> getType_column() {
        return type_column;
    }

    public void setType_column(List<String> type_column) {
        this.type_column = type_column;
    }

    public List<String> getImportation() {
        return importation;
    }

    public void setImportation(List<String> importation) {
        this.importation = importation;
    }

    public ReaderSql(String package_table, String nomTable) {
        this.package_table = package_table;
        this.nomTable = nomTable;
    }

    public ReaderSql(String nomTable) {
        this.nomTable = nomTable;
    }

    public ReaderSql(String pack, String nomTable, List<String> name_column, List<String> type_column,
            List<String> lib) {
        this.package_table = pack;
        this.nomTable = nomTable;
        this.name_column = name_column;
        this.type_column = type_column;
        this.importation = lib;
    }

    public ReaderSql() {
    }

    public String[] configs_retour_java() throws Exception {
        NodeList config = new Writing().getMyDocument("cs.xml").getElementsByTagName("config");
        Element conf = (Element) config.item(0);
        Element typeRetour = (Element) conf.getElementsByTagName("typeRetour").item(0);
        Element java = (Element) typeRetour.getElementsByTagName("java").item(0);
        String doubles = java.getElementsByTagName("Double").item(0).getTextContent();
        String chars = java.getElementsByTagName("Char").item(0).getTextContent();
        String floats = java.getElementsByTagName("Float").item(0).getTextContent();
        String ints = java.getElementsByTagName("Int").item(0).getTextContent();
        String TimeSpans = java.getElementsByTagName("TimeStamp").item(0).getTextContent();
        String Boo = java.getElementsByTagName("Boolean").item(0).getTextContent();
        String Times = java.getElementsByTagName("Time").item(0).getTextContent();
        String Dates = java.getElementsByTagName("Date").item(0).getTextContent();
        String Strins = java.getElementsByTagName("String").item(0).getTextContent();

        String[] retour_java = { doubles, chars, floats, ints, TimeSpans, Boo, Times, Dates, Strins };
        return retour_java;
    }

    public String[] configs_retour_Cs() throws Exception {
        NodeList config = new Writing().getMyDocument("cs.xml").getElementsByTagName("config");
        Element conf = (Element) config.item(0);
        Element typeRetour = (Element) conf.getElementsByTagName("typeRetour").item(0);
        Element csharp = (Element) typeRetour.getElementsByTagName("csharp").item(0);
        String doubles = csharp.getElementsByTagName("Double").item(0).getTextContent();
        String chars = csharp.getElementsByTagName("Char").item(0).getTextContent();
        String floats = csharp.getElementsByTagName("Float").item(0).getTextContent();
        String ints = csharp.getElementsByTagName("Int").item(0).getTextContent();
        String TimeSpans = csharp.getElementsByTagName("TimeStamp").item(0).getTextContent();
        String Boo = csharp.getElementsByTagName("Boolean").item(0).getTextContent();
        String Times = csharp.getElementsByTagName("Time").item(0).getTextContent();
        String Dates = csharp.getElementsByTagName("Date").item(0).getTextContent();
        String Strins = csharp.getElementsByTagName("String").item(0).getTextContent();

        String[] retour_csharp = { doubles, chars, floats, ints, TimeSpans, Boo, Times, Dates, Strins };
        return retour_csharp;
    }

    public String[] lib_java() throws Exception {
        String importing = "package";
        NodeList config = new Writing().getMyDocument("cs.xml").getElementsByTagName("config");
        Element conf = (Element) config.item(0);
        Element typeRetour = (Element) conf.getElementsByTagName("libs").item(0);
        Element java = (Element) typeRetour.getElementsByTagName("java").item(0);
        String date = java.getElementsByTagName("Date").item(0).getTextContent();
        String local = java.getElementsByTagName("LocalDateTime").item(0).getTextContent();
        String times = java.getElementsByTagName("Time").item(0).getTextContent();
        String[] retour_lib = { date, local, times };
        return retour_lib;
    }

    public String getTableAuth(Connection connection) {
        try {
            String sql = "SELECT c.relname AS table_name " +
                    "FROM pg_class c " +
                    "JOIN pg_description d ON c.oid = d.objoid " +
                    "WHERE d.description ILIKE ? " +
                    "AND c.relkind = 'r'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "%Auth%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String table_auth = rs.getString("table_name");
                return table_auth;
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void CreateAuth(Connection connection) {
        try {
            Statement pstmt = connection.createStatement();
            String createTableSQL = "CREATE TABLE UserAuth ( " +
                    "id SERIAL PRIMARY KEY, " +
                    "email VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL" +
                    ");";
            pstmt.executeUpdate(createTableSQL);
            String addCommentSQL = "COMMENT ON TABLE UserAuth IS 'Auth';";
            String email_sql = "COMMENT ON COLUMN UserAuth.email IS 'label';";
            String password_sql = "COMMENT ON COLUMN UserAuth.password IS 'password';";
            pstmt.executeUpdate(addCommentSQL);
            pstmt.executeUpdate(email_sql);
            pstmt.executeUpdate(password_sql);
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getColumnShow(Connection connection, String column_comment) {
        try {
            String sql = "SELECT a.attname AS column_name, " +
                    "pd.description AS column_comment " +
                    "FROM pg_attribute a " +
                    "LEFT JOIN pg_description pd ON a.attrelid = pd.objoid AND a.attnum = pd.objsubid " +
                    "WHERE a.attrelid = ?::regclass";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, this.nomTable);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                String columnComment = rs.getString("column_comment");
                if (columnComment != null && !columnComment.isEmpty()
                        && columnComment.equalsIgnoreCase(column_comment)) {
                    return columnName;
                }
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkIfAttributeIsPrimaryKey(connection);
    }

    public String getNameTableReferenced(Connection connection, String column) {
        try {
            String sql = "SELECT ccu.table_name AS referenced_table_name " +
                    "FROM information_schema.key_column_usage kcu " +
                    "LEFT JOIN information_schema.table_constraints tc ON kcu.constraint_name = tc.constraint_name " +
                    "LEFT JOIN information_schema.constraint_column_usage ccu ON tc.constraint_name = ccu.constraint_name "
                    +
                    "WHERE kcu.table_name = ? AND kcu.column_name = ? AND tc.constraint_type = 'FOREIGN KEY'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, this.nomTable);
            pstmt.setString(2, column);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String referencedTableName = rs.getString("referenced_table_name");
                return referencedTableName;
            }
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String checkIfAttributeIsPrimaryKey(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, this.nomTable);

            while (primaryKeys.next()) {
                String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
                for (int i = 0; i < this.getName_column().size(); i++) {
                    if (primaryKeyColumnName.equals(this.getName_column().get(i))) {
                        return this.getName_column().get(i);
                    }
                }
            }
            primaryKeys.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String TypePrimaryKey(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, this.nomTable);

            while (primaryKeys.next()) {
                String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
                for (int i = 0; i < this.getName_column().size(); i++) {
                    if (primaryKeyColumnName.equals(this.getName_column().get(i))) {
                        return this.getType_column().get(i);
                    }
                }
            }
            primaryKeys.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String updateChange(Connection connection) {
        String change = "";
        try {
            List<String> columns_not_primary = Column_not_primary(connection);
            for (int i = 0; i < columns_not_primary.size(); i++) {
                change += "     yourattribut." + columns_not_primary.get(i) + "=" + "entity."
                        + columns_not_primary.get(i) + ";\n";

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return change;
    }

    public List<String> Column_not_primary(Connection connection) {
        List<String> column_not_primary = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, this.nomTable);
            while (primaryKeys.next()) {
                String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
                for (int i = 0; i < this.getName_column().size(); i++) {
                    if (!primaryKeyColumnName.equals(this.getName_column().get(i))) {
                        column_not_primary.add(this.getName_column().get(i));
                    }
                }
            }
            primaryKeys.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return column_not_primary;
    }

    public List<String> Column_type(Connection connection) {
        List<String> column_type = new ArrayList<String>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, this.nomTable);
            while (primaryKeys.next()) {
                String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
                for (int i = 0; i < this.getName_column().size(); i++) {
                    if (!primaryKeyColumnName.equals(this.getName_column().get(i))) {
                        column_type.add(this.getType_column().get(i));
                    }
                }
            }
            primaryKeys.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return column_type;
    }

    public List<ReaderSql> allTable(String techno, String packa, Connection connexion) {
        List<ReaderSql> allsql = new ArrayList<ReaderSql>();
        try {
            DatabaseMetaData metaData = connexion.getMetaData();
            ResultSet alltable = metaData.getTables(null, null, "%", new String[] { "TABLE" });
            while (alltable.next()) {
                String tableName = alltable.getString("TABLE_NAME");
                if ((tableName.contains("pg_") == false)) {
                    if (tableName.contains("sql_") == false) {
                        System.out.println("Table : " + tableName);
                        ResultSet resultset = metaData.getColumns(null, null, tableName, null);
                        List<String> column_reader = new ArrayList<>();
                        List<String> type_reader = new ArrayList<>();
                        List<String> lib = new ArrayList<>();
                        while (resultset.next()) {
                            String nomColonne = resultset.getString("COLUMN_NAME");
                            int type_columns = resultset.getInt("DATA_TYPE");
                            column_reader.add(nomColonne);
                            if (techno.compareToIgnoreCase("JAVA") == 0) {
                                type_reader.add(getTypeForColumn_java(type_columns));
                            } else {
                                System.out.println("atomically");
                                type_reader.add(getTypeForColumn_csharp(type_columns));
                            }
                            lib.add(getImporation(techno, type_columns));
                        }
                        resultset.close();
                        allsql.add(new ReaderSql(packa, tableName, column_reader, type_reader, lib));
                    }
                }
            }
            alltable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allsql;
    }

    public ReaderSql aboutThisTable(String techno, String packa, String table, Connection connexion) {
        ReaderSql myreader = null;
        try {
            DatabaseMetaData metaData = connexion.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, table, null);
            List<String> column_reader = new ArrayList<>();
            List<String> type_reader = new ArrayList<>();
            List<String> lib = new ArrayList<>();
            while (resultSet.next()) {
                String nomColonne = resultSet.getString("COLUMN_NAME");
                int type_columns = resultSet.getInt("DATA_TYPE");
                column_reader.add(nomColonne);
                if (techno.compareToIgnoreCase("JAVA") == 0) {
                    type_reader.add(getTypeForColumn_java(type_columns));
                } else {
                    type_reader.add(getTypeForColumn_csharp(type_columns));
                }
                lib.add(getImporation(techno, type_columns));
            }
            myreader = new ReaderSql(packa, table, column_reader, type_reader, lib);
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myreader;
    }

    public String getImporation(String techno, int columntype) throws SQLException, Exception {
        if (techno.compareToIgnoreCase("JAVA") == 0) {
            switch (columntype) {
                case Types.DATE:
                    return lib_java()[0];
                case Types.TIMESTAMP:
                    return lib_java()[1];
                case Types.TIME:
                    return lib_java()[2];
                default:
                    return "";
            }
        }
        return "";
    }

    public String getTypeForColumn_java(int columntype) throws SQLException, Exception {
        switch (columntype) {
            case Types.BIT:
                return "BIT";
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.BIGINT:
            case Types.INTEGER:
                return configs_retour_java()[3];
            case Types.FLOAT:
                return configs_retour_java()[2];
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return configs_retour_java()[0];
            case Types.CHAR:
                return configs_retour_java()[1];
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return configs_retour_java()[8];
            case Types.DATE:
                return configs_retour_java()[7];
            case Types.TIME:
                return configs_retour_java()[6];
            case Types.TIMESTAMP:
                return configs_retour_java()[4];
            case Types.BOOLEAN:
                return configs_retour_java()[5];
            default:
                return "UNKNOWN";
        }
    }

    public String getTypeForColumn_csharp(int columntype) throws SQLException, Exception {
        switch (columntype) {
            case Types.BIT:
                return "BIT";
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.BIGINT:
            case Types.INTEGER:
                return configs_retour_Cs()[3];
            case Types.FLOAT:
                return configs_retour_Cs()[2];
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return configs_retour_Cs()[0];
            case Types.CHAR:
                return configs_retour_Cs()[1];

            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return configs_retour_Cs()[8];
            case Types.DATE:
                return configs_retour_Cs()[7];
            case Types.TIME:
                return configs_retour_Cs()[6];
            case Types.TIMESTAMP:
                return configs_retour_Cs()[4];
            case Types.BOOLEAN:
                return configs_retour_Cs()[5];
            default:
                return "UNKNOWN";
        }
    }
}
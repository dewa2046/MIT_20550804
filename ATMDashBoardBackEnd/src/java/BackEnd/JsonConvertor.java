/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BackEnd;

/**
 *
 * @author Administrator
 */
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonConvertor {

    public JSONArray rsToJson(ResultSet rsx, JSONArray jsonx) throws Exception {

        ResultSetMetaData rsmd = rsx.getMetaData();
        Date dt1;
        String strDate;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (rsx.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i < numColumns + 1; i++) {
                String column_name = rsmd.getColumnLabel(i);
                if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                    obj.put(column_name, rsx.getArray(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                    obj.put(column_name, rsx.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                    obj.put(column_name, rsx.getBoolean(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                    obj.put(column_name, rsx.getBlob(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                    obj.put(column_name, rsx.getDouble(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                    obj.put(column_name, rsx.getFloat(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                    obj.put(column_name, rsx.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                    obj.put(column_name, rsx.getNString(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
//                    String val = "";
//                    if (rsx.getString(column_name) != null) {
//                        val = rsx.getString(column_name);
//                    }
//                    obj.put(column_name, val);
                    obj.put(column_name, rsx.getString(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                    obj.put(column_name, rsx.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                    obj.put(column_name, rsx.getInt(column_name));
                } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                    Timestamp timestamp = rsx.getTimestamp(column_name);
                    if (timestamp != null) {
                        dt1 = new java.util.Date(timestamp.getTime());
                        strDate = formater.format(dt1);
                        obj.put(column_name, strDate);
                    }
//                    obj.put(column_name, rsx.getDate(column_name));

                } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                    obj.put(column_name, rsx.getTimestamp(column_name));
                } else {
                    obj.put(column_name, rsx.getObject(column_name));
                }
            }
            jsonx.put(obj);
            //jsonx.add(obj);
        }
        return jsonx;
    }

    public JSONArray rsToJson(ResultSet rsx, JSONArray jsonx, String TitleCol) throws Exception {

        //Print Resultset
//        String Fullrs="";
//            while(rsx.next())
//                {
//                   Fullrs=Fullrs+", PICKLIST_NAME="+rsx.getString("PICKLIST_NAME")+" PICKLIST_CODE="+rsx.getString("PICKLIST_CODE")+" PICK_VALUE "+rsx.getString("PICK_VALUE");
//                }                        
//            System.out.println("Fullrs="+Fullrs);
        JSONArray ResultJSON = new JSONArray();
        JSONArray TempJSON = new JSONArray();

        ResultSetMetaData rsmd = rsx.getMetaData();
        int numColumns = rsmd.getColumnCount();

        String TitleColumn = "", TitleColumnCollection = "";

        String Result = "";

        JSONObject Resultobj = new JSONObject();

        while (rsx.next()) {

            JSONObject obj = new JSONObject();

            if (TitleColumn.equals(rsx.getString(TitleCol))) {
                for (int i = 1; i < numColumns; i++) {
                    String column_name = rsmd.getColumnLabel(i);

                    if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                        obj.put(column_name, rsx.getArray(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                        obj.put(column_name, rsx.getString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                        obj.put(column_name, rsx.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                        obj.put(column_name, rsx.getBoolean(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                        obj.put(column_name, rsx.getBlob(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                        obj.put(column_name, rsx.getDouble(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                        obj.put(column_name, rsx.getFloat(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                        obj.put(column_name, rsx.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                        obj.put(column_name, rsx.getNString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                        obj.put(column_name, rsx.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                        obj.put(column_name, rsx.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                        obj.put(column_name, rsx.getDate(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                        obj.put(column_name, rsx.getTimestamp(column_name));
                    } else {
                        obj.put(column_name, rsx.getObject(column_name));
                    }
                }
                TempJSON.put(obj);

            } else {
                if (!TitleColumn.equals("")) {
                    Resultobj.put(TitleColumn.replaceAll("\\s+", ""), TempJSON);
                    TempJSON = new JSONArray();
                }

                //TitleColumn=rsx.getString(TitleCol).replace(" ", "");
                TitleColumn = rsx.getString(TitleCol);

                //Generate Column List
                TitleColumnCollection = TitleColumnCollection + "," + TitleColumn.replaceAll("\\s+", "");

                for (int i = 1; i < numColumns; i++) {
                    String column_name = rsmd.getColumnLabel(i);

                    if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                        obj.put(column_name, rsx.getArray(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                        obj.put(column_name, rsx.getString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                        obj.put(column_name, rsx.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                        obj.put(column_name, rsx.getBoolean(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                        obj.put(column_name, rsx.getBlob(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                        obj.put(column_name, rsx.getDouble(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                        obj.put(column_name, rsx.getFloat(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                        obj.put(column_name, rsx.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                        obj.put(column_name, rsx.getNString(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                        obj.put(column_name, rsx.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                        obj.put(column_name, rsx.getInt(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                        obj.put(column_name, rsx.getDate(column_name));
                    } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                        obj.put(column_name, rsx.getTimestamp(column_name));
                    } else {
                        obj.put(column_name, rsx.getObject(column_name));
                    }
                }
                TempJSON.put(obj);

            }
        }

        Resultobj.put(TitleColumn.replaceAll("\\s+", ""), TempJSON);
        //Resultobj.put(TitleCol,TitleColumnCollection);
        ResultJSON.put(Resultobj);

        return ResultJSON;
    }

    public JSONArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        String docList = "";
        String isMandatory = "";

        docList = resultSet.getMetaData().getColumnLabel(2).toLowerCase();
        isMandatory = resultSet.getMetaData().getColumnLabel(4).toLowerCase();

        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            //docList=resultSet.getMetaData().getColumnLabel(1).toLowerCase();
            //isMandatory=
            for (int i = 0; i < total_rows; i++) {
                JSONObject obj = new JSONObject();
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase(), resultSet.getObject(i + 1));
                jsonArray.put(obj);
                System.out.println();
                //isMandatory=resultSet.getMetaData().getColumnLabel(i + 1).toString();
            }

            System.out.println(docList);
            System.out.println(isMandatory);
        }
        return jsonArray;
    }

    public String convertToXML(ResultSet resultSet)
            throws Exception {
        StringBuffer xmlArray = new StringBuffer("<results>");
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            xmlArray.append("<result ");
            for (int i = 0; i < total_rows; i++) {
                xmlArray.append(" " + resultSet.getMetaData().getColumnLabel(i + 1)
                        .toLowerCase() + "='" + resultSet.getObject(i + 1) + "'");
            }
            xmlArray.append(" />");
        }
        xmlArray.append("</results>");
        return xmlArray.toString();
    }

    public JSONObject ConvertStringToJson(String ReqString) {

        JSONObject RequestJson = null;
        try {
            RequestJson = new JSONObject(ReqString);
            System.out.println("Successfully converted string to json by ConvertStringToJson");
        } catch (JSONException ex) {
            System.out.println("Error in converting string to json" + ex.toString());

            try {
                JSONObject obj = new JSONObject("\"Error\":\"Error in converting string to json\"");
                RequestJson = obj;
            } catch (JSONException ex1) {
                System.out.println("Error in converting string to json" + ex1.toString());
            }
        }

        return RequestJson;
    }

    public JSONArray getSortedArry(JSONArray jsonArray) throws JSONException {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonList = new ArrayList<JSONObject>();

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonList.add(jsonArray.getJSONObject(i));
        }

        Collections.sort(jsonList, new Comparator<JSONObject>() {

            private static final String KEY_NAME = "Name";

            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                } catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonArray.length(); i++) {
            sortedJsonArray.put(jsonList.get(i));
        }
        return sortedJsonArray;

    }

}

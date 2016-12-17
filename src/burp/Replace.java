package burp;

import java.util.List;
import java.util.Iterator;
import java.io.PrintWriter;

/**
 * Created by fruh on 9/7/16.
 */
public class Replace {
    public static String TYPE_REP_SEL = "Replace on selected";
    public static String TYPE_ADD_SEL = "Add new header on selected";
    public static String TYPE_REP_LAST = "Replace on last request";
    public static String TYPE_ADD_LAST = "Add new header on last request";
    public static String TYPE_REP_HEADER_LAST = "Replace header on last request";

    private boolean urlDecode = false;
    private String dataToPaste;
    private String replaceStr;
    private String id;
    private String type;
    private String msgId;
    private Extraction ext;

    public Replace(String id, String replaceStr, String type, Extraction ext) {
        this.id = id;
        this.replaceStr = replaceStr;
        this.type = type;
        this.ext = ext;
    }

    public String getDataToPaste() {
        return dataToPaste;
    }

    public void setDataToPaste(String dataToPaste) {
        this.dataToPaste = dataToPaste;
    }

    public String getReplaceStr() {
        return replaceStr;
    }

    public void setReplaceStr(String replaceStr) {
        this.replaceStr = replaceStr;
    }

    public String replaceData(String request, IExtensionHelpers helpers) {
        if (type.equals(TYPE_REP_SEL) || type.equals(TYPE_REP_LAST)) {
            request = request.replace(replaceStr, dataToPaste);
        } else {
            IRequestInfo rqInfo = helpers.analyzeRequest(request.getBytes());
            List<String> headers = rqInfo.getHeaders();

            if (type.equals(TYPE_REP_HEADER_LAST)){
                for (Iterator<String> iterator = headers.iterator(); iterator.hasNext();){
                    String header = iterator.next();
                    if (header.startsWith(replaceStr)){
                        iterator.remove();
                    }
                }
            }

            //headers.add(replaceStr + ": " + dataToPaste);
            // No ":" is added here, you should place it manually
            headers.add(replaceStr + (urlDecode ? helpers.urlDecode(dataToPaste) : dataToPaste));

            String msgBody = request.substring(rqInfo.getBodyOffset());
            request = new String(helpers.buildHttpMessage(headers, msgBody.getBytes()));
        }
        return request;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getExtId() {
        return ext.getId();
    }

    public Extraction getExt() {
        return ext;
    }

    public boolean isUrlDecode() {
        return urlDecode;
    }

    public void setUrlDecode(boolean urlDecode) {
        this.urlDecode = urlDecode;
    }

    @Override
    public String toString() {
        return "'" + id + "', '" + type + "', '" + replaceStr + "', '" + msgId + "'";
    }
}

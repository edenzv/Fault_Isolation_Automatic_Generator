package org.afeka.fi.backend.pojo.commonstructure;

import org.afeka.fi.backend.common.FiLogger;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PgBoundery {
    Logger logger= FiLogger.getLogger(getClass().getName());
    private String n="";
    private String type="";
    private String status="";
    private String description="";
    private String toYes="";
    private String toNo="";

    public PgBoundery(){

    }

    public PgBoundery(PG pg){
      try {
          n = pg._n;
          if (type!=null)
              type = pg.type;
          status = pg.status;
          if (pg.N.getTo()!=null)
            toNo = pg.N.getTo();
          if (pg.Y.getTo()!=null)
            toYes = pg.Y.getTo();
          description = createDescription(pg);
      }catch (Exception e){
          logger.error("Cannot convert pg to PgBoundery",e);
      }
    }

    private String createDescription(PG pg) {
        String description="";
        if(status.equals(Status.success.name()))
            description= Arrays.stream(pg.htmlObj.getHtmlData()).map(htmlData -> htmlData.txt).collect(Collectors.joining());
        else{
            if(pg.status.startsWith("missing")){
                String[] errors=pg.status.split(",");
                for (String error:errors) {
                    error.replace("missing","");
                    String name=error.substring(0,1).toUpperCase()+error.substring(1);
                    description += name +" is missing, ";
                }
                description.substring(0,description.length()-3);
            }
            else{
                description=pg.status+" please implement this error in PgBoundery.createDescription";
            }
        }
        return description;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToYes() {
        return toYes;
    }

    public void setToYes(String toYes) {
        this.toYes = toYes;
    }

    public String getToNo() {
        return toNo;
    }

    public void setToNo(String toNo) {
        this.toNo = toNo;
    }
}

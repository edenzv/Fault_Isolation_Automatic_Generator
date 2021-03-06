package org.afeka.fi.backend.clients;

import org.afeka.fi.backend.common.FiCommon;
import org.afeka.fi.backend.pojo.ocr.OcrWebServiceResponse;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

@Service("OcrWebService")
@ConfigurationProperties(prefix = "ocrprovider.ocrwebservice")
public class OcrWebService extends FiCommon implements OcrClient{
    private String licensecode;
    private String username;

    @Override
    public MultipartFile run(MultipartFile file) throws Exception {
        logger.called("OcrWebService.run","fiImage",file.getName());
        logger.info("Go to convert "+file.getName() +" to doc file with OcrWebService");
        HttpClientImpl httpClient=new HttpClientImpl();
        String url = "https://www.ocrwebservice.com/restservices/processDocument?language=english&gettext=true&outputformat=doc";
        Header[] headers={
                new BasicHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((username + ":" + licensecode).getBytes()))
                ,new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json")};
        OcrWebServiceResponse ocrWebServiceResponse=httpClient.post(url,headers,file.getBytes(),new OcrWebServiceResponse());
        file=httpClient.getFile(ocrWebServiceResponse.getOutputFileUrl().replaceAll("https","http"),null);
        return file;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLicensecode() {
        return licensecode;
    }

    public void setLicensecode(String licensecode) {
        this.licensecode = licensecode;
    }
}

package fr.imt.raimed2.virtualPatient.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import fr.imt.raimed2.action.dto.xml.ActionsDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@JacksonXmlRootElement(localName = "VirtualPatient")
@Getter
@Setter
public class VirtualPatientDTO implements Serializable {

    @JacksonXmlProperty
    private Integer age;

    @JacksonXmlProperty
    private String gender;

    @JacksonXmlProperty
    private Date createdAt;

    @JacksonXmlProperty(localName = "actions")
    private ActionsDTO actions;

    @JacksonXmlProperty(localName = "result")
    private String result;

}
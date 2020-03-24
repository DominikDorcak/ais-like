package sk.upjs.nosql_data_source.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import de.undercouch.bson4jackson.BsonFactory;
import sk.upjs.nosql_data_source.entity.SimpleStudent;
import sk.upjs.nosql_data_source.entity.Student;
import sk.upjs.nosql_data_source.entity.StudijnyProgram;
import sk.upjs.nosql_data_source.entity.Studium;
import sk.upjs.nosql_data_source.persist.DaoFactory;
import sk.upjs.nosql_data_source.persist.StudentDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentConvertor {


    private List<Student> getStudents(){
        StudentDao dao = DaoFactory.INSTANCE.getStudentDao();
        List<Student> list = dao.getAll();
        return list;
    }
    private List<SimpleStudent> getSimpleStudents(){
        StudentDao dao = DaoFactory.INSTANCE.getStudentDao();
        List<SimpleStudent> list = dao.getSimpleStudents();
        return list;
    }

    public String getObjectJSON(Object o, boolean format) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT,format);
        return mapper.writeValueAsString(o);
    }
    private List<String> getStudentsJSON(boolean format) throws JsonProcessingException {
        List<String> res = new ArrayList<>();
        List<Student> list = getStudents();
        for (Student s: list) {
            res.add(getObjectJSON(s,format));
        }
        return res;
    }

    private byte[] getObjectBSON(Object o) throws IOException {
        BsonFactory factory =  new BsonFactory();
        ObjectMapper om =  new ObjectMapper(factory);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        om.writeValue(out,o);
        return out.toByteArray();
    }

    private List<byte[]> getStudentsBSON() throws IOException {
        List<byte[]> result = new ArrayList<>();
        for (Student s:getStudents()) {
            result.add(getObjectBSON(s));
        }
        return result;
    }

    private String getObjectXML(Object o,boolean format) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT,format);
        return xmlMapper.writeValueAsString(o);
    }

    private List<String> getStudentsXML(boolean format) throws JsonProcessingException {
        List<String> res = new ArrayList<>();
        List<Student> list = getStudents();
        for (Student s: list) {
            res.add(getObjectXML(s,format));
        }
        return res;
    }

    private String getObjectYAML(Object o,boolean format) throws JsonProcessingException {
        YAMLMapper mapper = new YAMLMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT,format);
        return mapper.writeValueAsString(o);
    }

    private List<String> getStudentsYAML(boolean format) throws JsonProcessingException {
        List<String> res = new ArrayList<>();
        List<Student> list = getStudents();
        for (Student s: list) {
            res.add(getObjectYAML(s,format));
        }
        return res;
    }

    private String getObjectCSV(Object o,Class objClass, boolean header) throws JsonProcessingException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema =  mapper.schemaFor(objClass);
        if(header){
            schema.withHeader();
        }
        ObjectWriter owriter = mapper.writer(schema);
        return mapper.writeValueAsString(o);
    }



    public static void main(String[] args) throws IOException {
        StudentConvertor sc = new StudentConvertor();
        List<Student> students =  sc.getStudents();
        List<StudijnyProgram> programs = new ArrayList<>();
        for (Student s:students) {
            for (Studium studium: s.getStudium()) {
                programs.add(studium.getStudijnyProgram());
            }
        }
        //aj po hodinach googlenia a hrania sa stale nefunguje
        System.out.println(sc.getObjectCSV(programs,StudijnyProgram.class,true));

//        List<String> list = new ArrayList<>();
//        try {
//             list =  sc.getStudentsCSV(true);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        for (String s: list) {
//            System.out.println(s);
//        }
//        List<byte[]> stdents = sc.getStudentsBSON();
//        for (byte[] bytes:stdents) {
//            System.out.println(new String(bytes));
//        }
    }
}

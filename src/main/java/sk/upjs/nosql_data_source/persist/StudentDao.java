package sk.upjs.nosql_data_source.persist;

import java.util.List;

import sk.upjs.nosql_data_source.entity.SimpleStudent;
import sk.upjs.nosql_data_source.entity.Student;

public interface StudentDao {
	List<Student> getAll();
	List<SimpleStudent> getSimpleStudents();
}

package part_c;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.Company;
import io.codearte.jfairy.producer.person.Person;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Part_C_model_3 {

	public static void dataGenerator(int N) {
		Fairy fairy = Fairy.create();

		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("model3");
		MongoCollection<Document> companyCollection = database.getCollection("company");

		for (int i = 0; i < N; ++i) {
			// COMPANY:
			Company company = fairy.company();

			Document companyDocument = new Document();
			companyDocument.put("name", company.getName());
			companyDocument.put("domain", company.getDomain());
			companyDocument.put("email", company.getEmail());
			companyDocument.put("vatIdentificationNumber", company.getVatIdentificationNumber());

			// PERSONA:
			List<Document> persons = new ArrayList();
			for (int j = 0; j < N; ++j) {
				Person person = fairy.person();

				Document personDocument = new Document();
				personDocument.put("firstName", person.getFirstName());
				personDocument.put("middleName", person.getMiddleName());
				personDocument.put("lastName", person.getLastName());
				personDocument.put("email", person.getEmail());
				personDocument.put("username", person.getUsername());
				personDocument.put("passportNumber", person.getPassportNumber());
				personDocument.put("address", person.getAddress().toString());
				personDocument.put("password", person.getPassword());
				personDocument.put("sex", person.getSex().toString());
				personDocument.put("telephoneNumber", person.getTelephoneNumber());
				personDocument.put("dateOfBirth", person.getDateOfBirth().toString());
				personDocument.put("age", person.getAge());
				personDocument.put("nationalIdentityCardNumber", person.getNationalIdentityCardNumber());
				personDocument.put("nationalIdentificationNumber", person.getNationalIdentificationNumber());

				persons.add(personDocument);
			}
			companyDocument.put("persons", persons);
			companyCollection.insertOne(companyDocument);

		}
		client.close();
	}
}

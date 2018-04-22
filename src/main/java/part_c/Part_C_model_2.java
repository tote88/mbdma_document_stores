package part_c;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.Company;
import io.codearte.jfairy.producer.person.Person;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

public class Part_C_model_2 {

	public static void dataGenerator(int N) {
		Fairy fairy = Fairy.create();

		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("model2");
		MongoCollection<Document> personCollection = database.getCollection("person");

		for (int i = 0; i < N; ++i) {
			// COMPANY:
			Company company = fairy.company();

			Document companyDocument = new Document();
			companyDocument.put("name", company.getName());
			companyDocument.put("domain", company.getDomain());
			companyDocument.put("email", company.getEmail());
			companyDocument.put("vatIdentificationNumber", company.getVatIdentificationNumber());

			//System.out.println(id.toString());
			// PERSONA:
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
				personDocument.put("companyEmail", person.getFirstName()+"."+person.getLastName()+"@"+company.getDomain());
				personDocument.put("company", companyDocument);

				//System.out.println(personDocument.toJson());

				personCollection.insertOne(personDocument);
			}

		}

		/* QUERY 1: For each person, its full name and company */
		long startTime = System.currentTimeMillis(); // Get time at the start of the query
		FindIterable<Document> results = personCollection.find();
		for (Document doc : results) {
			Document company = (Document) doc.get("company");
			//System.out.println(doc.getString("firstName") + " " + doc.getString("middleName") + " " +
					//doc.getString("lastName") + " works at " + company.get("name"));
		}
		long queryTime = System.currentTimeMillis() - startTime; // Measure query execution time
		System.out.println("\nTIME USED FOR QUERY 1: " + queryTime);

		/* QUERY 2: For each company, name and number of employeer*/
		startTime = System.currentTimeMillis(); // Get time at the start of the query
		results = personCollection.find();
		Map<String, Integer> numberOfEmployees = new HashMap<String, Integer>();
		for (Document doc : results) {
			Document company = (Document) doc.get("company");
			Integer currentNumber = 0;
			String companyName = company.getString("name");
			if (numberOfEmployees.containsKey(companyName)) {
				currentNumber = numberOfEmployees.get(companyName);
				currentNumber++;
			} else {
				currentNumber = 1;
			}
			numberOfEmployees.put(companyName, currentNumber);
		}
		for (Map.Entry<String, Integer> company : numberOfEmployees.entrySet()) {
			System.out.println("At " + company.getKey() + " work " + company.getValue() + " people.");
		}
		queryTime = System.currentTimeMillis() - startTime; // Measure query execution time
		System.out.println("\nTIME USED FOR QUERY 2: " + queryTime);
	}
}

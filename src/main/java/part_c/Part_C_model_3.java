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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
				personDocument.put("companyEmail", person.getFirstName()+"."+person.getLastName()+"@"+company.getDomain());

				persons.add(personDocument);
			}
			companyDocument.put("persons", persons);
			companyCollection.insertOne(companyDocument);

		}
		/* QUERY 1: For each person, its full name and company */
		long startTime = System.currentTimeMillis(); // Get time at the start of the query
		FindIterable<Document> results = companyCollection.find();
		for (Document company : results) {
			ArrayList<Document> persons = (ArrayList) company.get("persons");

			
			for (Document person : persons) {
//				System.out.println(person.getString("firstName") + " " + person.getString("middleName") + " " +
//					person.getString("lastName") + " works at " + company.get("name"));
			}
		}
		long queryTime1 = System.currentTimeMillis() - startTime; // Measure query execution time
		
		/* QUERY 2: For each company, name and number of employees*/
		startTime = System.currentTimeMillis(); // Get time at the start of the query
		results = companyCollection.find();
		for (Document company : results) {
			ArrayList<Document> persons = (ArrayList) company.get("persons");

//			System.out.println("At " + company.get("name") + " work " + persons.size() + " people.");
		}
		long queryTime2 = System.currentTimeMillis() - startTime; // Measure query execution time
		
		/* QUERY 3 For each person born before 1988, update age to 30*/
		startTime = System.currentTimeMillis(); // Get time at the start of the query
		results = companyCollection.find();
		
		Document update = new Document();
		Document ageDoc = new Document();
		ageDoc.put("persons.$[].age", 30);
		update.put("$set", ageDoc);
		
		Document query = new Document();
		Document lessThan1988 = new Document();
		lessThan1988.put("$lt", "1988-01-01");
		query.put("persons.dateOfBirth", lessThan1988);
		companyCollection.updateMany(query, update);
		long queryTime3 = System.currentTimeMillis() - startTime; // Measure query execution time

		/* QUERY 4 update company name to include Company word*/
		startTime = System.currentTimeMillis(); // Get time at the start of the query

		results = companyCollection.find();
		for (Document doc : results) {
			ObjectId companyId = doc.getObjectId("_id");
			String companyName = doc.getString("name").concat(" Company");
			companyCollection.updateOne(
					new Document("_id", companyId),
					new Document("$set", new Document("name", companyName))
			);
		}
		long queryTime4 = System.currentTimeMillis() - startTime; // Measure query execution time


		client.close();
		System.out.println("\nTIME USED FOR QUERY 1: " + queryTime1);
		System.out.println("\nTIME USED FOR QUERY 2: " + queryTime2);
		System.out.println("\nTIME USED FOR QUERY 3: " + queryTime3);
		System.out.println("\nTIME USED FOR QUERY 4: " + queryTime4);
	}
}
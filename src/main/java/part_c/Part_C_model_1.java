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
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Part_C_model_1 {

	public static void dataGenerator(int N) {
		Fairy fairy = Fairy.create();

		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("model1");
		MongoCollection<Document> personCollection = database.getCollection("person");
		MongoCollection<Document> companyCollection = database.getCollection("company");

		personCollection.drop();
		companyCollection.drop();

		for (int i = 0; i < N; ++i) {
			// COMPANY:
			Company company = fairy.company();

			Document companyDocument = new Document();
			companyDocument.put("name", company.getName());
			companyDocument.put("domain", company.getDomain());
			companyDocument.put("email", company.getEmail());
			companyDocument.put("vatIdentificationNumber", company.getVatIdentificationNumber());

			companyCollection.insertOne(companyDocument);

			ObjectId id = companyDocument.getObjectId("_id");

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
				personDocument.put("companyEmail", person.getFirstName().toLowerCase()+"."+person.getLastName().toLowerCase()+"@"+company.getDomain());
				personDocument.put("companyId", id);

				personCollection.insertOne(personDocument);
			}


		}

		/* QUERY 1: For each person, its full name and company */
        long startTime = System.currentTimeMillis(); // Get time at the start of the query
        FindIterable<Document> results = personCollection.find();
        for (Document doc : results) {
			ObjectId companyId = doc.getObjectId("companyId");
			Document query = new Document();
			query.put("_id", companyId);
			Document company = companyCollection.find(query).first();

			System.out.println(doc.getString("firstName") + " " + doc.getString("middleName") + " " +
					doc.getString("lastName") + " works at " + company.get("name"));
        }
        long queryTime1 = System.currentTimeMillis() - startTime; // Measure query execution time


		/* QUERY 2: For each company, name and number of employees*/
		startTime = System.currentTimeMillis(); // Get time at the start of the query
		results = companyCollection.find();
		for (Document doc : results) {
			ObjectId companyId = doc.getObjectId("_id");
			Document query = new Document();
			query.put("companyId", companyId);

			Long nPeople = personCollection.count(query);

			System.out.println("At " + doc.get("name") + " work " + nPeople.toString() + " employees");
		}
		long queryTime2 = System.currentTimeMillis() - startTime; // Measure query execution time

		/* QUERY 3 For each person born before 1988, update age to 30*/
		startTime = System.currentTimeMillis(); // Get time at the start of the query

		Document update = new Document();
		Document ageDoc = new Document();
		ageDoc.put("age", 30);
		update.put("$set", ageDoc);

		Document query = new Document();
		Document lessThan1988 = new Document();
		lessThan1988.put("$lt", "1988-01-01");
		query.put("dateOfBirth", lessThan1988);
		personCollection.updateMany(query, update);


		long queryTime3 = System.currentTimeMillis() - startTime; // Measure query execution time

		/* QUERY 4 update company name to include Company word*/
		startTime = System.currentTimeMillis(); // Get time at the start of the query

		results = companyCollection.find();
		for (Document doc : results) {
			ObjectId companyId = doc.getObjectId("_id");
			String companyName = doc.get("name").toString().concat(" Company");

			companyCollection.updateOne(
					new Document("_id", companyId),
					new Document("$set", new Document("name", companyName)));
		}

		long queryTime4 = System.currentTimeMillis() - startTime; // Measure query execution time

		/*resultsAfter = companyCollection.find();
		for (Document doc : resultsAfter) {
			System.out.println(doc.get("name"));
		}*/

		client.close();
		System.out.println("\nTIME USED FOR QUERY 1: " + queryTime1);
		System.out.println("\nTIME USED FOR QUERY 2: " + queryTime2);
		System.out.println("\nTIME USED FOR QUERY 3: " + queryTime3);
		System.out.println("\nTIME USED FOR QUERY 4: " + queryTime4);
	}
}

package part_c;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.company.Company;
import io.codearte.jfairy.producer.person.Person;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Part_C_model_1 {

	public static void dataGenerator(int N) {
		Fairy fairy = Fairy.create();

		MongoClient client = new MongoClient();
		MongoDatabase database = client.getDatabase("model1");
		MongoCollection<Document> personCollection = database.getCollection("person");
		MongoCollection<Document> companyCollection = database.getCollection("company");

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
			personDocument.put("companyId", id);

			System.out.println(personDocument.toJson());

			personCollection.insertOne(personDocument);


		}

		/*
		Document query = new Document();
		query.put("firstName", oneName);

		long startTime = System.currentTimeMillis(); // Get time at the start of the query
		String queryResult = collection.find(query).first().toJson();
		long queryTime = System.currentTimeMillis() - startTime; // Measure query execution time

		System.out.println("First inserted person [ " + queryTime + " ms]: " + queryResult);
		*/
		client.close();
	}
}
